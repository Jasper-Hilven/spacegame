(ns actor.use-object)
(use 'actor.needs)
(use 'ship.basics)
(use 'ship.energy)
(use 'ship.storage)
(use 'world.resource)
(use 'actor.crew)
(use 'debux.core)
(def using-states [:beginning :using :ending])

(defn get-counter [person]
  (get-in person [:using-state-counter] 0))

(defn set-using-state [person v] (assoc person :using-state v))
(defn get-using-state [person] (:using-state person))
(defn set-using-state-counter [person v] (assoc person :using-state-counter v))
(defn increment-using-state-counter [person dTime]
  (set-using-state-counter person (+ dTime (get-counter person))))
(defn get-using-position [person] (:using person))
(defn set-using-position [person position] (assoc person :using position))
(defn set-using-position-ship [ship person-id position]
  (update-person ship person-id #(set-using-position % position)))
(defn get-using-position-ship [ship person-id]
  (get-using-position (get-person ship person-id)))
(defn get-using-block-ship [ship person-id]
  (let [position (get-using-position-ship ship person-id)]
    (and position (get-block-at-position ship position))))


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
        (set-using-state-counter 0))
    (increment-using-state-counter person dTime)))

(defn is-useful-to-do [ship person-id]
  (let [block (get-using-block-ship ship person-id)
        optimizing (and block (get-stop-when-full block))
        is-full (or (nil? optimizing) (>= (get-person-need-ship ship person-id optimizing) 1))]
    (not is-full)))

(defn update-person-ship-using-object [ship person-id using-position dTime]
  (let [block (get-block-at-position ship using-position)
        interactive-block (:interactive block)
        person (get-person ship person-id)
        required-resources (mult-resources (get-in interactive-block [:resource-usage]) dTime)
        required-energy (* dTime (get-in interactive-block [:energy-usage] 0))]
    (if (and
          (has-energy ship required-energy)
          (has-in-ship ship required-resources)
          (is-useful-to-do ship person-id))
      {:ship   (-> ship (take-energy required-energy) (take-from-ship required-resources))
       :person (update-person-with-object person (:interact-stats interactive-block) dTime)}
      {:ship ship :person (-> person (set-using-state :ending) (set-using-state-counter 0))}
      )))

(defn update-person-usage [ship person-id dTime]
  (let [person (get-person ship person-id)
        using-position (:using person)
        updated-person-and-ship
        (if
          (not using-position)
          {:ship ship :person person}
          (let [block (get-block-at-position ship using-position)
                interactive-block (:interactive block)
                using-state-person (:using-state person)
                start-delay (get-in interactive-block [:start-delay] 0)
                end-delay (get-in interactive-block [:end-delay] 0)
                ]
            (case using-state-person
              nil {:ship ship :person (set-using-state person :beginning)}
              :beginning
              {:ship   ship
               :person (update-person-begin-using-object person start-delay dTime)}
              :using (update-person-ship-using-object ship person-id using-position dTime)
              :ending
              {:ship   ship
               :person (update-person-end-using-object person end-delay dTime)}
              )))]
    (set-person
      (:ship updated-person-and-ship)
      person-id
      (:person updated-person-and-ship))))
