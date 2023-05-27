(ns thomascothran.laplace.ui-components.template
  (:require [thomascothran.laplace.ui-components.organism :as organism]))

(defn base
  "Base template"
  [body & [{:keys [title]
            :or {title "Laplace"}}]]
  [:html
   [:head
    [:title title]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href "/assets/css/bulma.min.css"}]
    (for [js ["vega_5.25.0.js"  "vega_lite_5.9.1.js" "vega_embed_6.22.1.js" "htmx_1.9.2.js"]]
      [:script {:src (str "/assets/js/" js)}])]
   body])


(defn page
  "Return hiccup for the default page layout"
  [content & [{:keys [page/title]}]]
  (let [body [:body.page-columns
              (organism/top-nav)
              [:section content]]]
    (base body {:title title})))
