(ns core.game
  (:use world.core)
  (:use ship.core)
  (:use world.position)
  (:use ship.movement))

(use 'debux.core)

(defn create-new-game []
  {:world    (get-world)
   :position start-pos
   :mode     :playing                                       ;;:building vs :playing
   :ship     (start-ship) })

(def game (create-new-game))
(:ship game)
(def game-jumped-ship (-> game (try-move-ship next-planet)))
