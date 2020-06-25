(ns actor.use-object
  (:require [actor.actionqueue :as aq]
            [actor.needs :as n]
            [ship.basics :as b]
            [ship.energy :as e]
            [ship.storage :as s]
            [world.resource :as r]
            [actor.crew :as c]
            ))

(def using-states [:beginning :using :ending])
(def start-actor-with-uses
  {:using               nil
   :using-state         nil
   :using-state-counter 0})

(defn get-counter [person]
  (get-in person [:using-state-counter] 0))

(defn set-using-state [person v] (assoc person :using-state v))
(defn set-using-state-ship [ship person-id v]
  (c/update-person ship person-id #(assoc % :using-state v)))

(defn get-using-state [person] (:using-state person))
(defn get-using-state-ship [ship person-id] (:using-state (c/get-person ship person-id)))

(defn set-using-state-counter [person v] (assoc person :using-state-counter v))
(defn set-using-state-counter-ship [ship person-id v]
  (c/update-person ship person-id #(assoc % :using-state-counter v)))

(defn increment-using-state-counter [person dTime]
  (set-using-state-counter person (+ dTime (get-counter person))))

(defn get-using-position [person] (:using person))

(defn set-using-position [person position] (assoc person :using position))

(defn set-using-position-ship [ship person-id position]
  (c/update-person ship person-id #(set-using-position % position)))

(defn get-using-position-ship [ship person-id]
  (get-using-position (c/get-person ship person-id)))

(defn get-using-block-ship [ship person-id]
  (let [position (get-using-position-ship ship person-id)]
    (and position (b/get-block-at-position ship position))))

(defn is-using-object? [ship person-id]
  (not (nil? (get-using-position-ship ship person-id))))

(defn get-interact-stats-block [block]
  (get-in block [:interactive :interact-stats] nil))

(defn get-stop-when-full [block]
  (get-in block [:interactive :stop-when-full] nil))

(defn update-person-begin-using-object [person start-delay dTime]
  (if
    (>= (get-counter person) start-delay)
    (-> person (set-using-state :using) (set-using-state-counter 0))
    (increment-using-state-counter person dTime)))

(defn update-person-end-using-object [person end-delay dTime]
  (if
    (>= (get-counter person) end-delay)
    (-> person
        (set-using-position nil)
        (set-using-state nil)
        (set-using-state-counter 0)
        (aq/drop-current-action))
    (increment-using-state-counter person dTime)))

(defn is-useful-to-do [ship person-id]
  (let [block (get-using-block-ship ship person-id)
        optimizing (and block (get-stop-when-full block))
        is-full (or (nil? optimizing) (>= (n/get-person-need-ship ship person-id optimizing) 1))]
    (not is-full)))

(defn update-person-ship-using-object [ship person-id using-position dTime]
  (let [block (b/get-block-at-position ship using-position)
        interactive-block (:interactive block)
        required-resources (r/mult-resources (get-in interactive-block [:resource-usage]) dTime)
        required-energy (* dTime (get-in interactive-block [:energy-usage] 0))]
    (if (and
          (e/has-energy ship required-energy)
          (s/has-in-ship ship required-resources)
          (is-useful-to-do ship person-id))

      (-> ship (e/take-energy required-energy) (s/take-from-ship required-resources)
          (n/update-person-with-object-ship person-id (:interact-stats interactive-block) dTime))

      (-> ship
          (set-using-state-counter-ship person-id 0)
          (set-using-state-ship person-id :ending)))))

(defn get-current-block-delay [ship person-id delay-type]
  (get-in
    (:interactive
      (b/get-block-at-position
        ship
        (get-using-position-ship ship person-id))) [delay-type] 0))

(defn get-current-block-start-delay [ship person-id]
  (get-current-block-delay ship person-id :start-delay))

(defn get-current-block-end-delay [ship person-id]
  (get-current-block-delay ship person-id :end-delay))

(defn update-person-usage-beginning [ship person-id dTime]
  (c/update-person
    ship person-id
    #(update-person-begin-using-object
       %
       (get-current-block-start-delay ship person-id)
       dTime)))

(defn update-person-usage-ending [ship person-id dTime]
  (c/update-person
    ship person-id
    #(update-person-end-using-object
       %
       (get-current-block-end-delay ship person-id)
       dTime)))

(defn update-person-usage-using [ship person-id dTime]
  (update-person-ship-using-object
    ship person-id
    (get-using-position-ship ship person-id)
    dTime))

(defn update-person-usage-no-state [ship person-id]
  (c/update-person
    ship person-id
    #(set-using-state % :beginning)))

(defn update-person-usage [ship person-id dTime]
  (if
    (not (get-using-position-ship ship person-id))
    ship
    (case (:using-state (c/get-person ship person-id))
      :beginning (update-person-usage-beginning ship person-id dTime)
      :using (update-person-usage-using ship person-id dTime)
      :ending (update-person-usage-ending ship person-id dTime)
      (update-person-usage-no-state ship person-id)
      )))
