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
(use 'ship.building)

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


(let [ship-with-block-storage (try-add-to-storage ship {:basic-storage 1})]
  (deftest do-building
    (is (= false (can-build-block ship-with-block-storage {:x 0 :y 0} :basic-storage)))
    (is (= false (can-build-block ship {:x 1 :y 1} :basic-storage)))
    (is (= true (can-build-block ship-with-block-storage {:x 1 :y 1} :basic-storage)))))

;;(defn run-all [] (run-tests))
;;(run-all)
