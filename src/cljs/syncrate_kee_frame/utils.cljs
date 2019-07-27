(ns syncrate-kee-frame.utils
  (:require [clojure.string :as s]))

(defn build-form-key
  [response]
  (-> (select-keys response [:last-method :uri])
      vals
      s/join
      (s/replace "/" "_")
      keyword))
