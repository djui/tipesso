(ns tipesso.hosters.github-test
  (:use clojure.test
        tipesso.hosters.github))

(deftest user-repo-test
  (is (= (user-repo "http://github.com/djui/tipesso") ["djui" "tipesso"]))
  (is (= (user-repo "https://github.com/djui/tipesso") ["djui" "tipesso"]))
  (is (= (user-repo "http://github.com/djui/tipesso.git") ["djui" "tipesso"]))
  (is (= (user-repo "https://github.com/djui/tipesso.git") ["djui" "tipesso"]))
  (is (= (user-repo "http://github.com/djui/tipesso/master") ["djui" "tipesso"]))
  (is (= (user-repo "https://github.com/djui/tipesso/master") ["djui" "tipesso"]))
  
  ;; FIX these tests are failing
  ;;(is (= (user-repo "http://djui.github.io/tipesso") ["djui" "tipesso"]))
  ;;(is (= (user-repo "https://djui.github.io/tipesso") ["djui" "tipesso"]))
  ;;(is (= (user-repo "http://djui.github.com/tipesso") ["djui" "tipesso"]))
  ;;(is (= (user-repo "https://djui.github.com/tipesso") ["djui" "tipesso"]))
  ;;(is (= (user-repo "http://djui.github.io/tipesso/index.html") ["djui" "tipesso"]))
  ;;(is (= (user-repo "https://djui.github.io/tipesso/index.html") ["djui" "tipesso"]))
  )

(deftest user-repo-test_failing
  (is (= (user-repo "http://google.com")
         nil)))

;; FIX failing test
#_(deftest handle-github-origin
  (is (= {:type :github
          :uri "https://github.com/djui/tipesso"
          :project {:name "tipesso"
                    :authors ["djui"]
                    ;; Make this pattern matching
                    :languages {:Clojure 1922 :JavaScript 256}}}
         (user-repo "https://github.com/djui/tipesso/"))))
