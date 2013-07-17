(ns tipesso.provider.github-test
  (:use clojure.test
        tipesso.provider.github))

(defn- fixture [url]
  {:url url})

(deftest responsible?-test
  (is (= (#'tipesso.provider.github/responsible? (fixture "http://github.com/djui/tipesso")) ["djui" "tipesso"]))
  (is (= (#'tipesso.provider.github/responsible? (fixture "https://github.com/djui/tipesso")) ["djui" "tipesso"]))
  (is (= (#'tipesso.provider.github/responsible? (fixture "http://github.com/djui/tipesso.git")) ["djui" "tipesso"]))
  (is (= (#'tipesso.provider.github/responsible? (fixture "https://github.com/djui/tipesso.git")) ["djui" "tipesso"]))
  (is (= (#'tipesso.provider.github/responsible? (fixture "http://github.com/djui/tipesso/master")) ["djui" "tipesso"]))
  (is (= (#'tipesso.provider.github/responsible? (fixture "https://github.com/djui/tipesso/master")) ["djui" "tipesso"]))
  
  ;; FIX these tests are failing
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "http://djui.github.io/tipesso")) ["djui" "tipesso"]))
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "https://djui.github.io/tipesso")) ["djui" "tipesso"]))
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "http://djui.github.com/tipesso")) ["djui" "tipesso"]))
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "https://djui.github.com/tipesso")) ["djui" "tipesso"]))
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "http://djui.github.io/tipesso/index.html")) ["djui" "tipesso"]))
  ;;(is (= (#'tipesso.provider.github/responsible? (fixture "https://djui.github.io/tipesso/index.html")) ["djui" "tipesso"]))
  )

(deftest responsible?-test_failing
  (is (= (#'tipesso.provider.github/responsible? (fixture "http://google.com"))
         nil)))

;; FIX failing test
#_(deftest handle-github-origin
  (is (= {:type :github
          :uri "https://github.com/djui/tipesso"
          :project {:name "tipesso"
                    :authors ["djui"]
                    ;; Make this pattern matching
                    :languages {:Clojure 1922 :JavaScript 256}}}
         (#'tipesso.provider.github/responsible? (fixture "https://github.com/djui/tipesso/")))))
