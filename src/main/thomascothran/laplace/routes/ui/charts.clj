(ns thomascothran.laplace.routes.ui.charts
  (:require
   [clojure.edn :as edn]
   [clojure.string :as str]
   [reitit.coercion.malli]
   [kixi.stats.distribution :as dist]
   [thomascothran.laplace.monte-carlo.models.throughput :as mct]
   [thomascothran.laplace.ui-components.atom.charts :as ac]
   [thomascothran.laplace.kixi :as lkx]))

(defn simple-throughput-model-post
  [req]
  (let [{:keys [min-work-item-no
                max-work-item-no
                throughput-dist]} (get-in req [:parameters :form])

        work-item-distribution
        (if (or (nil? max-work-item-no) (= "" max-work-item-no)
                (= min-work-item-no max-work-item-no ))
          (dist/categorical {min-work-item-no 1})
          (dist/uniform {:a min-work-item-no
                         :b max-work-item-no}))
        throughput-distribution
        (->> (str/split throughput-dist #"( |,)+")
             (map edn/read-string)
             (lkx/occurrences->categorical-dist))

        capacity-distribution              (dist/categorical {1 1})

        {histogram :monte-carlo/histogram} (mct/how-long? {:throughput/distribution throughput-distribution
                                                           :work-items/distribution work-item-distribution
                                                           :capacity/distribution   capacity-distribution})

        xs                                 (lkx/quantiles histogram {:histogram/quantile-step 1})
        ys                                 (->> (range 1 101 1) vec)
        chart-values                       (mapv (fn [x y] {:x x :y y}) xs ys)]
    {:hiccup/fragment
     [:div.is-flex.is-justify-content-center
      (ac/line-chart {:chart/values        chart-values
                           :chart/x-field       :x
                           :chart/y-field       :y
                           :chart/x-field-title "Time"
                           :chart/y-field-title "Confidence"})]}))

(defn routes
  []
  [""
   ["/simple-throughput-model"
    {:post {:parameters {:form [:map
                                [:min-work-item-no {:optional true}
                                 [:or number? [:string {:max 0}]]]
                                [:max-work-item-no number?]
                                [:throughput-dist :string]]}
            :handler  simple-throughput-model-post}}]])
