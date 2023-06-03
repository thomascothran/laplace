(ns thomascothran.laplace.routes.ui.home
  (:require [thomascothran.laplace.ui-components.organism :as organism]
            [thomascothran.laplace.routes.ui.charts :as charts]))

(defn home-get
  [_req]
  {:hiccup/page
   [:div.container.box
    (organism/simple-model-form {:form/action "/ui/charts/simple-throughput-model"
                                 :htmx/target "#simple-throughput-model-chart"})
    [:div#simple-throughput-model-chart]]})

(defn routes
  []
  ["" {:middleware [[:hiccup/page]]}
   [""  {:get home-get}]
   ["/charts"
    (charts/routes)]])
