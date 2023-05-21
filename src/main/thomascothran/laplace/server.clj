(ns thomascothran.laplace.server
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [reitit.middleware :as middleware]))

(def routes'
  ["*" {:get (constantly {:status 200 :body "hi"})}])

(defonce ^:private server*
  (atom nil))

(defn start!
  "Dynamic router will hot reload the routes, but it will
  be slower, since the routing tree is compiled each time."
  [{port        :server/port
    dynamic     :router/dynamic
    routes      :router/routes
    mw-registry :middleware/registry
    server-atom :server/atom
    :or         {port        8282
                 mw-registry {}
                 server-atom server*
                 routes routes'}}]
  (when @server-atom
    (throw (Exception. "Server already running")))
  (assert (vector? routes))
  (let [handler
        (if dynamic
          #(ring/ring-handler (ring/router routes
                                           {::middleware/registry mw-registry})
                              (constantly {:status 404}))
          (constantly (ring/ring-handler (ring/router routes {::middleware/registry mw-registry})
                                         (constantly {:status 404}))))
        app (fn [req] ((handler) req))
        server (jetty/run-jetty app {:port port :join? false})]
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
