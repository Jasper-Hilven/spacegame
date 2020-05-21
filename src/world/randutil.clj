(ns world.randutil)

(defn random-coll [collection] (collection (rand-int (count collection))))

(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn random-coll-diff [collection n]
  (if (< (count collection) n)
    (throw "invalid")
    (if (= 0 n)
      []
      (let [randi (rand-int (count collection))
            element (collection randi)
            remainder (vec-remove randi collection)
            ] (concat [element] (random-coll-diff remainder (dec n)))))))