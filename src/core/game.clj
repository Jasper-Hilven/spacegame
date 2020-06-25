(ns core.game
  (:use world.core)
  (:use ship.core)
  (:use ship.storage)
  (:use world.position)
  (:use ship.basics)
  (:use ship.movement)
  (:use ship.mining)
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class)
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]))

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

(def game-player-mined (-> game
                           (set-player-ship-mining true)
                           (update-mining-player-ship 1000)))
(get-player-ship-storage game-player-mined)
(get-mass-ship (get-player-ship game))
(get-mass-ship (get-player-ship game-player-mined))