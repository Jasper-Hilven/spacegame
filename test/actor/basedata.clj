(ns actor.basedata
  (:require [clojure.test :refer :all]
            [ship.basedata :refer :all]
            [actor.needs :as n]
            [actor.position :as p]
            [actor.use-object :as uo]))

(def ship-with-bunk-and-hallway-person-in-bunk (-> ship-with-bunk-and-hallway
                                                   (p/set-position-person-ship 0 {:x 0 :y 0})))
(def ship-with-bunk-and-long-hallway-person-in-bunk (-> ship-with-bunk-and-long-hallway
                                                   (p/set-position-person-ship 0 {:x 0 :y 0})))
(def ship-with-bunk-and-hallway-person-in-hallway
  (-> ship-with-bunk-and-hallway (p/set-position-person-ship 0 {:x 1 :y 0})))

(def ship-with-bunk-person-using-bunk
  (-> ship-with-bunk-and-hallway
      (uo/set-using-position-ship 0 {:x 0 :y 0})
      (n/set-person-need-ship 0 :rested 0.5)))
