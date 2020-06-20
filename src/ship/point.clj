(ns ship.point)

(defn add-point [p0 p1] {:x (+ (:x p0) (:x p1)) :y (+ (:y p0) (:y p1))})
(defn sub-point [p0 p1] {:x (- (:x p0) (:x p1)) :y (- (:y p0) (:y p1))})
(defn mult-point [p0 factor] {:x (* (:x p0) factor) :y (* (:y p0) factor)})

(defn get-1dg-distance-point [p0 p1] (+
                                       (Math/abs (- (:x p0) (:x p1)))
                                       (Math/abs (- (:y p0) (:y p1)))
                                       ))
(defn normalize-point-to-1d-direction [point] (cond (< (:x point) 0) {:x -1 :y 0}
                                                    (> (:x point) 0) {:x 1 :y 0}
                                                    (< (:y point) 0) {:x 0 :y -1}
                                                    (> (:y point) 0) {:x 0 :y 1}
                                                    :else {:x 0 :y 0}))
(normalize-point-to-1d-direction {:y 1 :x 0})
(defn get-1dg-direction [from to]
  (normalize-point-to-1d-direction (sub-point to from)))

(get-1dg-direction {:x 1 :y 0} {:x 5 :y 0})
(get-1dg-direction {:x 0 :y 0} {:x 0 :y 1}  )
(sub-point {:x 0 :y 1} {:x 0 :y 0} )
(defn get-range [n] (->> n inc (range (- n)) vec))

(defn get-circled-positions [radius]
  (for [
        x (get-range radius)
        y (get-range radius)
        :when (<= (+ (Math/abs x) (Math/abs y)) radius)]
    {:x x :y y}))
(defn get-circle-around [position radius]
  (filterv #(not= position %) (map #(add-point position %) (get-circled-positions radius))))


(normalize-point-to-1d-direction {:x -5 :y 0})
(mult-point {:x 4 :y 5} 5)
(get-1dg-distance-point {:x 3 :y 0} {:x 5 :y 0})
(get-1dg-distance-point {:x 3 :y 0} {:x 3 :y 0})
(get-1dg-distance-point {:x 6 :y 1} {:x 5 :y 0})
