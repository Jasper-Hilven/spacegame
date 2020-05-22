(ns ship.core)

(use '[ship.blocks :only [block-types, array2-to-map]])
(use '[ship.chassis :only [is-valid-structure]])
(use '[ship.movement :only [get-mass-ship get-total-engine-power get-jump-energy-cost-info]])
(use 'debux.core)
(use 'world.position)

(def start-ship-recipe
  (let [h :basic-chassis
        c :basic-core
        m :basic-motor
        b :basic-battery
        _ nil]
    [
     [_ _ _ m _ _ _]
     [_ _ b h b _ _]
     [_ m b b b m _]
     [m h b c b h m]
     [_ m b b b m _]
     [_ _ b h b _ _]
     [_ m h m h m _]
     ]
    )
  )
(defn build-ship [recipe energy] {:structure (array2-to-map (mapv #(mapv block-types %) recipe))
                                  :energy    energy})
(defn start-ship [] (build-ship start-ship-recipe {:electric 1000}))
(is-valid-structure (:structure (start-ship)))

(get-jump-energy-cost-info (start-ship) start-pos next-planet)