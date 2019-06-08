(defproject syncrate-kee-frame "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[buddy "2.0.0"]
                 [cheshire "5.8.1"]
                 [clj-oauth "1.5.5"]
                 [cljs-ajax "0.8.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cognitect/transit-clj "0.8.313"]
                 [com.google.javascript/closure-compiler-unshaded "v20190513" :scope "provided"]
                 [conman "0.8.3"]
                 [cprop "0.1.13"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [funcool/struct "1.3.0"]
                 [kee-frame "0.3.3" :exclusions [metosin/reitit-core org.clojure/core.async]]
                 [org.clojure/core.async "0.4.490"]
                 [luminus-immutant "0.2.5"]
                 [luminus-migrations "0.6.5"]
                 [luminus-transit "0.1.1"]
                 [markdown-clj "1.10.0"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/reitit "0.3.5"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojure/google-closure-library "0.0-20190213-2033d5d9" :scope "provided"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.postgresql/postgresql "42.2.5"]
                 [org.webjars.npm/bulma "0.7.4"]
                 [org.webjars.npm/material-icons "0.3.0"]
                 [org.webjars/webjars-locator "0.36"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [re-frame "0.10.6"]
                 [reagent "0.8.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.12"]
                 [thheller/shadow-cljs "2.8.37" :scope "provided"]]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot syncrate-kee-frame.core

  :plugins [[org.clojars.punkisdead/lein-cucumber "1.0.5"]
            [lein-shadow "0.1.3"]
            [lein-immutant "2.1.0"]
            [lein-sassc "0.10.4"]
            [lein-auto "0.1.2"]]
  :cucumber-feature-paths ["test/clj/features"]

   :sassc
   [{:src "resources/scss/screen.scss"
     :output-to "resources/public/css/screen.css"
     :style "nested"
     :import-path "resources/scss"}] 
  
   :auto
   {"sassc" {:file-pattern #"\.(scss|sass)$" :paths ["resources/scss"]}} 
  
  :hooks [leiningen.sassc]
  :clean-targets ^{:protect false}
  [:target-path "target/cljsbuild"]
  :shadow-cljs
  {:nrepl {:port 7002}
   :builds
   {:app
    {:target :browser
     :output-dir "target/cljsbuild/public/js"
     :asset-path "/js"
     :modules {:app {:entries [syncrate-kee-frame.app]}}
     :dev {:compiler-options {:closure-defines {re-frame.trace/trace-enabled? true
                                                day8.re-frame.tracing/trace-enabled? true}}}
     :devtools {:watch-dir "resources/public"
                :http-root   "public"
                :http-port   3000
                :preloads    [day8.re-frame-10x.preload]}}
    :test
    {:target :node-test
     :output-to "target/test/test.js"
     :autorun true}}}

  :npm-deps [[shadow-cljs "2.8.31"]
             [create-react-class "15.6.3"]
             [react "16.8.6"]
             [react-dom "16.8.6"]
             ["@smooth-ui/core-sc" "9.0.2"]
             [styled-components "^4.1.2"]
             [styled-icons "^5.4.0"]
             ["highlight.js" "^9.15.8"]
             [react-flip-move  "^3.0.3"]
             ["react-highlight.js" "^1.0.7"]]

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["shadow" "release" "app"]]
             
             :aot :all
             :uberjar-name "syncrate-kee-frame.jar"
             :source-paths ["env/prod/clj" "env/prod/cljs"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                  :dependencies [[binaryage/devtools "0.9.10"]
                                 [cider/piggieback "0.4.1"]
                                 [clj-webdriver/clj-webdriver "0.7.2"]
                                 [expound "0.7.2"]
                                 [org.apache.httpcomponents/httpcore "4.4"]
                                 [org.clojure/core.cache "0.6.3"]
                                 [org.seleniumhq.selenium/selenium-server "2.48.2" :exclusions [org.bouncycastle/bcprov-jdk15on org.bouncycastle/bcpkix-jdk15on]]
                                 [pjstadig/humane-test-output "0.9.0"]
                                 [prone "1.6.3"]
                                 [re-frisk "0.5.4.1"]
                                 [day8.re-frame/re-frame-10x "0.4.0"]
                                 [re-frame "0.10.5"]
                                 [ring/ring-devel "1.7.1"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]]
                  
                  
                  :source-paths ["env/dev/clj" "env/dev/cljs" "test/cljs"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
                  
                  

   :profiles/dev {}
   :profiles/test {}})
