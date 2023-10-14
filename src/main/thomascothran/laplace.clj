(ns thomascothran.laplace
  (:require [thomascothran.laplace.server :as server]
            [clojure.tools.logging :as log])
  (:gen-class))

(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error ex "Uncaught exception on" (.getName thread)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port 80]
    (server/start! {:server/port port})
    @(promise)))
