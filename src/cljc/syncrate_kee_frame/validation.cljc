(ns syncrate-kee-frame.validation
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(def error-key "Validation failure")

(def post-schema {:title [[v/min-count 3] v/required]
                  :body v/string})

(def user-schema {:facebook_id v/required})

(defn valid?
  [data schema]
  (apply b/valid? data (first (seq schema))))

(defn validate
  [data schema]
  (apply b/validate data (first (seq schema))))

(defn validate-field
  [value field schema]
  (validate {field value} (select-keys schema [field])))

(defn validate!
  [data schema]
  (let [[errors validated] (validate data schema)]
    (if errors
      (throw (ex-info error-key errors))
      validated)))
