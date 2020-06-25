(ns actor.core
  (:require [actor.actionqueue :as aq]
            [actor.needs :as n]
            [actor.crew :as c]
            [actor.walking :as w]
            [actor.use-object :as uo]
            [actor.position :as p]))

(def crew-member
  (reduce #(into % %2)
          c/start-person-basics
          [p/start-person-position
           aq/starting-actor-with-actions
           n/starting-actor-with-needs
           uo/start-actor-with-uses
           w/start-person-with-walking]
          ))


(defn start-crew []
  {0 crew-member})
