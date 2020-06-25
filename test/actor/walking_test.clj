(ns actor.walking-test
  (:require [clojure.test :refer :all]
            [actor.walking :as w]
            [actor.basedata :as bd]
            [actor.position :as p]
            ))


(def ship-with-person bd/ship-with-bunk-and-long-hallway-person-in-bunk)
(def ship-with-person-walking-to-position (w/set-person-walking-to ship-with-person 0 {:x 0 :y 0}))
(def ship-with-person-walking-to-next-position (w/set-person-walking-to ship-with-person 0 {:x 1 :y 0}))
(def ship-with-person-walking-to-away-position (w/set-person-walking-to ship-with-person 0 {:x 3 :y 0}))

(deftest update-person-walking
  (are [result position] (= (p/get-position-person-ship (:ship result) 0) position)
                         (w/update-person-walking ship-with-person 0 1) {:x 0 :y 0}
                         (w/update-person-walking ship-with-person-walking-to-position 0 1) {:x 0 :y 0}
                         (w/update-person-walking ship-with-person-walking-to-next-position 0 1) {:x 1 :y 0}
                         (w/update-person-walking ship-with-person-walking-to-next-position 0 0.1) {:x 0.1 :y 0.0}
                         (w/update-person-walking ship-with-person-walking-to-next-position 0 10) {:x 1 :y 0}
                         (w/update-person-walking ship-with-person-walking-to-away-position 0 2.1) {:x 2.1 :y 0.0}
                         ))
