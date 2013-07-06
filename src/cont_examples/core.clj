(ns cont-examples.core
  (:require [ring.middleware.session :as session]
            [cont-examples.async :as async]
            [cont-examples.monad :as monad])
  (:use [ring.adapter.jetty]))

(defn handler [{uri :uri :as request}]
  (condp = uri
    "/async" (async/handler request)
    "/monad" (monad/handler request)))

(def app
  (-> handler
      (session/wrap-session)))
