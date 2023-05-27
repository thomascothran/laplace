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
  (let [{:keys [maximum-work-item-growth
                minimum-work-item-growth
                work-item-no
                throughput-dist]} (get-in req [:parameters :form])

        work-item-distribution
        (if (= maximum-work-item-growth minimum-work-item-growth)
          (dist/categorical {work-item-no 1})
          (dist/uniform {:a (+ work-item-no
                               (* (/ minimum-work-item-growth 100)
                                  work-item-no))
                         :b (+ work-item-no
                               (* (/ maximum-work-item-growth 100)
                                  work-item-no))}))
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
     [:div (ac/line-chart {:chart/values        chart-values
                           :chart/x-field       :x
                           :chart/y-field       :y
                           :chart/x-field-title "Time"
                           :chart/y-field-title "Confidence"})]}))

(defn routes
  []
  [""
   ["/simple-throughput-model"
    {:post {:parameters {:form [:map
                                [:maximum-work-item-growth number?]
                                [:work-item-no number?]
                                [:minimum-work-item-growth number?]
                                [:throughput-dist :string]]}
            :handler  simple-throughput-model-post}}]])
