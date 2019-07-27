(ns syncrate-kee-frame.posts.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx subscribe]]
            [ajax.core :as http]
            [syncrate-kee-frame.db :refer [initial-db]]
            [syncrate-kee-frame.spec :refer [check-spec-interceptor]]
            [syncrate-kee-frame.errors.events :refer [build-form-key]]
            [clojure.walk :as w]
            [kee-frame.core :as kf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame.core :as rf]))

(def posts-interceptors [check-spec-interceptor])

(defn keywordize-id
  [coll]
  (->> coll
       (w/keywordize-keys)
       (map (fn [v]
              (let [id (-> v :id str keyword)]
                [id (assoc v :id id)])))
       (into {})))

(defn response->data
  [response]
  (let [post (w/keywordize-keys response)
        post-id (keyword (str (or (:id post) (random-uuid))))
        post-data (assoc post :id post-id)]
    post-data))

(reg-event-fx
  :load-posts
  posts-interceptors
  (fn-traced [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/api/posts"
                  :response-format (http/json-response-format)
                  :on-success      [:posts-loaded-successfully]
                  :on-failure      [:errors/set-request-error]}}))

(reg-event-fx
  :load-post
  posts-interceptors
  (fn-traced [{:keys [db]} [_ [post-id]]]
    {
     :db (assoc db :active-post (keyword post-id))
     :http-xhrio {:method :get
                  :uri (str "/api/posts/" post-id)
                  :response-format (http/json-response-format)
                  :on-success [:post-loaded]
                  :on-failure [:errors/set-request-error]}}))

(reg-event-db
  :posts-loaded-successfully
  posts-interceptors
  (fn-traced [db [_ response]]
    (-> db
      (assoc-in [:loading :posts] false)
      (assoc-in [:posts] (keywordize-id (get response "data"))))))

(reg-event-fx
  :create-post
  posts-interceptors
  (fn-traced [_ [_ post]]
    {:http-xhrio {:method          :post
                  :uri             "/api/posts"
                  :body            (js/JSON.stringify (clj->js post))
                  :response-format (http/json-response-format)
                  :on-success      [:post-created]
                  :on-failure      [:errors/handle-server-validation-error]}}))

(reg-event-fx
  :post-created
  posts-interceptors
  (fn-traced [{:keys [db]} [_ response]]
    (let [post-data (response->data response)
          post-id (:id post-data)]
      {:dispatch [:forms/clear-form :POST_api_posts]
       :db (-> db
               (update-in [:posts post-id] merge post-data))
       :navigate-to [:posts]})))

(reg-event-fx
  :post-loaded
  posts-interceptors
  (fn-traced [_ [_ response]]
    {:dispatch [:upsert-post response]}))

(reg-event-db
  :upsert-post
  posts-interceptors
  (fn-traced [db [_ response]]
    (let [post-data (response->data response)
          post-id (:id post-data)]
      (-> db
          (update-in [:posts post-id] merge post-data)))))
