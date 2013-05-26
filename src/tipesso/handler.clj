(ns tipesso.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.def :only (defhtml)]
            [hiccup.core :only (html)]
            [hiccup.page :only (html5 include-js include-css)]
            [hiccup.element :only (link-to)]
            [tipesso.discoverer :as discoverer]
            [ring.util.response :as response]))

(defroutes app-routes
  (GET "/" [] (response/resource-response "index.html" {:root "public"}))
  (GET "/project*" {{link "name"} :query-params} (discoverer/discover link))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
