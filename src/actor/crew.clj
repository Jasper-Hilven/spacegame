(ns actor.crew)
(use 'actor.needs)
(use 'ship.basics)
(use 'ship.energy)
(use 'ship.storage)
(use 'world.resource)

(defn start-crew [] {0 {
                        :name                "jos"
                        :needs               start-person-needs
                        :position            {:x 0 :y 0}
                        :using               nil
                        :using-state         nil
                        :using-state-counter 0              ;;using-state-counter
                        :id                  0}})
(defn get-person [ship person-id] (get-in ship [:crew person-id] ship))
(defn update-person [ship person-id update-func]
               (assoc-in ship [:crew person-id]
                         (update-func (get-in ship [:crew person-id]))))
(defn set-person [ship person-id person]
  (assoc-in ship [:crew person-id] person))
(defn is-same-person [p0 p1] (= (:id p0) (:id p1)))