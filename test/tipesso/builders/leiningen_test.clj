(ns tipesso.builders.leiningen-test
  (:use clojure.test
        tipesso.builders.leiningen))


(deftest dependencies-complex-test
  (let [content (pr-str '(defproject tipesso "0.1.0-SNAPSHOT"
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
                           :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}))
        prj-obj {:assets (fn [proj filename] content)}]
    (is (= (dependencies prj-obj)
           '[[org.clojure/clojure "1.5.1"]
             [compojure "1.1.5"]
             [hiccup "1.0.3"]
             [tentacles "0.2.5"]
             [org.clojure/data.json "0.2.2"]]))))

(deftest dependencies-simple-test
  (let [content (pr-str '(defproject foo "0.1.0"
                           :dependencies [[org.clojure/clojure "1.5.1"]]))
        prj-obj {:assets (fn [proj filename] content)}]
    (is (= (dependencies prj-obj) '[[org.clojure/clojure "1.5.1"]]))))

(deftest dependencies-trivial-test
  (let [content (pr-str '(defproject foo "0.1.0"))
        prj-obj {:assets (fn [proj filename] content)}]
    (is (= (dependencies prj-obj) nil))))
