(ns thomascothran.laplace.ui-components.molecule)

(defn text-field
  [{:field/keys       [label
                       name
                       help-text
                       value
                       type
                       required
                       max
                       min
                       step]
    :or               {value ""
                       type  "text"} :as m}]
  (assert (or label name))
  [:div.field
   [:label.field-label (or label name)]
   [:div.control
    [:input.input (cond-> {:type  type
                           :value value
                           :name name}
                    required (assoc :required "true")
                    max (assoc :max max)
                    min (assoc :min min)
                    step (assoc :step step))]]
   (when help-text
     [:p.help help-text])])

(defn number-field
  [m]
  (text-field (assoc m :field/type "number")))
