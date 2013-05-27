(ns tipesso.discoverer-test
  (:use clojure.test
        tipesso.discoverer))

(deftest github-origin
  (is (= :github (origin-type "http://github.com/djui/tipesso")))
  (is (= :github (origin-type "https://github.com/djui/tipesso")))
  (is (= :github (origin-type "http://github.com/djui/tipesso.git")))
  (is (= :github (origin-type "https://github.com/djui/tipesso.git")))
  (is (= :github (origin-type "http://github.com/djui/tipesso/master")))
  (is (= :github (origin-type "https://github.com/djui/tipesso/master")))
  (is (= :github (origin-type "http://djui.github.io/tipesso")))
  (is (= :github (origin-type "https://djui.github.io/tipesso")))
  (is (= :github (origin-type "http://djui.github.com/tipesso")))
  (is (= :github (origin-type "https://djui.github.com/tipesso")))
  (is (= :github (origin-type "http://djui.github.io/tipesso/index.html")))
  (is (= :github (origin-type "https://djui.github.io/tipesso/index.html"))))

(deftest unknown-origin
  (is (= :UNKNOWN (origin-type "http://google.com"))))

(deftest handle-github-origin
  (is (= {:type :github
          :uri "https://github.com/djui/tipesso"
          :project {:name "tipesso"
                    :authors ["djui"]
                    ;; Make this pattern matching
                    :languages {:Clojure 1922 :JavaScript 256}}}
         (handle-origin "https://github.com/djui/tipesso/"))))
