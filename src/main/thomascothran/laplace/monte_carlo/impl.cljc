(ns thomascothran.laplace.monte-carlo.impl
  (:require [hyperfiddle.rcf :refer [tests]]))

(defn run
  "Run a monte carlo simulation, returning a histogram

  Params:
  - `:monte-carlo/trial` - a fn that runs a trial when called
  - `:monte-carlo/iterations` - number of iterations to run
  - `:monte-carlo/trial-results-accumulator` - compress results

  Returns the accumulator applied to the trial results"
  [{trial       :monte-carlo/trial
    iterations  :monte-carlo/iterations}]
  (->> (repeatedly trial)
       (take iterations)))

(tests
 (def result
   (run {:monte-carlo/iterations 10
         :monte-carlo/trial      (constantly 1)}))

 "Should return a sequence of `iterations`"
 (count result) := 10

 "Should contain the specified values"
 (every? (partial = 1) result) := true)
