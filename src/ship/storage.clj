(ns ship.storage)
(use 'ship.basics)
(use 'world.resource)
(def empty-block-storage-capacity (reduce #(assoc % %2 0) {} forms))
(defn add-storage-capacities [s1 s2]
  (reduce #(assoc % %2 (+ (%2 s1) (%2 s2))) {} forms))
(defn get-block-storage [block] (or (:storage block) empty-block-storage-capacity))
(defn calculate-ship-storage-capacity [structure]
  (reduce-kv #(add-storage-capacities (get-block-storage %3) %)
             empty-block-storage-capacity
             structure))
(defn get-ship-max-storage [ship] (calculate-ship-storage-capacity (:structure ship)))
(defn get-ship-storage [ship] (get-in ship [:storage] {}))
(defn get-player-ship-storage [game] (get-ship-storage (get-player-ship game)))
(defn get-storage-left [ship]
  (let [capacity (get-ship-max-storage ship)
        stored (get-form-amounts (get-ship-storage ship))
        ]
    (subtract-forms capacity stored)))

(defn try-add-to-storage [ship resources]
  (let [storage-left (get-storage-left ship)
        fits (forms-contains storage-left (get-form-amounts resources))
        ship-new-resources (if fits
                             (add-resources resources (get-ship-storage ship))
                             (get-ship-storage ship))]
    (assoc ship :storage ship-new-resources)))
(defn take-from-ship [ship resources]
  (assoc ship :storage (subtract-resources (get-ship-storage ship) resources)))
(defn has-in-ship [ship resources] (resources-contains (get-ship-storage ship) resources))