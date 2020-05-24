(ns core.game
  (:use world.core)
  (:use ship.core)
  (:use world.position)
  (:use ship.movement))

(use 'debux.core)

(defn create-new-game []
  {:world    (get-world)
   :mode     :playing                                       ;;:building vs :playing
   :ship     (start-ship)})

(def game (create-new-game))
(:ship game)
(def game-jumped-ship (try-move-main-ship game next-planet))
(def game-jumped-ship2 (try-move-main-ship game-jumped-ship next-system))
(get-jump-info (:ship game-jumped-ship) (get-position-ship game-jumped-ship) next-system)