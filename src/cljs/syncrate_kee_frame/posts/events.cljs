(ns syncrate-kee-frame.posts.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :as http]
            [syncrate-kee-frame.db :refer [initial-db]]
            [clojure.walk :as w]
            [kee-frame.core :as kf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn keywordize-id
  [coll]
  (->> coll
       (w/keywordize-keys)
       (map (fn [v]
              (let [id (-> v :id str keyword)]
                [id (assoc v :id id)])))
       (into {})))

(reg-event-fx
  :load-posts
  (fn-traced [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/api/posts"
                  :response-format (http/json-response-format)
                  :on-success      [:posts-loaded-successfully]
                  :on-failure      [:common/set-request-error]}}))

(reg-event-fx
  :load-post
  (fn-traced [{:keys [db]} [_ [post-id]]]
    {
     :db (assoc db :active-post (keyword post-id))
     :http-xhrio {:method :get
                  :uri (str "/api/post/" post-id)
                  :response-format (http/json-response-format)
                  :on-success [:post-loaded]
                  :on-failure [:common/set-request-error]}}))

(reg-event-db
  :posts-loaded-successfully
  (fn-traced [db [_ posts]]
    (-> db
      (assoc-in [:loading :posts] false)
      (assoc-in [:posts] (keywordize-id posts)))))

(reg-event-fx
  :create-post
  (fn-traced [db [_ post]]
    {:http-xhrio {:method :post
                  :uri "/api/posts"
                  :body (js/JSON.stringify (clj->js post))
                  :headers {:content-type "application/json"}
                  :response-format (http/json-response-format)
                  :on-success      [:post-created]
                  :on-failure      [:common/set-error]}}))

(reg-event-fx
  :post-created
  (fn-traced [db [_ response]]
    {:dispatch [:upsert-post response]
     :navigate-to [:posts]}))

(reg-event-fx
  :post-loaded
  (fn-traced [db [_ response]]
    {:dispatch [:upsert-post response]}))

(reg-event-db
  :upsert-post
  (fn-traced [db [_ post]]
    (let [post (w/keywordize-keys post)
          post-id (keyword (str (or (:id post) (random-uuid))))
          post-data (assoc post :id post-id)]
      (-> db
          (update-in [:posts post-id] merge post-data)))))
