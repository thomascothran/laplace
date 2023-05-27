(ns thomascothran.laplace.routes
  (:require [reitit.ring :as ring]
            [reitit.coercion :as coercion]
            [thomascothran.laplace.routes.ui :as ui]))

(defn tree
  []
  [""
   ["/" (constantly {:status 308 :headers {"Location" "/ui"}})]
   ["/ui" (ui/routes)]
   ["/assets/*" (ring/create-resource-handler)]
   ["/api/v1/*"
    {:get (constantly {:status 200 :body "coming soon"})}]])
