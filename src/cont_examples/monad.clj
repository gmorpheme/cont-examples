(ns ^{:doc "Implement the conversation using the continuation monad
  from algo.monads"}
  cont-examples.monad
  (:require [clojure.algo.monads :refer :all]
            [ring.util.response :refer (response)]
            [cont-examples.common :refer (prompt show)]))

(defn get-input [question]
  (fn [k]
    [(prompt question) (fn [answer] (println "Injecting " answer " into continuation.") (k answer))]))

(defn converse []
  (domonad cont-m
    [name (get-input "name?")
     age (get-input "age?")]
    (show (str "Hello " name ", aged " age))))

(defn handler
  "Handle request - if there's a continuation, accept the answer into it.
Otherwise start a new conversation."
  [{session :session {answer "answer"} :params :as request}]

  (println "Request is " request)
  (println "Answer is " answer)
  
  ;; continue the conversation if there is one, otherwise initiate it
  (let [r (or (when-let [k (:cont session)] (k answer))
              (run-cont (converse)))]

    (if (vector? r)
                                        ; [question continuation]
      (-> (first r)
          (assoc-in [:session :cont] (second r)))
      
                                        ; pure (final) response
      (update-in r [:session] dissoc :cont))))
