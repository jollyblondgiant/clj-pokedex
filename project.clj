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
                 [com.taoensso/sente "1.16.2"]

                 ;;reagent
                 ;;re-frame


                 ]
  :source-paths ["src/clj" "src/cljs"]
  ;;:resource-paths ["resources" "target/cljsbuild"]
  :main ^:skip-aot pokedex.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
