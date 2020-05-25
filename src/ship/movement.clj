(ns ship.movement)
(use '[ship.blocks :only [get-block-mass]])
(use 'debux.core)
(use 'world.position)
(use 'ship.energy)
(use 'ship.basics)
(defn jump-energy-cost [position next-position mass]
  (let
    [diff (diff-position position next-position)]
    (* mass 60000000 (+ (* 400 (:galaxy diff)) (* 20 (:system diff)) (* 1 (:planet diff))))
    ))

(defn get-mass-ship [ship]
  (let [blocks (:structure ship)]
    (reduce-kv (fn [r _ bl] (+ r (get-block-mass bl))) 0 blocks)))
(defn get-position-player-ship [game]
  (:position (get-player-ship game)))
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
                        power-inefficient-output-block (* power-inefficient-range-block (:max-power-conversion block-data))
                        max-energy-output (+ (:max-energy-output %) power-inefficient-output-block energy-output-block)
                        max-energy-input (+ (:max-energy-input %) (:max-energy-input block-data))
                        ] {
                           :energy-output     energy-output
                           :energy-input      energy-input
                           :max-energy-output max-energy-output
                           :max-energy-input  max-energy-input
                           })
        basic-total-power-data (reduce-kv reductor initial-power-data (:structure ship))
        energy-output (:energy-output basic-total-power-data)
        energy-input (:energy-input basic-total-power-data)
        max-energy-output (:max-energy-output basic-total-power-data)
        max-energy-input (:max-energy-input basic-total-power-data)
        best-power-efficiency (/ energy-output (if (= 0 energy-input) 1 energy-input))
        efficiency-when-max-power (/ max-energy-output (if (= 0 max-energy-input) 1 max-energy-input))
        best-power-efficiency (if (= 0 best-power-efficiency) 1 best-power-efficiency)
        efficiency-when-max-power (if (= 0 efficiency-when-max-power) 1 efficiency-when-max-power)]
    (merge basic-total-power-data
           {:inefficient-energy-output (- max-energy-output energy-output)
            :inefficient-energy-input  (- max-energy-input energy-input)
            :best-power-efficiency     best-power-efficiency
            :efficiency-when-max-power efficiency-when-max-power
            })

    ))

(defn get-jump-energy-cost-info [ship position next-position]
  (let [mass (get-mass-ship ship)
        shop ship
        jump-cost (jump-energy-cost position next-position mass)
        engine-power-info (get-total-engine-power ship)
        energy-output (:energy-output engine-power-info)
        max-energy-output (:max-energy-output engine-power-info)
        enough-energy-output-jump (>= max-energy-output jump-cost)
        enough-energy-efficient-jump (>= energy-output jump-cost)
        efficient-energy-required-output (min jump-cost energy-output)
        best-power-efficiency (:best-power-efficiency engine-power-info)
        efficient-energy-required-input (/ efficient-energy-required-output best-power-efficiency)
        remaining-required-inefficient-energy-output (- jump-cost efficient-energy-required-output)
        remaining-inefficient-energy-can-output (:inefficient-energy-output engine-power-info)
        remaining-inefficient-energy-output (min remaining-inefficient-energy-can-output remaining-required-inefficient-energy-output)
        inefficient-output-factor (if (= 0 remaining-inefficient-energy-can-output)
                                    0
                                    (/ remaining-inefficient-energy-output remaining-inefficient-energy-can-output))
        inefficient-energy-input (* inefficient-output-factor (:inefficient-energy-input engine-power-info))
        energy-output-shortage-jump (if enough-energy-output-jump 0 (- jump-cost max-energy-output))
        energy-cost (+ efficient-energy-required-input inefficient-energy-input)]
    (merge {}
           {
            :engine-info                     engine-power-info
            :jump-required-output            jump-cost
            :enough-energy-output-jump       enough-energy-output-jump
            :enough-energy-efficient-jump    enough-energy-efficient-jump
            :energy-output-shortage-jump     energy-output-shortage-jump
            :energy-cost                     energy-cost
            :inefficient-energy-input        inefficient-energy-input
            :efficient-energy-required-input efficient-energy-required-input
            })
    ))
(defn get-jump-info [ship position next-position]
  (let [

        jump-energy-cost-info (get-jump-energy-cost-info ship position next-position)
        ship-energy (:electric (:energy ship))
        required-energy (:energy-cost jump-energy-cost-info)
        has-enough-energy (>= ship-energy required-energy)
        jumped (and has-enough-energy (:enough-energy-output-jump jump-energy-cost-info))
        new-energy (- ship-energy (if jumped required-energy 0))
        new-position (if jumped next-position position)]
    (merge {
            :required-energy   required-energy
            :ship-energy       ship-energy
            :has-enough-energy has-enough-energy
            :new-energy        new-energy
            :jumped            jumped
            :new-position      new-position}

           jump-energy-cost-info)
    ))
(defn update-position-ship [game next-position]
  (assoc-in game [:ship :position] next-position))

(defn try-move-player-ship [game next-position]
  (let [
        jump-info (get-jump-info (get-player-ship game) (get-position-player-ship game) next-position)
        ]

    (-> game
        (update-position-ship (:new-position jump-info))
        (set-ship-energy (:new-energy jump-info)))
    ))


