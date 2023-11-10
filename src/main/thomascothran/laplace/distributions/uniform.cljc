(ns thomascothran.laplace.distributions.uniform
  (:require [thomascothran.laplace.distribution.proto :as proto])
  (:import [java.time.temporal ChronoUnit]
           [java.time LocalDate]))

(defn sampler
  [^Number a ^Number b]
  (fn []
    (+ a
       (rand-int (- (inc b) a)))))

(comment
  (->> (repeatedly (sampler 5 10))
       (take 600)
       set))

(defrecord UniformDistribution
           [^Number a ^Number b]

  proto/Distribution
  (cdf [_ x]
    (cond (< x a) 0
          (< x b) 0
          :else (/ (- x a)
                   (- b a))))

  proto/Sampleable
  (sample [_] (repeatedly
               (sampler a b))))

(comment
  ())

