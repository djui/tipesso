(ns tipesso.handler
  (:use environ.core
        compojure.core
        [ring.adapter.jetty :only [run-jetty]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [tipesso.core :as tipesso]
            [ring.util.response :as response])
  (:gen-class))

(defroutes app-routes
  (GET "/" []
       (let [res (response/resource-response "index.html" {:root "public"})]
         (response/content-type res "text/html")))
  (GET "/project*" {{uri "name"} :query-params} (tipesso/tippables uri))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn start [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (:port env "8080"))]
    (start port)))
