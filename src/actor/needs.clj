(ns actor.needs
  (:require [actor.crew :as c]))

(def all-stats [:eaten :rested :entertained :hygiene :health :toilet])
(def start-person-needs
  (reduce #(assoc % %2 1) {} all-stats))
(def starting-actor-with-needs {:needs start-person-needs})

(def real-seconds-in-game-day (* 60 24.0))
(def game-day-per-real-second (/ 1 real-seconds-in-game-day))

(defn update-person-needs [person-stats time]
  (let [
        decrease-until-zero (fn [v factor] (max 0 (- v (* game-day-per-real-second factor time))))
        decrease-full-by-day #(decrease-until-zero % 1)
        decrease-full-by-2day #(decrease-until-zero % 0.5)
        decrease-twice-a-day #(decrease-until-zero % 2)
        new-eaten (decrease-full-by-day (:eaten person-stats))
        new-rested (decrease-full-by-day (:rested person-stats))
        new-entertained (decrease-full-by-day (:entertained person-stats))
        new-hygiene (decrease-full-by-2day (:hygiene person-stats))
        new-toilet (decrease-twice-a-day (:toilet person-stats))
        hunger-damage (if (= 0 new-eaten) 1 0)
        new-health (decrease-until-zero (:health person-stats) hunger-damage)]
    {:eaten       new-eaten
     :rested      new-rested
     :entertained new-entertained
     :hygiene     new-hygiene
     :toilet      new-toilet
     :health      new-health
     }))

(defn get-stat [objectOrPerson stat] (get-in objectOrPerson [stat] 0))
(defn assoc-in-for-stat [statfunc] (reduce #(assoc % %2 (statfunc %2)) {} all-stats))

(defn add-stats [first second]
  (assoc-in-for-stat #(min 1 (+ (get-stat first %) (get-stat second %)))))

(defn set-person-need [person need value] (assoc-in person [:needs need] value))
(defn set-person-need-ship [ship person-id need value]
  (c/update-person ship person-id #(set-person-need % need value)))
(defn get-person-need [person need] (get-in person [:needs need]))
(defn get-person-need-ship [ship person-id need] (get-person-need (c/get-person ship person-id) need))

(defn update-person-stats-with-object [person-stats object-stats time]
  (let [increase (assoc-in-for-stat #(* (get-stat object-stats %) time game-day-per-real-second))]
    (add-stats increase person-stats)))
(defn update-person-with-object [person object-stats time]
  (assoc person :needs (update-person-stats-with-object (:needs person) object-stats time)))
(defn update-person-with-object-ship [ship person-id object-stats time]
  (c/update-person ship person-id #(update-person-with-object % object-stats time)))

(defn get-walking-speed [stats]
  (reduce #(let [bad-need (- 1 (%2 stats))]
             (max 0.1
                  (- % (* 0.3 bad-need bad-need bad-need))))
          1
          all-stats))
