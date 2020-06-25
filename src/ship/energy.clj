(ns ship.energy)
(use 'debux.core)
(defn get-capacity [block] (if-let [bat (:battery block)] (or (:capacity bat) 0) 0))

(defn get-initial-energy [structure] {:electric (reduce-kv #(+ % (get-capacity %3)) 0 structure)})
(defn set-ship-energy [ship v] (assoc-in ship [:energy :electric] v))
(defn set-ship-energy-in-game [game v] (assoc-in game [:ship :energy :electric] v))
(defn change-ship-energy-in-game [game delta] (update-in game [:ship :energy :electric] #(+ % delta)))
(defn get-energy [ship] (get-in ship [:ship :energy] 0))
(defn has-energy [ship energy] (>= (get-energy ship) energy))
(defn take-energy [ship v]  (set-ship-energy ship (- (get-energy ship) v)))

(get-initial-energy {:k {:battery {:capacity 1000}} :k2 {:battery {:capacity 2000}}})


