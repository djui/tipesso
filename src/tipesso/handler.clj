(ns tipesso.handler
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [tipesso.core :refer [tippables]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :as middleware]
            [ring.util.response :as response])
  (:gen-class))

(defroutes app-routes
  (GET "/" []
       (let [res (response/resource-response "index.html" {:root "public"})]
         (response/content-type res "text/html")))
  (GET "/project*" {{uri "name"} :query-params}
       (let [res (tippables uri)]
         {:body res}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))

(defn start [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (start 8080))
