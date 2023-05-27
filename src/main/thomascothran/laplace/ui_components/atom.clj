(ns thomascothran.laplace.ui-components.atom)

(def ^:private -svg-defaults
  {:width 24
   :height 24
   :fill "none"
   :stroke "currentColor"
   :stroke-width 2
   :stroke-linecap "round"
   :stroke-linejoin "round"})

(defn feather-ico
  [icon-name & [opts]]
  [:svg (merge -svg-defaults opts)
   [:use {:href (str "/assets/icons/feather-sprite.svg#" icon-name)}]])
