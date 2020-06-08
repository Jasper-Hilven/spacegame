(ns ship.basics)
(use 'world.position)
(use 'world.resource)
(defn get-player-ship [game] (:ship game))
(defn update-player-ship [game f] (assoc game :ship (f (:ship game))))
(defn update-position-player-ship [game next-position]
  (assoc-in game [:ship :position] next-position))
(defn get-position-player-ship [game]
  (get-in game [:ship :position]))
(defn get-structure [ship] (:structure ship))
(defn set-structure [ship new-structure] (assoc ship :structure new-structure))
(defn get-block-at-position [ship position] (get-in (get-structure ship) [position]))
(defn has-block-at-position [ship position] (some? (get-block-at-position ship position)))
(defn has-no-block-at-position [ship position] (nil? (get-block-at-position ship position)))
(defn set-block-at-position [ship position block-name]
  (set-structure ship (assoc (get-structure ship) position (get-resource-by-name block-name))))
(defn remove-block-at-position [ship position] (set-structure ship (dissoc (get-structure ship) position)))

(defn get-player-planet [game]
  (let [position (get-position-player-ship game)
        planet (:planet (describe-position (:world game) position))]
    planet))
