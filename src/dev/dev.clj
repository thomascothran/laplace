(ns dev
  (:require [malli.dev :as mdev]
            [thomascothran.laplace.server :as server]))

(defn go!
  []
  (mdev/start!)
  (server/start! {:router/dynamic true}))

(defn stop!
  []
  (mdev/stop!)
  (server/stop!))
