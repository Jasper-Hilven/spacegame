(ns actor.action)
(use 'actor.use-object)
(use 'actor.walking)
(use 'actor.crew)
(use 'actor.actionqueue)
(defn set-walking-towards-next-action [ship person-id]
  (let [next-action-position (get-position-current-action-ship  ship person-id)]
    (if
      (nil? next-action-position)
      ship
      (set-person-walking-to ship person-id next-action-position))))
(defn set-use-object-after-walking [ship person-id]
  (update-person ship person-id #(set-using-position % (get-position-current-action %))))
(defn update-person-ship-action [ship person-id dTime]
  (cond
    (is-using-object? ship person-id)
    (update-person-usage ship person-id dTime)

    (is-walking-somewhere? ship person-id)
    (let [walking-result (update-person-walking ship person-id dTime)
          next-ship (:ship walking-result)
          done-walking (:done-walking walking-result)]
      (if done-walking (set-use-object-after-walking next-ship person-id) next-ship))

    :else (set-walking-towards-next-action ship person-id)))
