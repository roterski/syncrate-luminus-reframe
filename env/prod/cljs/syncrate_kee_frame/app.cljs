(ns syncrate-kee-frame.app
  (:require [syncrate-kee-frame.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init! false)
