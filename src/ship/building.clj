(ns ship.building)
(use 'ship.basics)
(use 'ship.storage)
(use 'ship.chassis)
(defn can-build-block [ship position block-name]
  (and (has-no-block-at-position ship position)
       (has-single-in-ship ship block-name)
       (:valid (is-valid-chassis (get-structure (set-block-at-position ship position block-name))))))

(defn can-build-block-info [ship position block-name]
  {:noblock        (has-no-block-at-position ship position)
   :single-in-ship (has-single-in-ship ship block-name)
   :is-valid-chassis (is-valid-chassis (get-structure (set-block-at-position ship position block-name)))})

(defn build-block [ship position block-name]
  (if (not (can-build-block ship position block-name))
    ship
    (-> ship
        (take-single-resource block-name)
        (set-block-at-position position block-name))))

(defn can-unbuild-block [ship position]
  (and (has-block-at-position ship position)
       (is-valid-structure (remove-block-at-position ship position))))

(defn unbuild-block [ship position]
  (if (not (can-unbuild-block ship position))
    ship
    (let [block-name (get-block-at-position ship position)]
      (-> ship
          (try-add-single-to-storage block-name)
          (remove-block-at-position position)))))

