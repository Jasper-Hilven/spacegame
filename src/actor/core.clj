(ns actor.core)
(use 'actor.actionqueue)
(use 'actor.needs)
(use 'actor.crew)
(use 'actor.walking)
(use 'actor.use-object)
(def crew-member
  (reduce #(into % %2)
          start-person-basics
          [
           starting-actor-with-actions
           starting-actor-with-needs
           start-actor-with-uses
           start-person-with-walking]
          ))


(defn start-crew [start-person-needs]
  {0 crew-member})
