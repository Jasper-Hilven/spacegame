(ns ship.blocks)
(use 'debux.core)
(use 'world.resource)
(use 'ship.point)
(defn vec2d [sx sy f]
  (mapv (fn [x] (mapv (fn [y] (f x y)) (range sx))) (range sy)))

(defn get-block-mass [block]
  (if (nil? block)
    0
    (get-weight-resource (let [key (:key block)]
                           key))))

(defn is-core? [block]
  (get block :is-core false))
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

(defn get-position-core [structure]
  (reduce-kv #(if (is-core? %3) %2 %) nil structure))

(defn get-position-core-or-center [structure]
  (or (get-position-core structure) {:x 0 :y 0}))

(defn set-core-to-ship-center [structure]
  (let [core-position (get-position-core-or-center structure)
        updated-keys (reduce-kv #(assoc % (sub-point %2 core-position) %3) {} structure)]
    updated-keys))

(get-position-core {{:x 0 :y 0} {:is-core true} {:x 0 :y 1} {:is-core false}})

(set-core-to-ship-center {{:x 1 :y 1} {:is-core true} {:x 2 :y 2} {:something :else} })