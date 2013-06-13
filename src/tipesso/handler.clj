(ns tipesso.handler
  (:use environ.core)
  (:use compojure.core
        [ring.adapter.jetty :only [run-jetty]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [tipesso.discoverer :as discoverer]
            [ring.util.response :as response])
  (:gen-class))

(defroutes app-routes
  (GET "/" []
       (let [res (response/resource-response "index.html" {:root "public"})]
         (response/content-type res "text/html")))
  (GET "/project*" {{link "name"} :query-params} (discoverer/discover link))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn start [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (:port env "8080"))]
    (start port)))
