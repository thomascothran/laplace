(ns thomascothran.laplace.distribution.proto
  "Protocols needed to work with Laplace")

(defprotocol Sampleable
  :extend-via-metadata true
  (sample [this] "Generate a sequence of samples"))

(defprotocol Distribution
  :extend-via-metadata true
  (cdf [this ^Number x] "Evaluate the cumulative distribution function at x"))

(comment
  (type [1 2 3]))

(defprotocol Quantile
  :extend-via-metadata true
  (quantile [this n]))

