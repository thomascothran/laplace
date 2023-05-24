(ns thomascothran.laplace.routes
  (:require [reitit.ring :as ring]))

(def tree
  [""
   ["/" (constantly {:status 308 :headers {"Location" "/ui"}})]
   ["/ui" {:middleware [[:hiccup/body]]}
    ["" (constantly {:hiccup/body [:p "Hi there you!"]})]]
   ["/assets/*" (ring/create-resource-handler)]
   ["/api/v1/*"
    {:get (constantly {:status 200 :body "coming soon"})}]])
