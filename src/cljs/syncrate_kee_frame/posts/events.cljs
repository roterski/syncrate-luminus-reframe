(ns syncrate-kee-frame.posts.events
  (:require [re-frame.core :refer [reg-event-db]]
            [syncrate-kee-frame.db :refer [initial-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(reg-event-db
  :set-active-post
  (fn-traced [db [_ [post-id]]]
    (assoc db :active-post (keyword post-id))))
