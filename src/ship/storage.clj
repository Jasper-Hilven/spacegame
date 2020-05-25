(ns ship.storage)

(def empty-block-storage-capacity {:max-fluid 0 :max-gas 0 :max-solid 0})
(defn add-storage-capacities [s1 s2]
  {:max-fluid (+ (:max-fluid s1) (:max-fluid s2))
   :max-gas   (+ (:max-gas s1) (:max-gas s2))
   :max-solid (+ (:max-solid s1) (:max-solid s2))})
(defn get-block-storage [block] (or (:storage block) empty-block-storage-capacity))
(defn calculate-ship-storage-capacity [structure]
  (reduce-kv #(add-storage-capacities (get-block-storage %3) %)
             empty-block-storage-capacity
             structure))
(defn get-ship-max-storage [ship] (calculate-ship-storage-capacity (:structure ship)))
(defn get-ship-storage [ship] (:storage ship))