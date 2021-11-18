(ns pokedex.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [clojure.data.json :refer [read-str write-str]]
            [clj-http.client :as client]
            [compojure.core :refer :all]
            [hiccup.page :refer [include-js include-css html5]]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit
             :refer (get-sch-adapter)]
            [compojure.route :as route]))

(defn random-pokemon [region] ;; #{:johto :kanto}
  (let [num (case region
              :johto (+ 151 (rand-int 251))
              :kanto (+ 1 (rand-int 151))
              (+1 (rand-int 897)))
        {name "name"
         {sprite "front_default"
          :as sprites} "sprites"
         :as pokemon}(-> "https://pokeapi.co/api/v2/pokemon/%s"
            (format num)
            client/get
            :body ;;also handle status
            (read-str :keyword-fn keyword)
            )]
    {:name name :sprite-url sprite}))

(def mount-target
  #_(let  [csrf-token
           (force ring.middleware.anti-forgery/*anti-forgery-token*)])
  [:div#app
   #_[:div#sente-csrf-token {:data-csrf-token csrf-token}]
   ])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css ;;adds bulma css
    "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.4/css/bulma.min.css")
   (include-css "/css/site.css")])

(defn loading-page []
  (let [pokemon (random-pokemon :kanto)]
    (html5
     (head)
     [:body {:class "body-container"}
      mount-target
      [:h1 (:name pokemon)]
      [:image
       {:src (:sprite-url pokemon)}]
      (include-js "/js/app.js")
      [:script "pokedex.core.init_BANG_()"]])))

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {})]

  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(defroutes app-routes
  (GET "/" [] index-handler)
  (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post req))
  (route/not-found "Welcome to Lost"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server #'app-routes {:port port})
    (println (str "running server at 127.0.0.1:" port "/"))))
