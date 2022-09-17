(ns planet.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[planet started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[planet has shut down successfully]=-"))
   :middleware identity})
