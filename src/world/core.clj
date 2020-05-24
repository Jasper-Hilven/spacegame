(ns world.core)
(use '[world.planet :only [gen-planet gen-star]])
(use '[world.galaxies :only [random-galaxy-name]])
(use '[world.species :only [get-species]])
(use 'debux.core)

(defn gen-solar-system [index]
  (let [star (gen-star)
        n-planets (+ 5 (rand-int 10))
        planets (mapv #(gen-planet % n-planets star) (range n-planets))
        ]
    {:star star :planets planets}))
(defn generate-galaxy [index] (let [nb-solar-system (+ 4 (rand-int 20))
                                    systems (vec (map gen-solar-system (range nb-solar-system)))]
                                {:name    (random-galaxy-name)
                                 :systems systems}))
(defn get-world []
  (let [
        galaxies (vec (map generate-galaxy (range 10)))
        species (get-species 10)]
    {:galaxies galaxies
     :species species}))


