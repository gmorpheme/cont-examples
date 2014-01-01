(ns ^{:doc "Common HTML helpers for each implementation"}
  cont-examples.common
  (:require [ring.util.response :refer (response)]))

(defn prompt
  "Display question and text box for response."
  [question]
  (response (str
             "<html><body><form method=\"POST\"><p>"
             question
             "</p><input type=\"text\" name=\"answer\"></input><input type=\"submit\"/></form></body></html>")))

(defn show
  "Display message"
  [text]
  (response (str
             "<html><body><p>"
             text
             "</p></body></html>")))

