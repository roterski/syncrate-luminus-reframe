(ns syncrate-kee-frame.nav.views.nav
  (:require [re-frame.core :as rf]
            [syncrate-kee-frame.nav.views.authenticated :refer [authenticated]]
            [syncrate-kee-frame.nav.views.public :refer [public]]))

(defn nav
  []
  ;(let [logged-in? @(rf/subscribe [:logged-in?])]
  ;  (if logged-in?
  [authenticated])
      ;[public]])