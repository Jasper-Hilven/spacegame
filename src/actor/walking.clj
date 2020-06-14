(ns actor.walking)
(use 'ship.basics)
(require '[clojure.data.priority-map :refer [priority-map]])

(defn- is-walkable-at-position [structure point]
  (get-in structure [point :walkable] false))

(defn- next-positions [point] (let [x (:x point)
                                    y (:y point)
                                    ]
                                [{:x x :y (inc y)}
                                 {:x x :y (dec y)}
                                 {:x (dec x) :y y}
                                 {:x (inc x) :y y}
                                 ]))
(defn- next-walkable-positions [structure-ship point]
  (filterv #(is-walkable-at-position structure-ship %) (next-positions point)))

(defn- get-walkable-froms [structure]
  (reduce-kv #(if (get %3 :walkable false) (assoc % %2 %2) %) {} structure))

(defn- get-walkable-from-froms [structure froms]
  (reduce-kv #(assoc % %2 (next-walkable-positions structure %3)) {} froms))

(defn- point-to-list [point] [(:x point) (:y point)])

(defn- list-to-point [list] {:x (get list 0) :y (get list 1)})

(defn- convert-single-key-value [k v]
  [(point-to-list k) (point-to-list v)])

(defn- single-element-convert [k value-list]
  (mapv (partial convert-single-key-value k) value-list))

(defn- structure-to-astar-data [structure]
  (let [froms (get-walkable-froms structure)
        from-tos (get-walkable-from-froms structure froms)
        all-paths-listed (reduce-kv #(into % (single-element-convert %2 %3)) [] from-tos)
        path-to-distance (reduce #(assoc % %2 1) {} all-paths-listed)
        ]
    path-to-distance)
  )

(defn- A*
  "Finds a path between start and goal inside the graph described by edges
   (a map of edge to distance); estimate is an heuristic for the actual
   distance. Accepts a named option: :monotonic (default to true).
   Returns the path if found or nil."
  [edges estimate start goal & {mono :monotonic :or {mono true}}]
  (let [f (memoize #(estimate % goal))                      ; unsure the memoization is worthy
        neighbours (reduce (fn [m [a b]] (assoc m a (conj (m a #{}) b)))
                           {} (keys edges))]
    (loop [q (priority-map start (f start)) preds {} shortest {start 0}
           done #{}]
      (when-let [[x hx] (peek q)]
        (if (= goal x)
          (reverse (take-while identity (iterate preds goal)))
          (let [dx (- hx (f x))
                bn (for [n (remove done (neighbours x))
                         :let [hn (+ dx (edges [x n]) (f n))
                               sn (shortest n Double/POSITIVE_INFINITY)]
                         :when (< hn sn)]
                     [n hn])]
            (recur (into (pop q) bn)
                   (into preds (for [[n] bn] [n x]))
                   (into shortest bn)
                   (if mono (conj done x) done))))))))


(defn- euclidian-distance [a b]                             ; multidimensional
  (Math/sqrt (reduce + (map #(let [c (- %1 %2)] (* c c)) a b))))

(defn- get-path-in-ship-recalculate [structure from to]
  (let [grid (structure-to-astar-data structure)
        astar-result (A* grid euclidian-distance (point-to-list from) (point-to-list to))]
    (if (nil? astar-result) nil (mapv list-to-point astar-result))))

(def get-path-in-ship (memoize get-path-in-ship-recalculate))



;; generate a grid graph whose outlying edges are one-way
(defn- grid [x y w h]
  (into {}
        (for [i (range w) j (range h)
              :let [x0 (+ x i) y0 (+ y j) x1 (inc x0) y1 (inc y0)]]
          {[[x0 y0] [x1 y0]] 1
           [[x1 y0] [x1 y1]] 1
           [[x1 y1] [x0 y1]] 1
           [[x0 y1] [x0 y0]] 1})))

(grid 0 0 4 4)
(keys (grid 1 1 2 2))
(def g (apply dissoc (grid 0 0 4 4) (keys (grid 1 1 2 2))))

(next-positions {:x 5 :y 5})

(def test-structure {{:x 0 :y 0} {:walkable true}
                     {:x 1 :y 0} {:walkable true}
                     {:x 2 :y 0} {:walkable true}
                     {:x 3 :y 0} {:walkable false}})

(def walkable-froms (get-walkable-froms test-structure))
(get-walkable-from-froms test-structure walkable-froms)
(convert-single-key-value {:x 1 :y 0} {:x 2 :y 0})

(single-element-convert
  {:x 1 :y 0}
  [{:x 0 :y 0} {:x 2 :y 0}])
(structure-to-astar-data test-structure)

(A* {[[0 0] [1 0]] 1, [[1 0] [0 0]] 1, [[1 0] [2 0]] 1, [[2 0] [1 0]] 1} euclidian-distance [0 0] [2 0])

(get-path-in-ship test-structure {:x 0 :y 0} {:x 2 :y 0})
