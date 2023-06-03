(ns thomascothran.laplace.ui-components.organism
  (:require [thomascothran.laplace.ui-components.atom :as atom]
            [thomascothran.laplace.ui-components.molecule :as molecule]))

(defn top-nav
  []
  [:nav#navbar.navbar.is-primary {:role "navigation"}
   [:div.navbar-brand
    [:a.navbar-item {:href "/"}
     (atom/feather-ico "cpu")
     [:h2.h2.is-size-5.ml-2.has-text-weight-semibold "Latour"]]
    [:a.navbar-item {:href "/"}]]])

(def ^:private
  simple-model-form-fields
  [#:field{:label    "Minimum number of work items"
           :name     "min-work-item-no"
           :value    "1"
           :type     "number"
           :min      "1"
           :required true}
   #:field{:label     "Maximum number of work items"
           :min       "1"
           :name      "max-work-item-no"
           :type      "number"
           :required  true
           :help-text "What is the maximum number of work items you could end up with?"}

   #:field{:label     "Throughput"
           :name "throughput-dist"
           :value     "1,2,3"
           :type      "text"
           :required  true
           :help-text "A comma separated list of how many work items were complete in previous periods"}])

(defn simple-model-form
  "Our simplest model answers the question: given a group
  of tasks, how long will they take?"
  [{post-to :form/action
    target  :htmx/target}]
  [:form {:hx-post post-to :hx-target target}
   (map molecule/text-field simple-model-form-fields)
   [:div.field.is-grouped.is-grouped-right
    [:p.control
     [:button.button.is-link {:type "submit"} "Submit"]]]])
