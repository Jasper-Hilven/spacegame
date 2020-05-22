(ns ship.blocks)
(use 'debux.core)


(def gas-weight 0.05)

(def materialtypes
  {
   :food        {:name "Food" :weight 1 :form :solid}
   :O2          {:name "Oxygen" :weight gas-weight :form :gas}
   :CO2         {:name "CO2" :weight gas-weight :form :gas}
   :carbon      {:name "Carbon" :weight 1 :form :solid}
   :water       {:name "Water" :weight 1 :form :liquid}
   :oil         {:name "Oil" :weight 1 :form :liquid}
   :plastic     {:name "Plastic" :weight 0.5 :form :solid}
   :steel       {:name "Steel" :weight 5 :form :solid}
   :titanium    {:name "Titanium" :weight 2 :form :solid}
   :electronics {:name "Electronics" :weight 2 :form :solid}
   :lithium-ion {:name "Lithium Ion" :weight 3 :form :solid}
   })

(def resource-conversions
  [
   {:name "Create oxygen" :from {:CO2 20} :to {:02 20 :carbon 1}}
   {:name "Create food" :from {:water 1 :carbon 1} :to {:food 2}}
   {:name "Consume food" :from {:food 1 :O2 20} :to {:CO2 20 :water 1}}
   {:name "Create plastic" :from {:oil 1} :to {:plastic 2}}
   ])

(def block-functions
  {:chassis       {:name "Chassis"}
   :core          {:name "Core"}
   :outer-shield  {:name "Outer shield"}
   :elec-engine   {:name "Electrical engine"}
   :battery       {:name "Battery"}
   :comb-engine   {:name "Combustion engine"}
   :gas-tank      {:name "Gas tank"}
   :liquid-tank   {:name "Liquid tank"}
   :solid-storage {:name "Solid storage"}
   :block-storage {:name "Block storage"}
   :laser         {:name "Laser"}})

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
  {:basic-chassis {:name          "Basic Chassis"
                   :chassis-level 1
                   :material      {:plastic 100 :steel 100}
                   :resistance    low-resistance}
   :basic-core    {:name          "Basic core"
                   :is-core       true
                   :chassis-level 3
                   :material      {:electronics 200 :plastic 100 :titanium 100 :steel 100}
                   :resistance    low-resistance}
   :basic-motor   {:name       "Basic motor"
                   :engine     {:electric             1
                                :power-conversion     (float 0.5)
                                :energy-input         1E12
                                :max-energy-input     4E12
                                :max-power-conversion (float 0.2)}
                   :material   {:plastic 20 :steel 150 :electronics 10}
                   :resistance low-resistance}
   :basic-battery {:name       "Basic battery"
                   :material   {:steel 10 :plastic 20 :electronics 10 :lithium-ion 200}
                   :resistance low-resistance}})
(defn get-block-mass [block]
  (if (nil? block)
    0
    (let [materials (:material block)] (reduce-kv #(+ % (* %3 (:weight (materialtypes %2)))) 0 materials))))
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