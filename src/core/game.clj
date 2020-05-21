(ns core.game
  (:use world.core)
  (:use ship.core))
(use 'debux.core)

(def start-pos {:galaxy 0
                :system 0
                :planet 0})
(defn create-new-game []
  {:world    (get-world)
   :position start-pos
   :mode     :playing   ;;:building vs :playing
   :ship start-ship})

(defn describe-position [world position]
   (let [
             galaxy ((:galaxies world) (:galaxy position))
             system ((galaxy :systems) (:system position))
             planet ((system :planets) (:planet position))]
         {:world  world
          :galaxy galaxy
          :system system
          :planet planet}))
(def test-game (create-new-game))
(describe-position
  (:world test-game)
  start-pos)
(create-new-game)