(ns ship.recipies)
(def start-ship-recipe
  (let [h :basic-chassis
        c :basic-core
        m :basic-motor
        b :basic-battery
        s :basic-storage
        _ nil]
    [
     [_ _ _ m _ _ _]
     [_ _ b h b _ _]
     [_ m s s s m _]
     [m h s c s h m]
     [_ m s s s m _]
     [_ _ s h s _ _]
     [_ m h m h m _]
     ]
    )
  )