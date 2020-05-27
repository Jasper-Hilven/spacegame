(ns ship.crafting)

(use 'world.resource)
(use 'ship.storage)
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

(defn ready-crafting [ship] (let [crafting-loaded (>= (get-in ship [:crafting-loaded :crafting] 0))
                                  any-crafting-power (> (get-amount-of-crafting-power ship) 0)]
                              (and any-crafting-power crafting-loaded)))
(defn update-crafting-loaded [ship dT]
  (let [new-crafting-power (min 0
                                     (+ (* dT (get-amount-of-crafting-power ship))
                                        (get-in ship [:crafting :crafting-loaded] 0)))]
    (assoc-in ship [:crafting :crafting-loaded] new-crafting-power)))

(defn get-crafting-list [ship] (get-in ship [:crafting :crafting-list] []))
(defn normalize-list [list] (filter #(> (:times %) 0 ) list))
(defn set-crafting-list [ship list] (assoc-in ship [:crafting :crafting-list] (normalize-list list)))
(defn append-to-crafting-list [ship conversion times]
  (set-crafting-list ship (conj (get-crafting-list ship) {:conversion conversion :times times})))
(defn pop-one [list] (let [next-list
                           (if (seq? list)
                             (let [first-element (list 0)
                                   times (:times first-element)
                                   rest-list (subvec list 1)]
                               (if (= 0 times)
                                 (pop-one rest-list)
                                 (if (= 1 times)
                                   rest-list
                                   (assoc list 0 (assoc first-element :times (dec times)))))) [])]
                       next-list))
(defn pop-one-from-crafting-list [ship] (set-crafting-list ship (pop-one (get-crafting-list ship))))
(defn clear-crafting-list [ship] (set-crafting-list ship []))
(defn peak-from-crafting-list [ship] (nth (normalize-list (get-crafting-list ship)) 0))

(defn get-resources-required-to-craft [product-resource-name]
  (get-in resources [product-resource-name, :material] {:invalid 1}))
(defn has-resources-to-craft [ship product-resource-name]
  (has-in-ship ship (get-resources-required-to-craft product-resource-name)))

(defn update-crafting-ship [ship dT]
  (let [updated-ship (update-crafting-loaded ship dT)]
    (if (ready-crafting updated-ship)
      (if-let [next-element-to-craft-name (peak-from-crafting-list updated-ship)]
        (if (has-resources-to-craft updated-ship next-element-to-craft-name)
          (-> updated-ship
              (pop-one-from-crafting-list)
              (take-from-ship (get-resources-required-to-craft next-element-to-craft-name))
              (try-add-to-storage {next-element-to-craft-name 1})))
        updated-ship)
      updated-ship)))

