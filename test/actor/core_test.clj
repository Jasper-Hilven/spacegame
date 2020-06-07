(ns actor.core-test
  (:require [clojure.test :refer :all]
            [world.core :refer :all]))
(use 'actor.needs)
(use 'debux.core)

(deftest success
  (testing "set and get"
    (is (= true true))))
(def updated-person  (update-person start-person-needs 500))
(def tired-person  (update-person updated-person 500))

(def very-tired-person  (update-person tired-person 500))
(deftest needs
  (is (= (get-walking-speed start-person-needs) 1.0))
  (is (> (get-walking-speed updated-person) 0.8))
  (is (< (get-walking-speed updated-person) 1.0))
  (is (> (get-walking-speed tired-person) 0.3))
  (is (< (get-walking-speed tired-person) 0.5))
  (is (>= (get-walking-speed very-tired-person) 0.1))
  (is (< (get-walking-speed very-tired-person) 0.2)))

(def object-stats-to-use {:toilet 100})
(get-stat object-stats-to-use :toilet)
(update-person-stats-with-object (update-person start-person-needs 1000) object-stats-to-use 14)

(defn run-all [] (run-tests))



(run-all)
