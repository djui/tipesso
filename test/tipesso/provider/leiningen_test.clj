(ns tipesso.provider.leiningen-test
  (:use clojure.test
        tipesso.provider.leiningen))

(defn- fixture [project-file-content]
  {:language :Clojure
   :asset (constantly (pr-str project-file-content))})

(deftest handler-complex-test
  (let [data (fixture '(defproject tipesso "0.1.0-SNAPSHOT"
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
                         :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}))]
    (is (= (handler data)
           '[{:provider :clojars, :id org.clojure/clojure, :version "1.5.1"}
             {:provider :clojars, :id compojure, :version "1.1.5"}
             {:provider :clojars, :id hiccup, :version "1.0.3"}
             {:provider :clojars, :id tentacles, :version "0.2.5"}
             {:provider :clojars, :id org.clojure/data.json, :version "0.2.2"}]))))

(deftest handler-simple-test
  (let [data (fixture '(defproject foo "0.1.0"
                         :dependencies [[org.clojure/clojure "1.5.1"]]))]
    (is (= (handler data) '[{:provider :clojars
                             :id org.clojure/clojure
                             :version "1.5.1"}]))))

(deftest handler-trivial-test
  (let [data (fixture '(defproject foo "0.1.0"))]
    (is (= (handler data) nil))))
