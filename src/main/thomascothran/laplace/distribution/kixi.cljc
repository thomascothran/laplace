(ns thomascothran.laplace.distribution.kixi
  "Adapters for kixi"
  (:require [thomascothran.laplace.distribution.proto
             :as proto]
            [clojure.test.check.random :refer [make-random]]
            [kixi.stats.protocols :as kixi-proto]
            [kixi.stats.distribution :as kixi-dist]
            [hyperfiddle.rcf :refer [tests tap %]]))

(extend-protocol proto/Distribution
  Object
  (cdf [this x]
    (if (satisfies? kixi-proto/PQuantile this)
      (do (extend-protocol proto/Distribution
            (class this)
            (cdf [this x]
              (kixi-proto/cdf this x)))
          (proto/cdf this x))
      (assert false (str "Unhandled entity: " this)))))

(tests

 "Kixi distributions support proto/cdf"
 (-> (kixi-dist/uniform {:a 1 :b 10})
     (proto/cdf 5)
     boolean) := true)

(extend-protocol proto/Quantile
  Object
  (quantile [this x]
    (if (satisfies? kixi-proto/PQuantile this)
      (do (extend-protocol proto/Quantile
            (class this)
            (quantile [this x]
              (kixi-proto/quantile this x)))
          (proto/quantile this x))
      (assert false (str "Unhandled entity: " this)))))

(tests

 "Kixi distributions support quantile"
 (-> (kixi-dist/uniform {:a 1 :b 10})
     (proto/quantile 0.5)
     boolean) := true)

(extend-protocol proto/Sampleable
  Object
  (sample [this]
    (if (satisfies? kixi-proto/PRandomVariable this)
      (do (extend-protocol proto/Sampleable
            (class this)
            (sample [this]
              (let [rng (make-random)]
                (repeatedly #(kixi-proto/sample-1 this rng)))))))))

(tests

 "Kixi distributions support sampling"

 (-> (kixi-dist/uniform {:a 1 :b 10})
     (proto/sample)
     first
     boolean) := true)
