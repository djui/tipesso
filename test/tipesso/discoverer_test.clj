(ns tipesso.discoverer-test
  (:use clojure.test
        tipesso.discoverer))

(deftest github-origin
  (is (github-project "http://github.com/djui/tipesso"))
  (is (github-project "https://github.com/djui/tipesso"))
  (is (github-project "http://github.com/djui/tipesso.git"))
  (is (github-project "https://github.com/djui/tipesso.git"))
  (is (github-project "http://github.com/djui/tipesso/master"))
  (is (github-project "https://github.com/djui/tipesso/master"))
  (is (github-project "http://djui.github.io/tipesso"))
  (is (github-project "https://djui.github.io/tipesso"))
  (is (github-project "http://djui.github.com/tipesso"))
  (is (github-project "https://djui.github.com/tipesso"))
  (is (github-project "http://djui.github.io/tipesso/index.html"))
  (is (github-project "https://djui.github.io/tipesso/index.html")))

(deftest unknown-origin
  (is (not (github-project "http://google.com"))))

;; TODO failing test
#_(deftest handle-github-origin
  (is (= {:type :github
          :uri "https://github.com/djui/tipesso"
          :project {:name "tipesso"
                    :authors ["djui"]
                    ;; Make this pattern matching
                    :languages {:Clojure 1922 :JavaScript 256}}}
         (github-project "https://github.com/djui/tipesso/"))))

(deftest leiningen-dependencies-test_complex
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
        prj-obj {:assets (fn [proj filename]
                           {:content content})}]
    (is (= (leiningen-dependencies prj-obj)
           '[[org.clojure/clojure "1.5.1"]
             [compojure "1.1.5"]
             [hiccup "1.0.3"]
             [tentacles "0.2.5"]
             [org.clojure/data.json "0.2.2"]]))))

(deftest leiningen-dependencies-test_simple
  (let [content (pr-str '(defproject foo "0.1.0"
                           :dependencies [[org.clojure/clojure "1.5.1"]]))
        prj-obj {:assets (fn [proj filename]
                           {:content content})}]
    (is (= (leiningen-dependencies prj-obj)
           '[[org.clojure/clojure "1.5.1"]]))))
