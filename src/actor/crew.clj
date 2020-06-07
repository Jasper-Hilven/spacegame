(ns actor.crew)
(use 'actor.needs)
(use 'ship.basics)
(use 'ship.energy)
(use 'ship.storage)
(use 'world.resource)
(def using-states [:beginning :using :ending])
(def start-crew [{
                  :name                "jos"
                  :needs               start-person-needs
                  :position            {:x 0 :y 0}
                  :using               {:x 0 :y 1}
                  :using-state         :beginning
                  :using-state-counter 0                    ;;using-state-counter
                  :id                  0}])

(defn update-person-usage [ship person dTime]
  (let [using-position (:using person)]
    (if
      (not using-position)
      {:ship ship :person person}
      (let [block (get-block-at-position ship using-position)
            interactive-block (:interactive block)
            using-state-person (:using-state person)]
        (case using-state-person
          nil {:ship ship :person (assoc person :using-state :beginning)}
          :beginning (let [counter (get-in person [:using-state-counter] 0)]
                       (cond
                         (>= counter (get-in interactive-block [:start-delay] 0))
                         {:ship ship :person (assoc (assoc person :using-state-counter 0) :using-state :using)}

                         :else
                         {:ship   ship
                          :person (assoc person :using-state-counter (+ dTime (:using-state-counter person)))}))
          :using (let [required-resources (mult-resources (get-in interactive-block [:resource-usage]) dTime)
                       required-energy (* dTime (get-in interactive-block [:energy-usage] 0))]
                   (if (and
                         (has-energy ship required-energy)
                         (has-in-ship ship required-resources))
                     {:ship   (-> ship (take-energy required-energy) (take-from-ship required-resources))
                      :person (update-person-with-object person (:interact-stats interactive-block) dTime)}
                     {:ship ship :person (assoc (assoc person :using-state :ending) :using-state-counter 0)}
                     ))
          :ending (comment "todo" (let [counter (:using-state-counter person)]))
          ))

      )))

(defn is-same-person [p0 p1] (= (:id p0) (:id p1)))