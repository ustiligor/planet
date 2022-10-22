(ns planet.game
  (:require
   [clojure.set :as set]))

(def directions
  [[0 1] [1 0] [1 -1] [0 -1] [-1 0] [-1 1]])

(defn apply-direction
  [location direction]
  (let [[x y] location
        [dx dy] direction]
    [(+ x dx)
     (+ y dy)]))

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
  []
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

(defn adjacent-locations
  [location]
  (map
   (fn [direction]
     (apply-direction location direction))
   directions))

(defn open-locations?
  [game]
  (let [tiles (get game :tiles)
        locations (set (keys tiles))]
    (if (empty? tiles)
      [[0 0]]
      (reduce
       (fn [open location]
         (let [adjacent (set (adjacent-locations location))
               locally-open (set/difference adjacent locations)]
           (set/union open locally-open)))
       #{}
       locations))))

(defn add-tile
  [game location tile]
  (assoc-in game [:tiles location] tile))
