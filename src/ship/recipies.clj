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
(def empty-ship-recipe [])

(def transport-ship-recipe
  (let [
        o :basic-hallway
        c :basic-chassis
        x :basic-core
        m :basic-motor
        b :basic-battery
        s :basic-storage
        f :basic-firmery
        g :basic-game-computer
        u :basic-bunk
        e :basic-eating-stand
        h :basic-shower
        t :basic-toilet
        _ nil
        ]

    [
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     [_ _ _ _ _ _ s s _ _ _ s s _ _ _ _ _ _]
     [_ _ _ _ g h e t f _ u u u u u _ _ _ _]
     [_ _ _ _ o o c o o o o o c o o _ _ _ _]
     [_ _ _ _ s s b b o s o b b s s _ _ _ _]
     [_ _ _ s s s b b o x o b b s s s _ _ _]
     [_ _ s o o c o o o o o o c o o o s _ _]
     [_ _ _ s o m m m m m m m m m o s _ _ _]
     [_ _ _ _ m _ _ _ _ _ _ _ _ _ m _ _ _ _]
     [_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
     ]
    ))