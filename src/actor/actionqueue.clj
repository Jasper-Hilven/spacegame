(ns actor.actionqueue)
(use 'actor.crew)
(def example-actor-with-actions {:actions '({:position  {:x 0 :y 2}
                                             :use-block :basic-bunk})})
(def starting-actor-with-actions {:actions '()})
(defn get-actions [actor] (:actions actor))
(defn drop-current-action [actor] (update actor :actions rest))
(defn add-action [actor action] (update actor :actions #(conj % action)))
(defn get-current-action [actor] (first (get-actions actor)))
(defn get-position-current-action [actor] (get (get-current-action actor) :position nil))
(defn get-position-current-action-ship [ship actor-id]
  (get-position-current-action
    (get-person ship actor-id)))
(defn get-block-type-current-action [actor] (get (get-current-action actor) :use-block nil))

(defn add-use-block-at-action [actor position block-type]
  (add-action actor {:position position :use-block block-type}))
(defn add-use-block-at-action-ship [ship actor-id position block-type]
  (update-person ship actor-id #(add-use-block-at-action % position block-type)))


(add-use-block-at-action starting-actor-with-actions {:x 0 :y 2} :basic-bunk)
(drop-current-action example-actor-with-actions)
(get-current-action starting-actor-with-actions)
(get-current-action example-actor-with-actions)
(add-action starting-actor-with-actions "action")