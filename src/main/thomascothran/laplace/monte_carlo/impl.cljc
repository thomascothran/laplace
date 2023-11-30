(ns thomascothran.laplace.monte-carlo.impl)

(defprotocol TrialResultsAccumulator
  "Trials can be a huge sequence of numbers. Usually it will 
  need to be compressed, as for example in a T-Digest."
  (accumulate [this trials]))

(defn run
  "Run a monte carlo simulation, returning a histogram

  Params:
  - `:monte-carlo/trial` - a fn that runs a trial when called
  - `:monte-carlo/iterations` - number of iterations to run
  - `:monte-carlo/trial-results-accumulator` - compress results

  Returns the accumulator applied to the trial results"
  [{trial       :monte-carlo/trial
    iterations  :monte-carlo/iterations
    accumulator :monte-carlo/trial-results-accumulator}]

  (->> (repeatedly trial)
       (take iterations)
       (accumulate accumulator)))

(comment
  (let [accumulator (reify TrialResultsAccumulator
                      (accumulate [_ trial-results]
                        trial-results))]
    (-> (run {:monte-carlo/iterations 10
              :monte-carlo/trial-results-accumulator accumulator
              :monte-carlo/trial      #(rand-nth [0 1 2 3])}))))

