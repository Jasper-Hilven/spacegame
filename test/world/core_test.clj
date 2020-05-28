(ns world.core-test
  (:require [clojure.test :refer :all]
            [world.core :refer :all]))

(use '[ship.blocks :only [array2-to-map]])
(use '[ship.chassis :only [is-valid-structure]])
(use '[ship.energy :only [get-initial-energy]])
(use '[ship.movement :only [get-mass-ship get-total-engine-power get-jump-energy-cost-info get-jump-info]])
(use 'debux.core)
(use 'world.position)
(use 'ship.storage)
(use 'ship.recipies)
(use 'world.resource)
(use 'ship.crafting)
(use 'ship.core)

(defn lol [a] (+ a 4))

(def order [:solar-system :galaxy :universe])
(def ship (start-ship))

(get-jump-info ship start-pos next-planet) :invalid

(get-mass-ship (try-add-to-storage ship {:oil 0}))
(get-ship-max-storage ship)
(get-mass-ship (try-add-to-storage ship {:basic-storage 1}))
(get-amount-of-crafting-power ship)

(deftest crafting-list
  (testing "set and get"
    (is (let [crafting-list (-> ship
                                (set-crafting-list [{:conversion :basic-chassis :times 1}])
                                (get-crafting-list)
                                )]
          crafting-list) [{:conversion :basic-chassis :times 1}])))

(defn build-crafting-scenario []
  (-> ship
      (set-crafting-list [{:conversion :basic-chassis :times 2}])
      (try-add-to-storage (get-in resources [:basic-chassis :material]))
      (update-crafting-ship 1)))


(deftest do-crafting
    (is (= (get-crafting-list (build-crafting-scenario)) [{:conversion :basic-chassis :times 1}]))
    (is (= (get-ship-storage (build-crafting-scenario)) {:basic-chassis 1}))
    (is (= (get-crafting-loaded (build-crafting-scenario)) -55.0)))

(defn run-all [] (run-tests))
(run-all)
