(defproject tipesso "0.1.0-SNAPSHOT"
  :description "Discover who to tip"
  :url "http://djui.github.io/tipesso"
  :dependencies [[com.taoensso/timbre "2.3.0"]
                 [compojure "1.1.5"]
                 [environ "0.4.0"]
                 [hiccup "1.0.3"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure.contrib/djui "1.9"]
                 [org.clojure/data.json "0.2.2"]
                 [org.clojure/data.xml "0.0.7"]
                 [tentacles "0.2.6"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-json "0.2.0"]
                 [xenopath "0.1.1"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler tipesso.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :main tipesso.handler)
