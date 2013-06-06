(defproject tipesso "0.1.0-SNAPSHOT"
  :description "Discover who to tip"
  :url "http://djui.github.io/tipesso"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [tentacles "0.2.5"]
                 ;; Parsing and writing JSON
                 [org.clojure/data.json "0.2.2"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler tipesso.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})
