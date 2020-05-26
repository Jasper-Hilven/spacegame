(ns ship.storage)
(use 'ship.basics)
(use 'world.resource)
(def basic-core-storage {:liquid 50 :gas 50 :solid 100})
(def basic-storage-block-storage {:liquid 200 :gas 200 :solid 300})
(def empty-block-storage-capacity {:liquid 0 :gas 0 :solid 0})
(defn add-storage-capacities [s1 s2]
  {:liquid (+ (:liquid s1) (:liquid s2))
   :gas    (+ (:gas s1) (:gas s2))
   :solid  (+ (:solid s1) (:solid s2))})
(defn get-block-storage [block] (or (:storage block) empty-block-storage-capacity))
(defn calculate-ship-storage-capacity [structure]
  (reduce-kv #(add-storage-capacities (get-block-storage %3) %)
             empty-block-storage-capacity
             structure))
(defn get-ship-max-storage [ship] (calculate-ship-storage-capacity (:structure ship)))
(defn get-ship-storage [ship] (or (:storage ship) {}))
(defn get-player-ship-storage [game] (get-ship-storage (get-player-ship game)) )
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
