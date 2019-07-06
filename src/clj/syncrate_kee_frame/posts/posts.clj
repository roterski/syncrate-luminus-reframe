(ns syncrate-kee-frame.posts.posts
  (:require [syncrate-kee-frame.db.core :as db]
            [clojure.pprint :refer [pprint]]
            [struct.core :as st]
            [syncrate-kee-frame.middleware.exception :refer [handle-exception]]
            [syncrate-kee-frame.validation :refer [post-schema]]))


(defn create-post [{:keys [body-params]}]
  (try
    (let [created-post (-> body-params
                           (st/validate! post-schema)
                           (db/create-post!))]
      {:status 200
       :body created-post})
    (catch Exception e
      (handle-exception e "create post"))))

(defn index-posts [_]
  (let [posts (db/get-posts)]
    {:status 200, :body {:data posts}}))

(defn show-post [{:keys [path-params]}]
 (let [post-id (Integer/parseInt (:id path-params))]
   {:status 200
    :body (db/get-post {:id post-id})}))
