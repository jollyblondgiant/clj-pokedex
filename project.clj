(defproject pokedex "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [http-kit "2.6.0-alpha1"]
                 [compojure "1.6.2"]
                 [hiccup "1.0.5"]
                 [clj-http "3.12.3"]
                 [org.clojure/data.json "2.4.0"]
                 ;;add datomic


                 [com.taoensso/sente "1.16.2"]
                 [org.clojure/clojurescript "1.10.891"
                  :scope "provided"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.5"
                  :exclusions [org.clojure/tools.reader]]
                 [reagent "1.1.0"]
                 [reagent-utils "0.3.3"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]

                 ;;re-frame

                 ]
  :resource-paths ["resources" "target/cljsbuild"]
  :source-paths ["src/clj" "src/cljs"]
  :main ^:skip-aot pokedex.core
  :target-path "target/%s"
  ;;:profiles {:uberjar {:aot :all}}

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to        "target/cljsbuild/public/js/app.js"
              :output-dir       "target/cljsbuild/public/js"
              :source-map       "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :infer-externs true
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:on-jsload "pokedex.core/mount-root"}
             :compiler
             {:main "pokedex.dev"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}
            }}

  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl
                      ]
   :css-dirs ["resources/public/css"]
   :ring-handler pokedex.core/app-routes}

  :profiles {:dev {:repl-options {:init-ns pokedex.repl}
                   :dependencies [[cider/piggieback "0.5.2"]
                                  [binaryage/devtools "1.0.2"]
                                  [ring/ring-mock "0.4.0"]
                                  [ring/ring-devel "1.9.1"]
                                  [prone "2020-01-17"]
                                  [figwheel-sidecar "0.5.20"]
                                  [nrepl "0.8.3"]
                                  [thheller/shadow-cljs "2.14.3"]
                                  [pjstadig/humane-test-output "0.10.0"]

 ]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.20"]
]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :shadow-cljs {:dependencies [[com.google.javascript/closure-compiler-unshaded "v20210505"]]}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}}

  )
