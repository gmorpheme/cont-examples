(ns cont-examples.async
  (:require [clojure.core.async :refer (chan <! >! <!! go put! take! close!)]
            [ring.util.response :refer (response)]
            [cont-examples.common :refer (prompt show)]))

(defn make-coroutine
  "Construct in and out channels for communicating with function f.
Returns [call resume] for initial call and subsequent resumes."
  [f]
  (let [out (chan)
        in (chan)
        yield (fn [question] (go (>! out (prompt question)) (<! in)))
        return (fn [value] (go (>! out (show value)) (close! in)))
        call (fn [] out)
        resume (fn [value] (go (>! in value) (<! out)))]
    (f yield return)
    [call resume]))

(defn converse [yield return]
  (go
   (let [name (<! (yield "name?"))
         age (<! (yield "age?"))]
     (return (str "Hello " name ", aged " age)))))

(defn handler
  "Handle request - if there's a resume function in the session, call
with answer. Otherwise start a new conversation."
  [{{resume :resume :as session} :session {answer "answer"} :params}]
  (if (and resume answer)
    (<!! (resume answer))
    (let [[call resume] (make-coroutine converse)]
      (-> (<!! (call))
          (assoc-in [:session :resume] resume)))))
