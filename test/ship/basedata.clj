(ns ship.basedata
  (:require [clojure.test :refer :all]
            [ship.core :as sc]
            [ship.basics :as sb]))

(def ship-with-bunk-and-hallway (-> (sc/empty-ship)
                                    (sb/set-block-at-position {:x 0 :y 0} :basic-bunk)
                                    (sb/set-block-at-position {:x 1 :y 0} :basic-hallway)))
(def ship-with-bunk-and-long-hallway
  (-> ship-with-bunk-and-hallway
      (sb/set-block-at-position {:x 2 :y 0} :basic-hallway)
      (sb/set-block-at-position {:x 3 :y 0} :basic-hallway)
      ))