(ns actor.position
  (:require [actor.crew :as c]
            [ship.point :as p]))


(def start-person-position {:position {:x 0 :y 0}})

(defn get-position [person] (:position person))
(defn get-position-person-ship [ship person-id] (get-position (c/get-person ship person-id)))
(defn set-position [person position] (assoc person :position position))
(defn set-position-person-ship [ship person-id position] (c/update-person ship person-id #(set-position % position)))

(defn move-in-1dg-direction [from to max-movement]
  (let [distance (p/get-1dg-distance-point from to)
        direction-into (p/get-1dg-direction from to)
        distance-to-move (min distance max-movement)
        total-movement (p/mult-point direction-into distance-to-move)
        arrives (<= distance max-movement)
        new-position (if arrives to (p/add-point total-movement from))
        movement-left (- max-movement distance-to-move)]
    {:new-position  new-position
     :movement-left movement-left}))



