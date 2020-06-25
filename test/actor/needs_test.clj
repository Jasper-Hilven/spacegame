(ns actor.needs-test
  (:require [clojure.test :refer :all]
            [actor.needs :refer :all]))

(def updated-person (update-person-needs start-person-needs 500))
(def tired-person (update-person-needs updated-person 500))
(def very-tired-person (update-person-needs tired-person 500))

(deftest needs
  (is (= (get-walking-speed start-person-needs) 1.0))
  (is (> (get-walking-speed updated-person) 0.8))
  (is (< (get-walking-speed updated-person) 1.0))
  (is (> (get-walking-speed tired-person) 0.3))
  (is (< (get-walking-speed tired-person) 0.5))
  (is (>= (get-walking-speed very-tired-person) 0.1))
  (is (< (get-walking-speed very-tired-person) 0.2)))

