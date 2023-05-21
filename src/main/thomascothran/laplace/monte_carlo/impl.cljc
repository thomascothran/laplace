(ns thomascothran.laplace.monte-carlo.impl
  (:require [kixi.stats.core :refer [histogram]]
            [kixi.stats.distribution :as dist]))

(defn run
  "Run a monte carlo simulation, returning a histogram

  Params:
  - `:monte-carlo/trial` - a fn that runs a trial when called
  - `:monte-carlo/iterations` - number of iterations to run

  Returns `m` with:
  - `:monte-carlo/histogram`- Resulting histogram"
  [{trial :monte-carlo/trial
    iterations :monte-carlo/iterations
    :as m}]
  (let [hist (->> (repeatedly trial)
                  (take iterations)
                  (transduce identity histogram))]
    (assoc m :monte-carlo/histogram hist)))

(comment
  (-> (run {:monte-carlo/iterations 10
            :monte-carlo/trial      rand})
      :monte-carlo/histogram
      dist/summary))
