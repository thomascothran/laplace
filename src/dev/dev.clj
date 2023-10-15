(ns dev
  (:require [malli.dev :as mdev]
            [thomascothran.laplace]
            [thomascothran.laplace.server :as server]))

(defn start-notebooks!
  []
  ((requiring-resolve 'nextjournal.clerk/serve!)
   {:browse true, :watch-paths ["src/clerk"]}))

(comment
  (start-notebooks!))

(defn go!
  []
  (mdev/start!)
  (server/start! {:router/dynamic true}))

(comment
  (go!))

(defn stop!
  []
  (mdev/stop!)
  (server/stop!))

(comment
  (stop!))
