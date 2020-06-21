(ns actor.crew)
(use 'ship.basics)
(use 'ship.energy)
(use 'ship.storage)
(use 'world.resource)

(def start-person-basics {:id       0
                          :name     "jos"
                          :position {:x 0 :y 0}
                          })

(defn get-person [ship person-id] (get-in ship [:crew person-id]))
(defn update-person [ship person-id update-func]
  (assoc-in ship [:crew person-id]
            (update-func (get-in ship [:crew person-id]))))
(defn set-person [ship person-id person]
  (assoc-in ship [:crew person-id] person))
(defn is-same-person [p0 p1] (= (:id p0) (:id p1)))
