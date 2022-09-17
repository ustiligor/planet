(ns planet.run
  (:require
   [planet.game :as game]))

(defn -main
  []
  (println (game/new-game)))
