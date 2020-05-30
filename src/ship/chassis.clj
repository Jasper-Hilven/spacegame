(ns ship.chassis)
(use 'debux.core)

(def core :core)
(defn get-level [block] (or (:chassis-level block) 0))

(defn get-chassis-requirements [level]
  {:range (+ 2 (* (+ level 1) level))
   :level (+ 1 level)})


(defn get-range [n] (->> n inc (range (- n)) vec))
(defn get-circled-positions [radius]
  (for [
        x (get-range radius)
        y (get-range radius)
        :when (<= (+ (Math/abs x) (Math/abs y)) radius)]
    {:x x :y y}))
(defn add-position [p0 p1] {:x (+ (:x p0) (:x p1)) :y (+ (:y p0) (:y p1))})
(defn get-circle-around [position radius] (filterv #(not= position %) (map #(add-position position %) (get-circled-positions radius))))
(defn is-valid-chassis-point [point chassis]
  (let [block (chassis point)]
    (if (or (nil? block) (:is-core block))
      {:valid true :reason :core}
      (let [requirements (get-chassis-requirements (get-level block))
            required-level (:level requirements)
            points-around (get-circle-around point (:range requirements))
            blocks-to-hang-on (filterv #(<= required-level (get-level (chassis %))) points-around)
            empty (empty? blocks-to-hang-on)
            ]
        (if empty
          {:valid false :reason :no-chassis :position point}
          {:valid true :hangs-on blocks-to-hang-on :position point})
        ))))

(defn has-valid-single-core [structure]
  (let [positions (keys structure)
        cores (filterv #(:is-core (structure %)) positions)
        nbCores (count cores)
        valid (= 1 nbCores)
        error (if (< 1 nbCores)
                {:too-many-cores cores}
                (if (> 1 nbCores) :no-cores nil))]
    {:valid valid :error error}))

(defn is-valid-chassis
  [structure] (let [positions (keys structure)
                    valid-calcs (map #(is-valid-chassis-point % structure) positions)
                    valids (filterv #(:valid %) valid-calcs)
                    invalids (filterv #(not (:valid %)) valid-calcs)
                    valid (empty? invalids)
                    ] {:valid valid :valids valids :invalids invalids}))

(defn is-valid-structure [structure]
  (let [valid-single-core (has-valid-single-core structure)
        valid-chassis (is-valid-chassis structure)]
    {:valid             (and (:valid valid-chassis) (:valid valid-single-core))
     :invalid-chassis   (:invalids valid-chassis)
     :single-core-error (:error valid-single-core)}
    ))



(get-circle-around {:x 0 :y 0} 2)
(ship.chassis/is-valid-chassis-point {:x 0 :y 0} {{:x 0 :y 0} {:is-core true}})
(ship.chassis/is-valid-chassis-point
  {:x 1 :y 1}
  {{:x 0 :y 0} {:is-core true :chassis-level 2},
   {:x 1 :y 1} {:chassis-level 2}})

(ship.chassis/is-valid-chassis-point
  {:x 1 :y 1}
  {{:x 0 :y 0} {:is-core true :chassis-level 3},
   {:x 1 :y 1} {:chassis-level 2}})

(ship.chassis/is-valid-structure
  {{:x 0 :y 0}  {:is-core true :chassis-level 3},
   {:x 1 :y 0}  {:is-core true :chassis-level 3}
   {:x 1 :y 10} {:chassis-level 2}})
