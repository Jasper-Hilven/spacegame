(ns ship.blocks)
(use 'debux.core)
(use 'ship.storage)
(use 'ship.mining)
(use 'world.resource)
(def resource-conversions
  [
   {:name "Create oxygen" :from {:CO2 20} :to {:02 20 :carbon 1}}
   {:name "Create food" :from {:water 1 :carbon 1} :to {:food 2}}
   {:name "Consume food" :from {:food 1 :O2 20} :to {:CO2 20 :water 1}}
   {:name "Create plastic" :from {:oil 1} :to {:plastic 2}}
   ])

(def resistances
  {:impact-resistance {:name "Impact resistance"}
   :laser-resistance  {:name "Laser resistance"}
   :heat-resistance   {:name "Heat resistance"}})

(def low-resistance
  {:impact-resistance 1
   :laser-resistance  1
   :heat-resistance   1})

(defn vec2d [sx sy f]
  (mapv (fn [x] (mapv (fn [y] (f x y)) (range sx))) (range sy)))

(def block-types
  (let [basic-engine-property {:electric             1
                               :power-conversion     (float 0.5)
                               :energy-input         1E13
                               :max-energy-input     4E13
                               :max-power-conversion (float 0.2)}
        basic-battery-property {:capacity 1E13}]
    {:basic-chassis {:name          "Basic Chassis"
                     :chassis-level 1
                     :material      {:plastic 100 :iron 100}
                     }
     :basic-core    {:name          "Basic core"
                     :is-core       true
                     :chassis-level 3
                     :material      {:electronics 200 :plastic 100 :titanium 100 :iron 100}
                     :battery       basic-battery-property
                     :engine        basic-engine-property
                     :storage       basic-core-storage
                     }
     :basic-motor   {:name     "Basic motor"
                     :engine   basic-engine-property
                     :material {:plastic 20 :iron 150 :electronics 10}
                     }
     :basic-battery {:name     "Basic battery"
                     :material {:iron 10 :plastic 20 :electronics 10 :lithium-ion 200}
                     :battery  basic-battery-property
                     }
     :basic-miner   {:name     "basic miner"
                     :material {:iron 300 :plastic 20 :electronics 10 :titanium 40}
                     :miner    {:resources-per-second 1}}
     :basic-storage {:name     "basic storage"
                     :material {:plastic 200 :electronics 5}
                     :storage  basic-storage-block-storage}}))
(defn get-block-mass [block]
  (if (nil? block)
    0
    (let [materials (:material block)] (reduce-kv #(+ % (* %3 (:weight (resources %2)))) 0 materials))))
(get-block-mass (block-types :basic-core))
(defn map-to-array2 [m]
  (let [kys (keys m)
        minx (reduce #(min % (:x %2)) 0 kys)
        miny (reduce #(min % (:y %2)) 0 kys)
        maxx (reduce #(max % (:x %2)) 0 kys)
        maxy (reduce #(max % (:y %2)) 0 kys)]
    (vec2d (- maxx minx -1) (- maxy miny -1) #(m {:x (- %2 minx) :y (- % miny)}))))

(defn array2-to-map [arr]
  (reduce-kv
    (fn [ry y yColl]
      (reduce-kv
        (fn [r x v]
          (if (nil? v) r (assoc r {:x x :y y} v))) ry yColl))
    {}

    arr))

(array2-to-map [[1 2 3]
                [1 2 3]])
(map-to-array2 {{:x 1 :y 2} "s" {:x 2 :y 3} "p"})