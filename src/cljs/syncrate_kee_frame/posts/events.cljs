(ns syncrate-kee-frame.posts.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :as http]
            [syncrate-kee-frame.db :refer [initial-db]]
            [clojure.walk :as w]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn keywordize-id
  [coll]
  (->> coll
       (w/keywordize-keys)
       (map (fn [v]
              (let [id (-> v :id str keyword)]
                [id (assoc v :id id)])))
       (into {})))

(reg-event-db
  :set-active-post
  (fn-traced [db [_ [post-id]]]
    (assoc db :active-post (keyword post-id))))

(reg-event-db
  :posts-loaded-successfully
  (fn-traced [db [_ posts]]
    (do
      ;(js/console.log "POSTS:")
      (js/console.log posts)

      (-> db
        (assoc-in [:loading :posts] false)
        (assoc-in [:posts] (keywordize-id posts))))))

(reg-event-fx
  :create-post
  (fn-traced [db [_ post]]
    {:http-xhrio {:method :post
                  :uri "api/posts"
                  :body (js/JSON.stringify (clj->js post))
                  :headers (select-keys (http/json-request-format) [:content-type])
                  :response-format (http/json-response-format)
                  :on-success      [:post-created-successfully]}}))

(reg-event-fx
  :post-created-successfully
  (fn-traced [db [_ response]]
    {:dispatch [:upsert-post response]}))

(reg-event-db
  :upsert-post
  (fn-traced [db [_ post]]
    (def captured-post post)
    (let [post-id (keyword (str (or (:id post) (random-uuid))))
          post-data (merge {:id post-id} post)]
      (-> db
          (update-in [:posts post-id] merge post-data)))))
