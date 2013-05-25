(ns tipesso.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.def :only (defhtml)]
            [hiccup.core :only (html)]
            [hiccup.page :only (html5 include-js include-css)]
            [hiccup.element :only (link-to)]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))


