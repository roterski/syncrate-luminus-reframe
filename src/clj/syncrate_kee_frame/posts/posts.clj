(ns syncrate-kee-frame.posts.posts
  (:require [syncrate-kee-frame.db.core :as db]
            [clojure.pprint :refer [pprint]]
            [ring.util.http-response :as response]))

(defn create-post [{:keys [body-params]}]
  (try
    (let [created-post (db/create-post! body-params)]
      {:status 200
       :body created-post})
    (catch Exception e
      (let [{id :syncrate-kee-frame/error-id
             errors :errors} (ex-data e)]
        (case id
          :validation
          (response/bad-request {:errors errors})
          ;;else
          (response/internal-server-error
            {:errors {:server-error ["Failed to create post!"]}}))))))

(defn index-posts [_]
  (let [posts (db/get-posts)]
    {:status 200, :body {:data posts}}))

(defn show-post [{:keys [path-params]}]
 (let [post-id (Integer/parseInt (:id path-params))]
   {:status 200
    :body (db/get-post {:id post-id})}))
