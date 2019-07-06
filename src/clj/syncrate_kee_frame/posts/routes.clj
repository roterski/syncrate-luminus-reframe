(ns syncrate-kee-frame.posts.routes
  (:require
    [syncrate-kee-frame.middleware :refer [wrap-restricted]]
    [syncrate-kee-frame.posts.posts :refer [create-post index-posts show-post]]))

(defn posts-routes []
  ["/posts"
    {:swagger {:tags ["posts"]}
     :middleware [wrap-restricted]}
    [""
     {:get  {:summary    "return list of posts"
             :parameters {}
             :responses  {200 {:body {:data [{:id         pos-int?
                                              :title      string?
                                              :body       string?
                                              :created_at some?}]}}}
             :handler    index-posts}

      :post {:summary    "create post"
             :parameters {:body {:title string?
                                 :body  string?}}
             :responses  {200 {:body map?}
                          500 {:errors map?}}
             :handler    create-post}}]
    ["/:id"
     {:get {:summary "return single post"
            :parameters {:path-params {:id pos-int?}}
            :handler show-post}}]])
