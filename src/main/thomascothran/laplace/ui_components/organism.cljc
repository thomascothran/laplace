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
  [#:field{:label     "Number of work items"
           :name      "work-item-no"
           :value     "1"
           :type      "number"
           :min       "1"
           :required  true
           :help-text "How many work items do you have?"}
   #:field{:label     "Minimum growth of work items"
           :value     "0"
           :min       "0"
           :name      "minimum-work-item-growth"
           :type      "number"
           :required  true
           :help-text "What is the minimal percentage of work item growth? E.g., 0% would mean no stories are added"}
   #:field {:label     "Maximum work-item growth"
            :name      "maximum-work-item-growth"
            :value     "100"
            :required  true
            :type      "number"
            :help-text "What is the maximum growth for stories? E.g., 100% story grown means you'll end with twice the stories"}
   #:field{:label     "Throughput"
           :name "throughput-dist"
           :value     "1,2,3"
           :type      "text"
           :required  true
           :help-text "A comma separated list of how many work items were complete in previous periods"}])

(defn simple-model-form
  "Our simplest model answers the question: given a group
  of tasks, how long will they take?"
  [{post-to                 :form/action
    target                  :htmx/target}]
  [:form {:hx-post post-to :hx-target target}
   (map molecule/text-field simple-model-form-fields)
   [:button.button.is-link {:type "submit"} "Submit"]])
