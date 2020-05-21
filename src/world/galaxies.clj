(ns world.galaxies)
(def adjective ["Milky" "Blended" "Spheroidal" "Circinus" "Sculptor" "Ringed"
                "Tentacle"
                "Infinite"
                "Large" "Big" "Huge" "Gigantic"
                "Medium"
                "Tiny" "Small" "Mini"
                "Black" "Red" "Orange" "Yellow" "Green" "Purple" "Grey" "White"
                "Bright" "Dark"
                "Chaos"
                "Stephan's" "Darwin's" "Hawkings"])


(def first-parts ["andro" "gyno" "" "whirl", "mess" "eye" "tenta" "peta" "mono" "duo" "trito" "quatto" "penta" "hexa"])
(def second-parts ["meda", "pool" "wheel" "oyster" "crab" "flower" "sombrero" "buna" "bila" "bika" "brero"
                   "cat" "dogo"])
(def single-name ["sigar" "flute" "comet"])

(def suffices ["galaxy" "cloud" "object" "cosmos" "1" "2" "3" "12" "42"])

(defn random-coll [collection] (collection (rand-int (count collection))))
(defn random-boolean [] (> (rand-int 2) 0))
(defn random1InX [x] (= (rand-int x) 0))
(defn random-galaxy-name [] (let [mid-name (if (random1InX 4)
                                      (random-coll single-name)
                                      (str (random-coll first-parts)
                                           (random-coll second-parts)))
                           suffix (if (random-boolean)
                                    (str " " (random-coll suffices)) "")
                           adject (random-coll adjective)]
                       (str adject " " mid-name suffix)))
