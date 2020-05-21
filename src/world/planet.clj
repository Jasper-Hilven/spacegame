(ns world.planet)

(require '[world.randutil :as randi])
(use '[clojure.string :only (join split)])

(def parts {:1 ["b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"]
            :2 ["a", "e", "o", "u"]
            :3 ["br", "cr", "dr", "fr", "gr", "pr", "str", "tr", "bl", "cl", "fl", "gl", "pl", "sl", "sc", "sk", "sm", "sn", "sp", "st", "sw", "ch", "sh", "th", "wh"]
            :4 ["ae", "ai", "ao", "au", "a", "ay", "ea", "ei", "eo", "eu", "e", "ey", "ua", "ue", "ui", "uo", "u", "uy", "ia", "ie", "iu", "io", "iy", "oa", "oe", "ou", "oi", "o", "oy"]
            :5 ["turn", "ter", "nus", "rus", "tania", "hiri", "hines", "gawa", "nides", "carro", "rilia", "stea", "lia", "lea", "ria", "nov", "phus", "mia", "nerth", "wei", "ruta", "tov", "zuno", "vis", "lara", "nia", "liv", "tera", "gantu", "yama", "tune", "ter", "nus", "cury", "bos", "pra", "thea", "nope", "tis", "clite"]
            :6 ["una", "ion", "iea", "iri", "illes", "ides", "agua", "olla", "inda", "eshan", "oria", "ilia", "erth", "arth", "orth", "oth", "illon", "ichi", "ov", "arvis", "ara", "ars", "yke", "yria", "onoe", "ippe", "osie", "one", "ore", "ade", "adus", "urn", "ypso", "ora", "iuq", "orix", "apus", "ion", "eon", "eron", "ao", "omia"]})

(def mtx [[:1, :2, :5],
          [:2, :3, :6],
          [:3, :4, :5],
          [:4, :3, :6],
          [:3, :4, :2, :5],
          [:2, :1, :3, :6],
          [:3, :4, :2, :5],
          [:4, :3, :1, :6],
          [:3, :4, :1, :4, :5],
          [:4, :1, :4, :3, :6]])

(defn random-planet-name []
  (let [mappedPartsLists (map #(parts %) (randi/random-coll mtx))
        mappedParts (map randi/random-coll mappedPartsLists)
        concatted (join "" mappedParts)]
    concatted))
(def min-star-size 20000000)
(def star-size-variation 100000000)

(defn gen-star [] (let [size (+ min-star-size (rand-int star-size-variation))]
                    {:name (random-planet-name)
                     :size size}))
(defn get-planet-class [] (randi/random-coll [:gas :metal :terrestial :sea]))

(defn gen-planet [index of star] {
                                  :name (random-planet-name)
                                  :size (+ 100000 (rand-int 10000000))
                                  :temperature (+ -220 (rand 100) (* 700 (float (/ (- of index 1)  of))))})
