(ns thomascothran.laplace.ui-components.template)

(defn base
  "Base template"
  [body & [{:keys [title]
            :or {title "Laplace"}}]]
  [:html
   [:head
    [:title title]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href "/assets/css/bulma.min.css"}]]
   [:body body]])


;; (defn page
;;   "Default page layout"
;;   [{:keys [page/content]}]
;;   )
