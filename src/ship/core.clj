(ns ship.core)

(use '[ship.blocks :only [array2-to-map]])
(use '[ship.chassis :only [is-valid-structure]])
(use '[ship.energy :only [get-initial-energy]])
(use '[ship.movement :only [get-mass-ship get-total-engine-power get-jump-energy-cost-info get-jump-info]])
(use 'debux.core)
(use 'world.position)
(use 'ship.storage)
(use 'ship.basics)
(use 'ship.recipies)
(use 'world.resource)
(use 'ship.crafting)
(use 'actor.crew)
(use 'actor.needs)
(defn build-ship [recipe position]
  (let [structure (array2-to-map (mapv #(mapv resources %) recipe))]
    {:structure structure
     :energy    (get-initial-energy structure)
     :position  position
     :crafting  crafting-start
     :crew      (start-crew start-person-needs)}))

(defn start-ship [] (build-ship transport-ship-recipe start-pos))
(is-valid-structure (get-structure (start-ship)))

