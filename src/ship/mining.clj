(ns ship.mining)
(use 'ship.basics)
(use 'ship.storage)
(def basic-miner {:resources-per-second 1})

(defn map-function-on-map-vals [m f]
  (reduce (fn [altered-map [k v]] (assoc altered-map k (f v))) {} m))

(defn get-mine-capacity-player-ship [game] 1)

(defn is-ship-mining [ship] (or (:mining ship) false))


(defn set-player-ship-mining [game enable] (update-player-ship game #(assoc % :mining enable)))

(defn update-mining-player-ship [game dT]
  (update-player-ship
    game
    (fn [ship]
      (try-add-to-storage
        ship
        (if (is-ship-mining ship)
          (let [available-resources (:resources-to-find (get-player-planet game))]
            (map-function-on-map-vals available-resources #(* (get-mine-capacity-player-ship game) dT %)))
          {})))))