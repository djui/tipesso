(ns tipesso.macros-test
  (:use clojure.test
        tipesso.macros))

(deftest test-create-map
  (testing "simple"
    (let [a 1 b 2 c 3]
      (is (= (create-map a b c)
             {:a 1 :b 2 :c 3})))))