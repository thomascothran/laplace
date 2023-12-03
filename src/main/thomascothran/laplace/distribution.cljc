(ns thomascothran.laplace.distribution
  (:require [thomascothran.laplace.distribution.proto
             :as proto]))

(defn sample
  "Infinite sequence of samples from a distribution"
  [distribution]
  (proto/sample distribution))

(defn cdf
  [distribution ^Integer x]
  (proto/cdf distribution x))

(defn quantile
  [distribution ^Integer n]
  (proto/quantile distribution n))

