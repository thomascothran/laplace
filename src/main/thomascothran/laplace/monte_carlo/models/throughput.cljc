(ns thomascothran.laplace.monte-carlo.models.throughput
  "Use throughput models with a group of work items to
  answer questions like:

  - How long until they are complete?
  - How many items can be complete in a given time frame"
  (:require [kixi.stats.core :refer [histogram]]
            [kixi.stats.distribution :as dist]))

(defn how-long?
  "How long will it take to complete a number of items?

  `:throughput.distribution/portion` is useful if you have
  a distribution for a team, but can only put half the
  team on an effort."
  [{throughput-distribution :throughput/distribution
    iterations :monte-carlo/iterations
    work-item-distribution :work-items/distribution
    distribution-portion :throughput.distribution/portion
    :or {distribution-portion 1
         iterations 500}}]
  (let [items-completed #(-> (dist/draw throughput-distribution)
                             (* distribution-portion))
        simulate #(loop [time-units 0
                         work-items-remaining (dist/draw work-item-distribution)]
                    (let [completed (items-completed)
                          items-left (- work-items-remaining completed)]
                      (if (neg? items-left)
                        time-units
                        (recur (inc time-units) items-left))))]
    (->> (repeatedly simulate)
         (take iterations)
         (transduce identity histogram))))

(comment
  (-> {:monte-carlo/iterations 20
       :throughput/distribution (dist/uniform {:a 1 :b 2})
       :work-items/distribution (dist/uniform {:a 20 :b 30})}
      how-long?
      dist/summary))

(defn how-many?
  "How many work items can be finished in a time period?

  A throughput based model.

  Importantly, the time period must use the same unit for the
  :time/period and the :throughput/distribution."
  [{time-period :time/period
    throughput-distribution :throughput/distribution
    iterations :monte-carlo/iterations
    :or {iterations 500}}]
  (let [stories #(loop [work-items 0
                        time-inc 0]
                   (if (= time-inc time-period)
                     work-items (recur (+ work-items (dist/draw throughput-distribution))
                                       (inc time-inc))))]
    (->> (repeatedly stories)
         (take iterations)
         (transduce identity histogram))))

(comment
  (-> {:time/period 100
       :monte-carlo/iterations 10
       :throughput/distribution (dist/uniform {:a 0 :b 3})}
      (how-many?)
      dist/summary))
