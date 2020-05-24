(ns ship.energy)

(defn get-capacity [block] (if-let [bat (:battery block)] (or (:capacity bat) 0) 0))
(defn get-initial-energy [structure] {:electric (reduce-kv #(+ % (get-capacity %3)) 0 structure)})
(defn change-ship-energy [game delta] (update-in game [:ship :energy :electric] #(+ % delta) ))


(get-initial-energy {:k {:battery {:capacity 1000}} :k2 {:battery {:capacity 2000}}})


