(ns cont-examples.monad
  (:require [clojure.algo.monads :refer :all]
            [ring.util.response :refer (response)]
            [cont-examples.common :refer (prompt show)]))

(defn get-input [question]
  (fn [k]
    [(prompt question) (fn [answer] (k answer))]))

(defn converse []
  (domonad cont-m
    [name (get-input "name?")
     age (get-input "age?")]
    (show (str "Hello " name ", aged " age))))

(defn handler
  "Handle request - if there's a continuation, accept the answer into it.
Otherwise start a new conversation."
  [{session :session {answer :answer} :params}]
  (if-let [k (:cont session)]
    (k answer)
    (let [r (run-cont (converse))]
      (if (seq r)
        ; [question continuation]
        (-> (first r)
            (assoc-in [:session :cont] (second r)))
        ; response
        (update-in r [:session] dissoc :cont)))))