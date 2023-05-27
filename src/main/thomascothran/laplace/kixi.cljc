(ns thomascothran.laplace.kixi
  (:require [kixi.stats.distribution :as dist]))

(defn quantiles
  [histogram & [{quantile-step :histogram/quantile-step
                 :or           {quantile-step 1}
                 :as           _opts}]]
  (->> (range 0 100 quantile-step)
       (mapv #(dist/quantile histogram (/ % 100)))))

(comment
  (quantiles (dist/uniform {:a 0 :b 10})))

(defn occurrences->categorical-dist
  "Turn a sequence of occurrences into a
  categorical distribution.

  For example, suppose that the stories completed
  in the last 4 sprints are `[20, 30, 35]`. We want
  to turn this sequence into a categorical distribution."
  [xs]
  (let [occurrence-count (count xs)]
    (->> (frequencies xs)
         (reduce-kv (fn [m k v]
                      (assoc m k (/ v occurrence-count)))
                    {})
         (dist/categorical))))

(comment
  (->>  [20 30 40]
        occurrences->categorical-dist
        (dist/sample-summary 20)))
