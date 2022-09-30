(ns planet.game)

(def transporter-attributes
  {:arkakx
   {:capacity 2
    :road 2
    :land 1}
   :bontor
   {:capacity 3
    :road 3}
   :cludim
   {:capacity 6
    :road 4}
   :xygom
   {:capacity 3
    :water 3}
   :yaalg
   {:capacity 5
    :water 4}
   :zonozodo
   {:capacity 8
    :water 6}})

(defn make-player
  [home]
  {:transporters
   (reduce
    (fn [transporters n]
      (assoc
       transporters
       [:arkakx n]
       {:tile home
        :cargo []}))
    {}
    (range 3))
   :home home
   })

(defn new-game
  [players]
  {:tiles {}
   :players {}
   :roads {}
   :resources {}
   :buildings {}
   :walls {}
   :rivers {}
   :wonder {}
   :temple {}
   :order {}})
