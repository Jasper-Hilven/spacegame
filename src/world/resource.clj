(ns world.resource)
(def gas-weight 0.05)



(def resources
  {
   :food        {:name "Food" :weight 1 :form :solid}
   :O2          {:name "Oxygen" :weight gas-weight :form :gas}
   :CO2         {:name "CO2" :weight gas-weight :form :gas}
   :carbon      {:name "Carbon" :weight 1 :form :solid}
   :water       {:name "Water" :weight 1 :form :liquid}
   :oil         {:name "Oil" :weight 1 :form :liquid}
   :plastic     {:name "Plastic" :weight 0.5 :form :solid}
   :iron        {:name "Steel" :weight 5 :form :solid}
   :titanium    {:name "Titanium" :weight 2 :form :solid}
   :electronics {:name "Electronics" :weight 2 :form :solid}
   :lithium-ion {:name "Lithium Ion" :weight 3 :form :solid}
   })
(defn get-resource [coll resource] (or (resource coll) 0))
(defn add-resources [add1 add2] (reduce-kv #(assoc % %2 (+ (get-resource add1 %2) %3)) add1 add2))
(add-resources {:iron 1 :water 1} {:iron 1})
(defn get-weight-resources [resourcez] (reduce-kv #(+ % (* (:weight (resources %2)) %3)) 0 resourcez))
(get-weight-resources {:oil 1 :titanium 2})
(defn get-form-amounts [resource-amounts]
  (reduce-kv
    #(update-in %
                [(:form (resources %2))]
                (partial + %3))
    {:solid 0 :liquid 0 :gas 0}
    resource-amounts))

(get-form-amounts {:food 1 :O2 1 :carbon 2})
(def forms [:solid :liquid :gas])
(defn subtract-forms [from to-subtract] (reduce #(assoc % %2 (- (or (%2 from) 0) (or (%2 to-subtract) 0))) {} forms))
(defn forms-contains [container containee] (let [subtracted (subtract-forms container containee)
                                                 contains (reduce-kv #(and % (>= %3 0)) true subtracted)] contains))




(def subtract-test (subtract-forms {:solid 10} {:solid 5}))
