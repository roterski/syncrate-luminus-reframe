(ns syncrate-kee-frame.spec
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf]))

(defn check-and-throw
  [a-spec db]
  (when-not (s/valid? a-spec db)
            (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;(s/def ::errors (or nil? (s/map-of keyword? map?)))

(s/def ::active-post (s/or :keyword keyword? :nil nil?))

(s/def :post/id keyword?)
(s/def :post/title string?)
(s/def :post/body string?)
(s/def :post/map (s/keys :req-un [:post/id :post/title :post/body]))
(s/def ::posts (s/map-of :post/id :post/map))

(s/def ::db (s/keys :req-un [::active-post
                             ;::errors
                             ::posts]))

(def check-spec-interceptor (rf/after (partial check-and-throw ::db)))
