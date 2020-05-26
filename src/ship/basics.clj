(ns ship.basics)
(use 'world.position)
(defn get-player-ship [game] (:ship game))
(defn update-player-ship [game f] (assoc game :ship (f (:ship game))))
(defn update-position-player-ship [game next-position]
  (assoc-in game [:ship :position] next-position))
(defn get-position-player-ship [game]
  (get-in game [:ship :position]))

(defn get-player-planet [game]
  (let [position (get-position-player-ship game)
        planet (:planet (describe-position (:world game) position))]
    planet))
