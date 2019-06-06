(ns syncrate-kee-frame.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[syncrate-kee-frame started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[syncrate-kee-frame has shut down successfully]=-"))
   :middleware identity})
