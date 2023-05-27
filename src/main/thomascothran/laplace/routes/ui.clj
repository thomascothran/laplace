(ns thomascothran.laplace.routes.ui
  (:require [thomascothran.laplace.routes.ui.home :as home]))

(defn routes
  []
  ["" {:middleware [[:hiccup/page]]}
   (home/routes)])
