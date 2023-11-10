(ns thomascothran.laplace.distribution
  (:require [thomascothran.laplace.distribution.proto
             :refer [Distribution Sampleable]
             :as proto]))

(defn sample
  "Infinite sequence of samples from a distribution"
  [^Sampleable distribution]
  (proto/sample distribution))

(defn cdf
  [^Distribution distribution ^Integer x]
  (proto/cdf distribution x))

;; (defn- quantile-from-sample
;;   [^Sampleable distribution ^Integer n]
;;   (let [sample (->> (sample distribution)
;;                     (take 50)
;;                     (sort))
;;         low (first sample)
;;         high (last sample)]))

(defn quantile
  [^Distribution distribution ^Integer n]
  (if (isa? Sampleable distribution)
    (throw (ex-info "Unsupported" distribution))))

