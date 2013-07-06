(defproject cont-examples "0.1.0-SNAPSHOT"
  :description "Various examples of yield / continuation based web handling"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :plugins [[lein-ring "0.8.3"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/algo.monads "0.1.4"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [ring/ring-core "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.8"]]
  :ring {:handler cont-examples.core/app})
