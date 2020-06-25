(ns actor.crew)

(def start-person-basics {:id       0
                          :name     "jos"
                          })

(defn get-person [ship person-id] (get-in ship [:crew person-id]))
(defn update-person [ship person-id update-func]
  (assoc-in ship [:crew person-id]
            (update-func (get-in ship [:crew person-id]))))
(defn set-person [ship person-id person]
  (assoc-in ship [:crew person-id] person))
(defn is-same-person [p0 p1] (= (:id p0) (:id p1)))
