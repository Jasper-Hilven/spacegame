(ns ship.movement)
(use '[ship.blocks :only [get-block-mass]])
(use 'debux.core)

(defn jump-energy-cost [position next-position mass]
  (let
    [distanceF #(Math/abs (- (% position) (% next-position)))
     planet-diff (distanceF :planet)
     system-diff (distanceF :system)
     galaxy-diff (distanceF :galaxy)]
    (* mass (+ (* 400 galaxy-diff) (* 20 system-diff) (* 1 planet-diff)))
    ))

(defn get-mass-ship [ship]
  (dbg (let [blocks (:structure ship)]
         (reduce-kv (fn [r _ bl] (+ r (get-block-mass bl))) 0 blocks))))

