(ns thomascothran.laplace.server.middleware
  (:require [reitit.ring :as ring]
            [reitit.coercion.malli]
            [hiccup.page :as hp]
            #_[reitit.openapi :as openapi]
            [reitit.ring.malli]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
    ;       [reitit.ring.middleware.dev :as dev]
            [reitit.ring.middleware.dev :as dev]
            [ring.adapter.jetty :as jetty]
            [muuntaja.core :as m]
            [clojure.java.io :as io]
            [malli.util :as mu]
            [thomascothran.laplace.ui-components.template :as template]))

(defn -hiccup-body-mw
  "Handlers can return a hiccup/body, which will then be turned
  into html."
  [handler]
  (fn [req]
    (let [res (handler req)]
      (if-let [hiccup-body (:hiccup/body res)]
        (-> res
            (assoc :status 200
                   :body (-> hiccup-body template/base hp/html5))
            (assoc-in [:headers "Content-Type"] "text/html"))
        res))))

(def defaults
  [swagger/swagger-feature
   #_openapi/openapi-feature
   parameters/parameters-middleware
   muuntaja/format-negotiate-middleware
   muuntaja/format-response-middleware
   exception/exception-middleware
   muuntaja/format-request-middleware
   coercion/coerce-response-middleware
   coercion/coerce-request-middleware
   multipart/multipart-middleware])

(def registry
  {:hiccup/body -hiccup-body-mw})
