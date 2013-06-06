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
  (not (is (github-project "http://google.com"))))

#_(deftest handle-github-origin
  (is (= {:type :github
          :uri "https://github.com/djui/tipesso"
          :project {:name "tipesso"
                    :authors ["djui"]
                    ;; Make this pattern matching
                    :languages {:Clojure 1922 :JavaScript 256}}}
         (github-project "https://github.com/djui/tipesso/"))))
