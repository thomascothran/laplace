(ns thomascothran.laplace.distribution.impl
  (:require [thomascothran.laplace.distribution.proto :as proto]
            [hyperfiddle.rcf :refer [tests]]))

(extend-protocol proto/Sampleable
  #?(:clj java.lang.Number
     :cljs js/Number)
  (sample [this]
    (repeat this))
  #?(:clj clojure.lang.PersistentVector
     :cljs PersistentVector)
  (sample [this]
    (repeatedly #(rand-nth this))))

(tests
 "Numbers support sampling"
 (take 3 (proto/sample 1)) := '(1 1 1)

 "Vectors support sampling"
 (def sampled-vector-nums
   (let [nums [1 2 3 5]]
     (->> (take 30 (proto/sample [1 2 3 5]))
          (filter (into #{} nums))
          (count))))
 sampled-vector-nums := 30)

(extend-protocol proto/Distribution
  #?(:clj java.lang.Number
     :cljs js/Number)
  (cdf [this x]
    (if (< x this) 0.0 1.0))

   ;; Essentially an empirical distribution
  #?(:clj clojure.lang.PersistentVector
     :cljs PersistentVector)
  (cdf [this x]
    (let [this' (sort this)
          below (take-while (partial >= x) this')]  ; Sum counts up to x
      (/ (count below)
         (count this')))))

(tests
 "CDF works on vectors"

 (proto/cdf [1 1 1 4] 4) := 1

 (proto/cdf [1 2 3 4 5 6] 3) := 1/2)

(defn- vec->quantile
  "Get the `n`th quantile from `xs`.
  
  For example, to get the tenth quantile:
  
  > (quantile xs 0.1)"
  [xs ^Number n]
  (let [sxs (sort xs)
        total (count xs)]
    (reduce (fn [prev-count curr]
              (cond (>= (/ prev-count total) n)
                    (reduced curr)

                    (= (inc prev-count) total)
                    nil

                    :else (inc prev-count)))
            0
            sxs)))

(tests
 "Vec->quantile"
 (vec->quantile [1 3 9 9] 0.25) := 3
 (vec->quantile [1 1] 0.9) := nil
 (vec->quantile [1] 0.0) := 1
 (-> (range 10)
     vec
     (sort)
     (vec->quantile 0.5)) := 5)

(extend-protocol proto/Quantile

  #?(:clj java.lang.Number
     :cljs js/Number)
  (quantile [_this n]
    (if (< n 1)
      0.0 n))

  #?(:clj clojure.lang.PersistentVector
     :cljs PersistentVector)
  (quantile [this n]
    (vec->quantile this n)))


