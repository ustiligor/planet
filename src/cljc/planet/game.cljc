(ns planet.game
  (:require
   [clojure.set :as set]
   [clojure.string :as string]))

(def directions
  [[0 1] [1 0] [1 -1] [0 -1] [-1 0] [-1 1]])

(def land-types
  [:plains
   :forest
   :mountain
   :desert
   :rocks
   :ocean])

(def land-frequencies
  {[:plains 0] 10
   [:plains 2] 3
   [:plains 3] 1
   [:forest 0] 10
   [:forest 2] 3
   [:forest 3] 1
   [:mountain 0] 8
   [:mountain 1] 5
   [:desert 0] 10
   [:desert 2] 1
   [:desert 3] 1
   [:rocks 0] 10
   [:rocks 2] 3
   [:rocks 3] 1
   [:ocean 0] 10})

(defn pick-tile
  [distribution]
  (let [total (reduce + 0 (vals distribution))]
    (loop [choice (* (rand) total)
           options (vec distribution)]
      (let [[tile proportion] (first options)]
        (if (< choice proportion)
          tile
          (recur
           (- choice proportion)
           (rest options)))))))

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
       {:tile {:location home}
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
   :wonder {}
   :temple {}
   :order {}})

(defn adjacent-locations
  [location]
  (map
   (fn [direction]
     (apply-direction location direction))
   directions))

(defn adjacent-tiles
  [tiles location]
  (let [adjacent-to (adjacent-locations location)
        adjacent (filter
                  (fn [adjacent]
                    (get tiles adjacent))
                  adjacent-to)]
    adjacent))

(defn adjacent-count
  [tiles location]
  (count (adjacent-tiles tiles location)))

(defn adjacent-counts
  [tiles locations]
  (reduce
   (fn [counts location]
     (assoc
      counts
      location
      (adjacent-count tiles location)))
   {}
   locations))

(defn most-adjacent-tiles
  [tiles locations]
  (let [counts (adjacent-counts tiles locations)
        count-groups (group-by last counts)
        largest (last (sort (keys count-groups)))
        largest-group (get count-groups largest)]
    (map first largest-group)))

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

(defn make-tile
  [distribution]
  (let [[land rivers] (pick-tile distribution)]
    {:type land :rivers (take rivers (shuffle directions))}))

(defn generate-tiles
  [size distribution]
  (map
   (fn [n] (make-tile distribution))
   (range size)))

(defn random-world
  [size available-tiles]
  (reduce
   (fn [tiles tile]
     (let [open (vec (open-locations tiles))
           location (rand-nth open)]
       (assoc tiles location tile)))
   {}
   (take size (shuffle available-tiles))))

(defn glob-world
  [size river-frequency]
  (reduce
   (fn [tiles n]
     (let [open (vec (open-locations tiles))
           most-adjacent (most-adjacent-tiles tiles open)
           location (rand-nth most-adjacent)
           adjacent (adjacent-tiles tiles location)
           oceans (filter
                   (fn [tile]
                     (= :ocean (get-in tiles [tile :type])))
                   adjacent)
           type (rand-nth land-types)]
       (assoc tiles location {:type type})))
   {}
   (range size)))

;; [-2 2]      [0 1]     [2 0]     ---->    f   d   p
;;       [-1 1]     [1 0]          ---->    | p | d
;; [-2 1]      [0 0]     [2 -1]    ---->    o | o | r
;;       [-1 0]     [1 -1]         ---->    | r | m
;; [-2 0]      [0 -1]    [2 -2]    ---->    m   p   p

(defn leftmost-index
  [tiles]
  (first (sort (map first (keys tiles)))))

(defn location-height
  [[x y]]
  (+ x (* 2 y)))

(defn print-tiles
  [tiles]
  (let [rows (group-by location-height (keys tiles))
        column-keys (sort (map first (keys tiles)))
        left (first column-keys)
        right (last column-keys)
        column-indexes (range left (inc right))
        row-keys (reverse (sort (keys rows)))
        top (first row-keys)
        bottom (last row-keys)
        row-indexes (range top (dec bottom) -1)]
    (doseq [row row-indexes]
      (let [columns
            (map
             (fn [column]
               (let [offset (- row column)]
                 (if (odd? offset)
                   "|"
                   (let [location [column (/ offset 2)]
                         tile (get tiles location)]
                     (if tile
                       (let [tile-type (get tile :type)
                             tile-symbol (first (name tile-type))]
                         tile-symbol)
                       " ")))))
             column-indexes)
            row-string (string/join " " columns)]
        (println row-string)))))
