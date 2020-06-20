(ns world.resource)
(def gas-weight 0.05)
(use 'debux.core)



(def resources
  (let [basic-engine-property {:electric             1
                               :power-conversion     (float 0.5)
                               :energy-input         1E13
                               :max-energy-input     4E13
                               :max-power-conversion (float 0.2)}
        basic-battery-property {:capacity 1E13}
        basic-core-storage {:liquid 50 :gas 50 :solid 100 :block 1}
        basic-storage-block-storage {:liquid 200 :gas 200 :solid 300 :block 2}

        unkeyed {
                 :food                {:name "Food" :weight 1 :form :solid}
                 :O2                  {:name "Oxygen" :weight gas-weight :form :gas}
                 :CO2                 {:name "CO2" :weight gas-weight :form :gas}
                 :carbon              {:name "Carbon" :weight 1 :form :solid}
                 :water               {:name "Water" :weight 1 :form :liquid}
                 :oil                 {:name "Oil" :weight 1 :form :liquid}
                 :plastic             {:name "Plastic" :weight 0.5 :form :solid}
                 :iron                {:name "Steel" :weight 5 :form :solid}
                 :titanium            {:name "Titanium" :weight 2 :form :solid}
                 :electronics         {:name "Electronics" :weight 2 :form :solid}
                 :lithium-ion         {:name "Lithium Ion" :weight 3 :form :solid}

                 :basic-chassis       {:name          "Basic Chassis"
                                       :chassis-level 2
                                       :material      {:plastic 100 :iron 100}
                                       :form          :block
                                       :walkable      true
                                       :walk-speed    1
                                       }
                 :basic-core          {:name          "Basic core"
                                       :is-core       true
                                       :chassis-level 3
                                       :material      {:electronics 200 :plastic 100 :titanium 100 :iron 100}
                                       :battery       basic-battery-property
                                       :engine        basic-engine-property
                                       :storage       basic-core-storage
                                       :form          :block
                                       :crafting      {:power 0.1}
                                       }
                 :basic-motor         {:name     "Basic motor"
                                       :engine   basic-engine-property
                                       :material {:plastic 20 :iron 150 :electronics 10}
                                       :form     :block
                                       }
                 :basic-battery       {:name     "Basic battery"
                                       :material {:iron 10 :plastic 20 :electronics 10 :lithium-ion 200}
                                       :battery  basic-battery-property
                                       :form     :block
                                       }
                 :basic-miner         {:name     "basic miner"
                                       :material {:iron 300 :plastic 20 :electronics 10 :titanium 40}
                                       :miner    {:resources-per-second 1}
                                       :form     :block}
                 :basic-storage       {:name     "basic storage"
                                       :material {:plastic 200 :electronics 5}
                                       :storage  basic-storage-block-storage
                                       :form     :block
                                       }
                 :basic-crafter       {:name     "basic crafter"
                                       :material {:plastic 100 :electronics 50 :titanium 5}
                                       :form     :block
                                       :crafting {:power 1}}
                 :basic-bunk          {:name        "bunk"
                                       :material    {:plastic 20 :electronics 2 :iron 20}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.5
                                       :interactive {:location       :on
                                                     :interact-stats {:rested 20}
                                                     :start-delay    20
                                                     :end-delay      10
                                                     :stop-when-full :rested}}
                 :basic-toilet        {:name        "toilet"
                                       :material    {:plastic 40 :iron 10}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.4
                                       :interactive {:location       :on
                                                     :interact-stats {:toilet 100 :hygiene -10}
                                                     :resource-usage {:water 1}
                                                     :start-delay    10
                                                     :end-delay      5
                                                     :stop-when-full :toilet}}
                 :basic-shower        {:name        "shower"
                                       :material    {:plastic 20 :iron 20}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.5
                                       :interactive {:location       :on
                                                     :interact-stats {:hygiene 50}
                                                     :resource-usage {:water 5}
                                                     :start-delay    20
                                                     :end-delay      10
                                                     :stop-when-full :hygiene}}
                 :basic-eating-stand  {:name        "eating stand"
                                       :material    {:plastic 20 :iron 20}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.5
                                       :interactive {:location       :on
                                                     :interact-stats {:eaten 10}
                                                     :resource-usage {:food 1}
                                                     :start-delay    10
                                                     :end-delay      5
                                                     :stop-when-full :eaten}}
                 :basic-game-computer {:name        "game computer"
                                       :material    {:plastic 10 :electronics 50 :iron 20}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.5
                                       :interactive {:location       :on
                                                     :interact-stats {:entertained 10}
                                                     :energy-usage   1
                                                     :start-delay    10
                                                     :end-delay      5
                                                     :stop-when-full :entertained}}
                 :basic-hallway       {:name       "basic hallway"
                                       :material   {:plastic 10 :iron 10}
                                       :form       :block
                                       :walkable   true
                                       :walk-speed 1}
                 :basic-firmery       {:name        "basic firmery"
                                       :material    {:plastic 10 :electronics 100 :iron 50}
                                       :form        :block
                                       :walkable    true
                                       :walk-speed  0.5
                                       :interactive {:location       :on
                                                     :interact-stats {:health 1}
                                                     :energy-usage   10
                                                     :resource-usage {:O2 3}
                                                     :start-delay    10
                                                     :end-delay      10
                                                     :stop-when-full :health
                                                     }}}]
    (reduce-kv #(assoc % %2 (assoc %3 :key %2)) {} unkeyed)))



(defn get-resource [coll resource] (or (resource coll) 0))
(defn simplify-resources [container] (reduce-kv #(if (= 0 %3) % (assoc % %2 %3)) {} container))
(defn add-resources [add1 add2] (simplify-resources (reduce-kv #(assoc % %2 (+ (get-resource add1 %2) %3)) add1 add2)))
(defn mult-resources [r mult] (simplify-resources (reduce-kv #(assoc % %2 (* %3 mult)) r r)))
(defn resources-contains [container containee]
  (reduce-kv #(and % (>= (get-resource container %2) %3)) true containee))


(defn subtract-resources [container containee] (simplify-resources (reduce-kv #(assoc % %2 (- (get-resource container %2) %3)) container containee)))
(add-resources {:iron 1 :water 1} {:iron 1})
(subtract-resources {:iron 1 :water 1} {:iron 1})
(defn get-weight-resource [name]
  (let [
        dName name
        resource (dName resources)
        weight (:weight resource)
        material (:material resource)]
    (or weight
        (if material
          (reduce-kv #(+ % (* %3 (get-weight-resource %2))) 0 material)
          (throw (Exception. resource))))))
(defn get-weight-resources [resourcez] (reduce-kv #(+ % (* %3 (get-weight-resource %2))) 0 resourcez))
(get-weight-resources {:oil 1 :titanium 2 :basic-storage 1})
(defn get-form-amounts [resource-amounts]
  (reduce-kv
    #(update-in %
                [(:form (resources %2))]
                (partial + %3))
    {:solid 0 :liquid 0 :gas 0 :block 0}
    resource-amounts))

(get-form-amounts {:food 1 :O2 1 :carbon 2})
(def forms [:solid :liquid :gas :block])
(defn subtract-forms [from to-subtract] (reduce #(assoc % %2 (- (or (%2 from) 0) (or (%2 to-subtract) 0))) {} forms))
(defn forms-contains [container containee] (let [subtracted (subtract-forms container containee)
                                                 contains (reduce-kv #(and % (>= %3 0)) true subtracted)] contains))

(defn get-resource-by-name [name] (name resources))


(def subtract-test (subtract-forms {:solid 10} {:solid 5}))
