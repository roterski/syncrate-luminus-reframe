(ns syncrate-kee-frame.routes.home
  (:require
    [syncrate-kee-frame.layout :as layout]
    [syncrate-kee-frame.db.core :as db]
    [clojure.java.io :as io]
    [syncrate-kee-frame.middleware :as middleware]
    [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])

