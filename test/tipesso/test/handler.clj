(ns tipesso.test.handler
  (:use clojure.java.io
        clojure.test
        ring.mock.request  
        tipesso.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) (as-file (resource "public/index.html"))))))
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
