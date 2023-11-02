(ns thomascothran.laplace.server
  (:require [ring.adapter.jetty :as jetty]
            [clojure.tools.logging :as log]
            [reitit.ring :as ring]
            [reitit.middleware :as middleware]
            [reitit.dev.pretty :as pretty]
            [thomascothran.laplace.routes :as routes]
            [thomascothran.laplace.server.middleware :as mw]
            [reitit.coercion.malli]
            [reitit.coercion :as coercion]))

(def routes
  ["/" {:get (constantly {:status 200 :body "hi you there"})}])

(defonce ^:private server*
  (atom nil))

(defn- reitit-conf ;; fn ensures hot reloading works
  []
  {:exception            pretty/exception
   ;; :compile              coercion/compile-request-coercers
   :data               {:middleware mw/defaults
                        :coercion reitit.coercion.malli/coercion}
   ::middleware/registry mw/registry})

(defn start!
  "Dynamic router will hot reload the routes, but it will
  be slower, since the routing tree is compiled each time."
  [{port        :server/port
    dynamic     :router/dynamic
    server-atom :server/atom
    :or         {port        8282
                 server-atom server*}}]
  (when @server-atom
    (throw (Exception. "Server already running")))
  (let [handler
        (if dynamic
          #(ring/ring-handler (ring/router (routes/tree) (reitit-conf))
                              (constantly {:status 404}))
          (constantly (ring/ring-handler (ring/router (routes/tree) (reitit-conf))
                                         (constantly {:status 404}))))
        app (fn [req] ((handler) req))
        server (jetty/run-jetty app {:port port :join? false})]
    (log/infof "Server started on port %s" port)
    (reset! server-atom server)))

(defn stop!
  ([] (stop! {}))
  ([{server-atom :server/atom
     :or {server-atom server*}}]
   (assert @server-atom)
   (.stop @server-atom)
   (reset! server-atom nil)))

(comment
  (start! {:router/dynamic true})
  (stop!))
