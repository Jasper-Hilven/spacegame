(ns actor.core-test
  (:require [clojure.test :refer :all]
            [world.core :refer :all]))
(use 'actor.needs)
(use 'actor.use-object)
(use 'debux.core)
(use 'ship.basics)
(use 'ship.core)
(use 'actor.crew)
(deftest success
  (testing "set and get"
    (is (= true true))))
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

(let [person-finished-starting-using-object
      (update-person-begin-using-object
        (update-person-begin-using-object
          (set-using-state-counter (set-using-state {} :beginning) 0) 5.0 5.0)
        5.0 0.0
        )]
  (deftest person-begin-using-object
    (is (= 2.0
           (get-counter
             (update-person-begin-using-object {} 5.0 2.0))))
    (is (zero?
          (get-counter person-finished-starting-using-object)))
    (is (= :using (get-using-state person-finished-starting-using-object)))))


(def ship-with-bunk
  (-> (start-ship)
      (set-block-at-position {:x 0 :y 0} :basic-bunk)
      (update-person 0 #(assoc % :position {:x 0 :y 0}))
      (update-person 0 #(assoc % :using {:x 0 :y 0}))
      (update-person 0 #(set-person-need % :rested 0.5))))
(deftest person-continue-using-object
  (is (> (get-in (update-person-ship-using-object ship-with-bunk 0 {:x 0 :y 0} 1)
                 [:person :needs :rested])
         0.5)))

(-> ship-with-bunk
    (update-person-usage 0 0)
    (update-person-usage 0 20)
    (update-person-usage 0 0)
    (update-person-usage 0 60))

(deftest person-uses-object
  (is ()))


(defn run-all [] (run-tests))



(run-all)
