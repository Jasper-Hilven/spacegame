(ns actor.position)
(use 'actor.crew)
(use 'ship.point)
(use 'debux.core)
(def start-person-position {:position {:x 0 :y 0}})

(defn get-position [person] (:position person))
(defn get-position-person-ship [ship person-id] (get-position (get-person ship person-id)))
(defn set-position [person position] (assoc person :position position))
(defn set-position-person-ship [ship person-id position] (update-person ship person-id #(set-position % position)))

(defn move-in-1dg-direction [from to max-movement]
  (let [distance (get-1dg-distance-point from to)
        direction-into (get-1dg-direction from to)
        distance-to-move (min distance max-movement)
        total-movement (mult-point direction-into distance-to-move)
        arrives (<= distance max-movement)
        new-position (if arrives to (add-point total-movement from))
        movement-left (- max-movement distance-to-move)]
    {:new-position  new-position
     :movement-left movement-left}))


(move-in-1dg-direction {:x 0 :y 0} {:x 1 :y 0} 0.5)
(move-in-1dg-direction {:x 0 :y 0} {:x 1 :y 0} 2)
(move-in-1dg-direction {:x 0 :y 0} {:x 1.5 :y 0} 2)

(move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1} 0.5)
(move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1} 2)
(move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1.5} 2)

