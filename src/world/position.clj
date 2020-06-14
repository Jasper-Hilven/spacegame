(ns world.position)


(def start-pos {:galaxy 0
                :system 0
                :planet 0})
(def next-planet {:galaxy 0
                  :system 0
                  :planet 1})
(def next-system {:galaxy 0
                  :system 1
                  :planet 0})
(def next-galaxy {:galaxy 1
                  :system 0
                  :planet 0})
(defn diff-position [p0 p1]
  (let [distanceF #(Math/abs ^int (- (% p0) (% p1)))
        planet-diff (distanceF :planet)
        system-diff (distanceF :system)
        galaxy-diff (distanceF :galaxy)]
    {:planet planet-diff
     :system system-diff
     :galaxy galaxy-diff})

  )
(defn describe-position [world position]
  (let [
        galaxy ((:galaxies world) (:galaxy position))
        system ((galaxy :systems) (:system position))
        planet ((system :planets) (:planet position))]
    {:world  world
     :galaxy galaxy
     :system system
     :planet planet}))

