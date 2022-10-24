(ns planet.game
  (:require
   [clojure.set :as set]))

(def directions
  [[0 1] [1 0] [1 -1] [0 -1] [-1 0] [-1 1]])

(def tile-types
  [:plains
   :forest
   :mountain
   :desert
   :rocks
   :ocean])

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

(defn open-locations
  [tiles]
  (let [locations (set (keys tiles))]
    (if (empty? tiles)
      [[0 0]]
      (set/difference
       (reduce
        (fn [open location]
          (let [adjacent (set (adjacent-locations location))]
            (set/union open adjacent)))
        #{}
        locations)
       locations))))

(defn add-tile
  [game location tile]
  (assoc-in game [:tiles location] tile))

(defn random-world
  [size]
  (reduce
   (fn [tiles n]
     (let [open (vec (open-locations tiles))
           location (rand-nth open)
           type (rand-nth tile-types)]
       (assoc tiles location {:type type})))
   {}
   (range size)))

;; [-2 2]      [0 1]     [2 0]     ---->    f   d   p
;;       [-1 1]     [1 0]          ---->      p   d
;; [-2 1]      [0 0]     [2 -1]    ---->    o   o   r
;;       [-1 0]     [1 -1]         ---->      r   m
;; [-2 0]      [0 -1]    [2 -2]    ---->    m   p   p

(defn print-tiles
  [tiles]
  )
