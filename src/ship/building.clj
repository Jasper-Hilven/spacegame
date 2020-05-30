(ns ship.building)
(use 'ship.basics)
(use 'ship.storage)
(defn can-build-block [ship position block-name]
  (and (has-no-block-at-position ship position)
       (has-single-in-ship ship block-name)))

(defn build-block [ship position block-name]
  (if (not (can-build-block ship position block-name))
    ship
    (-> ship
        (take-single-resource block-name)
        (set-block-at-position position block-name))))

(defn can-unbuild-block [ship position]
  (and (has-block-at-position ship position)))

(defn unbuild-block [ship position]
  (if (not (can-unbuild-block ship position))
    ship
    (let [block-name (get-block-at-position ship position)]
      (-> ship
          (try-add-single-to-storage block-name)
          (remove-block-at-position position)))))
