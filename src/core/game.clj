(ns core.game
  (:use world.core)
  (:use ship.core)
  (:use world.position)
  (:use ship.basics)
  (:use ship.movement))

(use 'debux.core)

(defn create-new-game []
  {:world    (get-world)
   :mode     :playing                                       ;;:building vs :playing
   :ship     (start-ship)})

(def game (create-new-game))
(get-player-ship game)
(def game-jumped-ship (try-move-player-ship game next-planet))
(def game-jumped-ship2 (try-move-player-ship game-jumped-ship next-system))
(get-jump-info (get-player-ship game-jumped-ship) (get-position-player-ship game-jumped-ship) next-system)
