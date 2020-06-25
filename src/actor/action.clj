(ns actor.action
  (:require [actor.use-object :as uo]
            [actor.walking :as w]
            [actor.crew :as c]
            [actor.actionqueue :as aq]))

(defn set-walking-towards-next-action [ship person-id]
  (let [next-action-position (aq/get-position-current-action-ship ship person-id)]
    (if
      (nil? next-action-position)
      ship
      (w/set-person-walking-to ship person-id next-action-position))))

(defn set-use-object-after-walking [ship person-id]
  (c/update-person ship person-id #(uo/set-using-position % (aq/get-position-current-action %))))

(defn update-person-ship-action-walking-somewhere [ship person-id dTime]
  (let [walking-result (w/update-person-walking ship person-id dTime)
        next-ship (:ship walking-result)
        done-walking (:done-walking walking-result)
        error (:error walking-result)]
    (if (= :tile-no-longer-walkable error)
      (set-walking-towards-next-action ship person-id)
      (if done-walking
        (set-use-object-after-walking next-ship person-id)
        next-ship))))

(defn update-person-ship-action [ship person-id dTime]
  (cond
    (uo/is-using-object? ship person-id)
    (uo/update-person-usage ship person-id dTime)
    (w/is-walking-somewhere? ship person-id)
    (update-person-ship-action-walking-somewhere ship person-id dTime)
    :else (set-walking-towards-next-action ship person-id)))
