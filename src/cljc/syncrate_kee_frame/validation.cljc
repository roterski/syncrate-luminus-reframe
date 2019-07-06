(ns syncrate-kee-frame.validation
  (:require [struct.core :as st]))

(def post-schema
  [[:title st/required st/string [st/min-count 3]]])
