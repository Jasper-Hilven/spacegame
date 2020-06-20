(ns actor.walking)
(use 'ship.basics)
(use 'ship.point)
(use 'actor.pathcalculation)
(use 'ship.basics)
(use 'actor.crew)
(use 'actor.position)
(def start-person-with-walking {:walking '()})
(def example-person-with-walking-path {:walking '({:x 0 :y 1} {:x 0 :y 2})})

(defn get-walking-path [ship person-id]
  (:walking (get-person ship person-id)))
(defn is-not-walking-somewhere? [ship person-id]
  (empty? (get-walking-path ship person-id)))
(defn is-walking-somewhere? [ship person-id]
  (not (is-not-walking-somewhere? ship person-id)))
(defn get-walking-towards-first-tile [ship person-id]
  (first (get-walking-path ship person-id)))
(defn get-walking-towards-result-tile [ship person-id]
  (last (get-walking-path ship person-id)))


(defn set-person-walking-ship [ship person-id walking-path]
  (update-person ship person-id #(assoc % :walking walking-path)))
(defn drop-walking-towards-first-tile [ship person-id]
  (set-person-walking-ship ship person-id
                           (rest (get-walking-path ship person-id))))

(drop-walking-towards-first-tile {:crew {0 {:walking '({:x 0 :y 0} {:x 0 :y 1})}}} 0)
(defn set-person-walking-to [ship person-id towards-position]
  (set-person-walking-ship
    ship person-id
    (get-path-in-ship
      (get-structure ship)
      (get-position (get-person ship person-id)) towards-position)))

(defn update-person-walking [ship person-id movement]
  (let [position (get-position-person-ship ship person-id)
        towards (get-walking-towards-first-tile ship person-id)]
    (cond
      (is-not-walking-somewhere? ship person-id)
      {:ship ship :done-walking true}

      (nil? position) {:ship ship :done-walking true :error "no position"}
      (= position towards)
      (update-person-walking (drop-walking-towards-first-tile ship person-id) person-id movement)

      (<=  movement 0)
      {:ship ship :done-walking false}

      :else
      (let [moved (move-in-1dg-direction position towards movement)
            new-position (:new-position moved)
            movement-left (:movement-left moved)
            ship-person-new-position (set-position-person-ship ship person-id new-position)]
        (update-person-walking ship-person-new-position person-id movement-left))
      )))

(def ship-with-person {:crew {0 {:position {:x 0 :y 0}}}})
(def ship-with-person-walking-to-position {:crew {0 {:position {:x 0 :y 0} :walking '({:x 0 :y 0})}}})
(def ship-with-person-walking-to-next-position {:crew {0 {:position {:x 0 :y 0} :walking '({:x 0 :y 1})}}})
(def ship-with-person-walking-to-away-position {:crew {0 {:position {:x 0 :y 0} :walking '({:x 0 :y 0} {:x 0 :y 1} {:x 0 :y 2} {:x 0 :y 3})}}})
(update-person-walking ship-with-person 0 1)

(update-person-walking ship-with-person-walking-to-position 0 1)
(update-person-walking ship-with-person-walking-to-next-position 0 1)
(update-person-walking ship-with-person-walking-to-next-position 0 0.1)
(update-person-walking ship-with-person-walking-to-away-position 0 2.1)









