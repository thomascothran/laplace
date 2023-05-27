(ns thomascothran.laplace.server.middleware
  (:require [reitit.ring :as ring]
            [reitit.coercion.malli]
            [hiccup.page :as hp]
            [hiccup.core :as h]
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

(defn -hiccup-mw
  "Handlers can return a hiccup/body, which will then be turned
  into html."
  [handler]
  (fn [req]
    (let [res (handler req)
          hiccup-page (:hiccup/page res)
          hiccup-fragment (:hiccup/fragment res)
          html-body (cond hiccup-page (-> hiccup-page template/page hp/html5)
                          hiccup-fragment (-> hiccup-fragment h/html str))]
      (if html-body
        (-> (assoc res :status 200 :body html-body)
            (assoc-in [:headers "Content-Type"] "text/html"))
        res))))


(defn logger
  [handler]
  (fn [req]
    (clojure.pprint/pprint {:msg "Entering"
                            :req req})
    (try (let [res (handler req)]
           (clojure.pprint/pprint {:msg "Leaving"
                                   :res res})
           res)
         (catch Exception e
           (clojure.pprint/pprint {:msg "Error"
                                   :error e
                                   :error-data (ex-data e)})
           (throw e)))))

(def defaults
  [#_logger
   swagger/swagger-feature
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
  {:hiccup/page -hiccup-mw})
