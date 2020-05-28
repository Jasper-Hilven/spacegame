(ns ship.crafting)

(use 'world.resource)
(use 'ship.storage)
(use 'debux.core)
(def resource-conversions
  [
   {:name "Create oxygen" :from {:CO2 20} :to {:02 20 :carbon 1}}
   {:name "Create food" :from {:water 1 :carbon 1} :to {:food 2}}
   {:name "Consume food" :from {:food 1 :O2 20} :to {:CO2 20 :water 1}}
   {:name "Create plastic" :from {:oil 1} :to {:plastic 2}}
   ])


(def crafting-start {:crafting-list [] :crafting-loaded 0})
(defn get-amount-of-crafting-power [ship]
  (reduce-kv #(+ % (get-in %3 [:crafting :power] 0)) 0 (:structure ship)))

(defn get-crafting-loaded [ship] (get-in ship [:crafting :crafting-loaded] -1))
(defn set-crafting-loaded [ship loaded] (assoc-in ship [:crafting :crafting-loaded] loaded))
(defn decrease-crafting-loaded [ship amount] (set-crafting-loaded ship (- (get-crafting-loaded ship) amount)))
(defn ready-crafting [ship] (let [crafting-loaded (>= (get-crafting-loaded ship))
                                  any-crafting-power (> (get-amount-of-crafting-power ship) 0)]
                              (and any-crafting-power crafting-loaded)))
(defn update-crafting-loaded [ship dT]
  (let [new-crafting-power (min 0
                                (+ (* dT (get-amount-of-crafting-power ship))
                                   (get-crafting-loaded ship)))]
    (assoc-in ship [:crafting :crafting-loaded] new-crafting-power)))

(defn get-crafting-list [ship] (get-in ship [:crafting :crafting-list] []))
(defn normalize-list [list] (if-not list [] (filterv #(> (:times %) 0) list)))
(defn set-crafting-list [ship list] (if (not (vector? list)) (throw (ex-info "No vector" {:not :ok})) (assoc-in ship [:crafting :crafting-list] (normalize-list list))))
(defn append-to-crafting-list [ship conversion times]
  (set-crafting-list ship (conj (get-crafting-list ship) {:conversion conversion :times times})))
(defn pop-one [list] (let [next-list
                           (if (not (empty? list))
                             (let [first-element (nth list 0)
                                   times (:times first-element)
                                   da-list list
                                   rest-list (subvec list 1)]
                               (if (= 0 times)
                                 (pop-one rest-list)
                                 (if (= 1 times)
                                   rest-list
                                   (assoc list 0 (assoc first-element :times (dec times))))))
                             [])]
                       next-list))

(pop-one [{:conversion :basic-chassis :times 2}])
(pop-one [{:conversion :basic-chassis :times 1}])
(pop-one [])

(defn pop-one-from-crafting-list [ship]
  (set-crafting-list ship (pop-one (get-crafting-list ship))))
(defn clear-crafting-list [ship] (set-crafting-list ship []))
(defn peak-from-crafting-list [ship] (get-in (nth (normalize-list (get-crafting-list ship)) 0) [:conversion] nil))

(defn get-resources-required-to-craft [product-resource-name]
  (get-in resources
          [product-resource-name :material]
          {:invalid "invalid"}))

(get-in resources
        [:basic-chassis :material] 0)

(defn has-resources-to-craft [ship product-resource-name]
  (has-in-ship
    ship
    (get-resources-required-to-craft product-resource-name)))
(defn decrease-crafting-loaded [ship block-crafted])
(defn update-crafting-ship [ship dT]
  (let [updated-ship (update-crafting-loaded ship dT)]
    (if (ready-crafting updated-ship)
      (if-let [next-element-to-craft-name (peak-from-crafting-list updated-ship)]
        (if (has-resources-to-craft updated-ship next-element-to-craft-name)
          (-> updated-ship
              (pop-one-from-crafting-list)
              (take-from-ship (get-resources-required-to-craft next-element-to-craft-name))
              (try-add-to-storage {next-element-to-craft-name 1})
              (decrease-crafting-loaded (get-weight-resource next-element-to-craft-name)))
          updated-ship)
        updated-ship)
      updated-ship)))

