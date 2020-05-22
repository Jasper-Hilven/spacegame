(ns core.game
  (:use world.core)
  (:use ship.core)
  (:use world.position))

(use 'debux.core)

(defn create-new-game []
  {:world    (get-world)
   :position start-pos
   :mode     :playing                                       ;;:building vs :playing
   :ship     start-ship})

(def test-game (create-new-game))
(describe-position
  (:world test-game)
  start-pos)
(create-new-game)
(defn set-to-building-mode
  [game] (assoc))