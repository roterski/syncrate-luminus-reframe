(ns syncrate-kee-frame.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [syncrate-kee-frame.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[syncrate-kee-frame started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[syncrate-kee-frame has shut down successfully]=-"))
   :middleware wrap-dev})
