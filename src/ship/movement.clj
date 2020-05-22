(ns ship.movement)
(use '[ship.blocks :only [get-block-mass]])
(use 'debux.core)
(use 'world.position)
(defn jump-energy-cost [position next-position mass]
  (let
    [diff (diff-position position next-position)]
    (* mass 60000000 (+ (* 400 (:galaxy diff)) (* 20 (:system diff)) (* 1 (:planet diff))))
    ))

(defn get-mass-ship [ship]
  (let [blocks (:structure ship)]
    (reduce-kv (fn [r _ bl] (+ r (get-block-mass bl))) 0 blocks)))

(defn get-block-power-data [block]
  (if-let [engine (and block (:engine block))]
    engine
    {
     :electric             0
     :power-conversion     0
     :energy-input         0
     :max-energy-input     0
     :max-power-conversion 0}))

(defn get-total-engine-power [ship]
  (let [
        initial-power-data {
                            :energy-output     0
                            :energy-input      0
                            :max-energy-input  0
                            :max-energy-output 0}
        reductor #(let [block-data (get-block-power-data %3)
                        energy-output-block (* (:energy-input block-data) (:power-conversion block-data))
                        energy-output (+ (:energy-output %) energy-output-block)
                        energy-input (+ (:energy-input %) (:energy-input block-data))
                        power-inefficient-range-block (- (:max-energy-input block-data) (:energy-input block-data))
                        power-inefficient-output-block (dbg (* power-inefficient-range-block (:max-power-conversion block-data)))
                        max-energy-output (+ (:max-energy-output %) power-inefficient-output-block energy-output-block)
                        max-energy-input (+ (:max-energy-input %) (:max-energy-input block-data))
                        best-power-efficiency (/ energy-output (if (= 0 energy-input) 1 energy-input))
                        efficiency-when-max-power (/ max-energy-output (if (= 0 max-energy-input) 1 max-energy-input))
                        ] {
                           :energy-output             energy-output
                           :energy-input              energy-input
                           :max-energy-output         max-energy-output
                           :max-energy-input          max-energy-input
                           :best-power-efficiency     (if (= 0 best-power-efficiency) 1 best-power-efficiency)
                           :efficiency-when-max-power (if (= 0 efficiency-when-max-power) 1 efficiency-when-max-power)
                           })
        structure (:structure ship)
        total-power-data (reduce-kv reductor initial-power-data structure)
        ]
    total-power-data))

(defn get-jump-energy-cost-info [ship position next-position]
  (let [mass (get-mass-ship ship)
        jump-cost (jump-energy-cost position next-position mass)
        engine-power-info (get-total-engine-power ship)
        energy-output (:energy-output engine-power-info)
        max-energy-output (:max-energy-output engine-power-info)
        enough-energy-jump (>= max-energy-output jump-cost)
        enough-energy-efficient-jump (>= energy-output jump-cost)
        efficient-energy-output (min jump-cost energy-output)
        efficient-energy-input (/ efficient-energy-output (:best-power-efficiency engine-power-info))
        ] (merge {}
                 {
                  :engine-info engine-power-info
                  :jump-cost                    jump-cost
                  :enough-energy-jump           enough-energy-jump
                  :enough-energy-efficient-jump enough-energy-efficient-jump
                  :efficient-energy-input efficient-energy-input
                  })
          ))