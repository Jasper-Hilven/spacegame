(ns ship.core)

(use '[ship.blocks :only [block-types, array2-to-map]])

(def start-ship-recipe
  (let [h :basic-chassis
        c :basic-core
        m :basic-motor
        b :basic-battery
        _ nil]
    [
     [_ _ _ _ _ _ _]
     [_ _ _ m _ _ _]
     [_ _ b h b _ _]
     [_ m b b b m _]
     [m m b c b m m]
     [_ m b b b m _]
     [_ _ b h b _ _]
     [_ _ _ m _ _ _]
     ]
    )
  )
(defn build-ship [recipe] (array2-to-map (mapv #(mapv block-types %) recipe)))
(def start-ship (build-ship start-ship-recipe))
