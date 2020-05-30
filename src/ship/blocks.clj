(ns ship.blocks)
(use 'debux.core)
(use 'ship.storage)
(use 'ship.mining)
(use 'world.resource)

(defn vec2d [sx sy f]
  (mapv (fn [x] (mapv (fn [y] (f x y)) (range sx))) (range sy)))

(defn get-block-mass [block]
  (if (nil? block)
    0
    (get-weight-resource (let [key (:key block)]
                           key))))
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
(defn get-block-type [block] (:key block))
(array2-to-map [[1 2 3]
                [1 2 3]])
(map-to-array2 {{:x 1 :y 2} "s" {:x 2 :y 3} "p"})