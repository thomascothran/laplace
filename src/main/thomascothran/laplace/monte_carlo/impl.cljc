(ns thomascothran.laplace.monte-carlo.impl
  (:require [clojure.core.async :as a]
            [kixi.stats.core :refer [histogram]]
            [kixi.stats.distribution :as dist]))

(defn run
  "Run a monte carlo simulation, returning a histogram

  Params:
  - `:monte-carlo/trial` - a fn that runs a trial when called
  - `:monte-carlo/iterations` - number of iterations to run

  Returns `m` with:
  - `:monte-carlo/histogram`- Resulting histogram
  - `:monte-carlo/trials` - the result of the trials"
  [{trial :monte-carlo/trial
    iterations :monte-carlo/iterations
    :as m}]
  (let [trials (->> (repeatedly trial)
                    (take iterations))
        hist (transduce identity histogram trials)]
    (assoc m
           :monte-carlo/trials trials
           :monte-carlo/histogram hist)))

(comment
  (-> (run {:monte-carlo/iterations 10
            :monte-carlo/trial      #(rand-nth [0 1 2 3])})
      :monte-carlo/histogram
      #_dist/cdf
      dist/summary))

;; (defn- -run-async
;;   [{trial :monte-carlo/trial
;;     iterations :monte-carlo/iterations
;;     out-chan ::out-chan
;;     in-chan ::in-chan
;;     :or {in-chan (a/chan 50)
;;          out-chan (a/chan)}
;;     :as m}]

;;   (let [xf (comp (map (fn [_] (trial)))
;;                  histogram)]
;;     (a/pipeline 4 out-chan histogram in-chan))
;;   ;;
;;   (a/go-loop [i 0]
;;     (when-not (= iterations i)
;;       (a/>! in-chan trial)
;;       (recur (inc i))))

;;   (let [hist (->> (repeatedly trial)
;;                   (take iterations)
;;                   (transduce identity histogram))]
;;     (assoc m :monte-carlo/histogram hist)))
