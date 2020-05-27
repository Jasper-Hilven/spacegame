(ns ship.core)

(use '[ship.blocks :only [ array2-to-map]])
(use '[ship.chassis :only [is-valid-structure]])
(use '[ship.energy :only [get-initial-energy]])
(use '[ship.movement :only [get-mass-ship get-total-engine-power get-jump-energy-cost-info get-jump-info]])
(use 'debux.core)
(use 'world.position)
(use 'ship.storage)
(use 'ship.recipies)
(use 'world.resource)
(use 'ship.crafting)
(defn build-ship [recipe position]
  (let [structure (array2-to-map (mapv #(mapv resources %) recipe))]
    {:structure structure
     :energy    (get-initial-energy structure)
     :position position
     :crafting crafting-start}))

(defn start-ship [] (build-ship start-ship-recipe start-pos))

(is-valid-structure (:structure (start-ship)))

(def ship (start-ship))

(get-jump-info ship start-pos next-planet)

(get-mass-ship (try-add-to-storage ship {:oil 0}))
(get-ship-max-storage ship)
(get-mass-ship (try-add-to-storage ship {:basic-storage 1}))
(get-amount-of-crafting-power ship)

(update-crafting-loaded ship 1)