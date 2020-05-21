(ns world.species)
(require '[world.randutil :as randi])
(use '[clojure.string :only (join split)])

(def parts {:1 ["aby" "ale" "ansio" "ama" "aqua" "aru" "aando" "egre"]
            :2 ["b" "c" "d" "f" "g" "h" "j" "k" "l" "m" "n" "p" "q" "r" "s" "t" "v" "w" "x" "z"]
            :3 ["a" "e" "i" "o" "u" "y"]
            :4 ["an" "lips" "pers" "bes" "arc" "arg" "ark" "ask" "ass" "badl" "ball" "bimm" "bith"
                "bolt" "both" "boun" "caam" "cath" "caron" "celeg" "cer" "chadr" "chag" "chaz" "aeth"
                "chiss" "chist" "claw" "car" "zel" "tron" "firr" "yuuz"]})
(def combos [[:1 :2 :3]
             [:3 :2 :1]
             [:3 :2 :1 :2 :3]
             [:1 :2 :3 :4]
             [:4 :1]
             [:4 :3 :2 :1]])
(def base-list ["Abyssin", "Aleena", "Amanin", "Ansionian", "Anomid", "Anx", "Anzati", "Aqualish", "Arcona", "Argazdan", "Arkanian", "Aruzan", "Askajian", "Assembler", "Balosar", "Bando-Gora", "Barabel", "Besalisk", "Bimm", "Bith", "Boltrunians", "Bothan", "Bouncer", "Caamasi", "Cathar", "Carondian", "Celegian", "Cerean", "Chadra-Fan", "Chagrian", "Chazrach", "Chiss", "Chistori", "Clawdite", "Codru-Ji", "Coway", "Croke", "Dantari", "Dashade", "Defel", "Devaronian", "Drachnam", "Draethos", "Drall", "Dressellian", "Droch", "Droid", "Drovian", "Dug", "Duros", "Echani", "Elom", "Elomin", "Epicanthix", "ErKit", "Ewok", "Evocii", "Falleen", "Feeorin", "Ferroans", "Firrerreo", "Fosh", "Frozian", "Frozarns", "Gado", "Gamorrean", "Gand", "Gank", "GenDai", "Gerb", "Geonosian", "Givin", "Gizka", "Glymphid", "Gorax", "Gorith", "Gorog", "Gossam", "Gotal", "Gran", "Gree", "Grizmallt", "Gungan", "Gwurran", "Habassa", "Hallotan", "Hapan", "Himoran", "Hnemthean", "Hoojib", "Huk", "Human", "Hssis", "Hutt", "Iktotchi", "Iridonian", "Ishi-Tib", "Ithorian", "Jabiimas", "Jawa", "Kaleesh", "Kaminoan", "Kel-Dor", "Keshiri", "Kiffar", "Kitonak", "Klatooinian", "Kobok", "Kubaz", "Kurtzen", "Kushiban", "Kwa", "Kwi", "Lannik", "Latter", "Lepi", "Letaki", "Lurmen", "Mandalorians", "Massassi", "Melodie", "Mimbanite", "Miraluka", "Mirialan", "Mustafarian", "Muun", "Myneyrsh", "Myriad", "Nagai", "Nautolan", "Neimoidian", "Nelvaanian", "Neti", "Nikto", "Noghri", "Nosaurian", "Ogemite", "Omwati", "Ongree", "Ortolan", "Oswaft", "Palowick", "Paaerduag", "Pauan", "Phlog", "Priapulin", "Psadan", "Pweck", "Quarren", "Quermian", "Rakata", "Ranat", "Rishii", "Rodian", "Roonan", "Ruurian", "Ryn", "Rattataki", "Saffa", "Sanyassan", "Saurin", "Selkath", "Selonian", "Shawda-Ubb", "Shiido", "Shistavanen", "Sikan", "Sith", "Skakoan", "Sneevel", "Snivvian", "Squib", "Ssi-Ruuk", "Stereb", "Sullustan", "Talortai", "Tarasin", "Talz", "Taung", "Tchuukthai", "Teek", "Teevan", "Thakwaash", "Theelin", "Thennqora", "Terentatek", "Thisspiasian", "Thrella", "Timoliini", "Tlanda-Til", "Tof", "Togorian", "Togruta", "Toydarian", "Trandoshan", "Trianii", "Troig", "Tunroth", "Tusken-Raider", "Twilek", "Ubese", "Ugnaught", "Umbaran", "Unu", "Utai", "Utapaun", "Vaathkree", "Vagaari", "Veknoid", "Vella", "Verpine", "Vodran", "Vor", "Voxyn", "Vratix", "Vulptereen", "Vurk", "Wampa", "Weequay", "Whaladon", "Wharl", "Whill", "Whiphid", "Wirutid", "Wol-Cabasshite", "Wookiee", "Woostoid", "Wroonian", "Xting", "Xexto", "Ybith", "Yaka", "Yevetha", "Yuuzhan-Vong", "Yuvernian", "Yuzzem", "Yuzzum", "Zabrak", "Zeltron", "Zhell", "Zygerrian"])
(def core-structure ["crustacean" "dendroidal" "robotic" "gelly" "hairy" "fiery" "icy" "stoneformed" "muddy" "clay" "iron"])
(def form-like ["humanoid" "bipodal" "tripodal" "quadripodal" "myriapodal" "birdlike" "fishlike" "catlike" "doglike" "arachnoid"])

(defn random-species-name []
  (let [mappedPartsLists (map #(parts %) (randi/random-coll combos))
        mappedParts (map randi/random-coll mappedPartsLists)
        concatted (join "" mappedParts)]
    concatted))
(defn random-species [] {:name      (random-species-name)
                         :structure (randi/random-coll core-structure)
                         :form      (randi/random-coll form-like)})

(defn get-species [n] (repeatedly n random-species))

