(ns actor.position-test
  (:require [actor.position :as p]
            [clojure.test :refer :all]))

(deftest move-in-1dg-direction
  (are [x y] (= (:new-position x) y)
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 1 :y 0} 0.5) {:x 0.5 :y 0.0}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 1 :y 0} 2) {:x 1 :y 0}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 1.5 :y 0} 2) {:x 1.5 :y 0}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1} 0.5) {:x 0.0 :y 0.5}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1} 0.5) {:x 0.0 :y 0.5}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1} 2) {:x 0 :y 1}
             (p/move-in-1dg-direction {:x 0 :y 0} {:x 0 :y 1.5} 2) {:x 0 :y 1.5}))
(run-tests)