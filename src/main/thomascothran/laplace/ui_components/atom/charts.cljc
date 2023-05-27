(ns thomascothran.laplace.ui-components.atom.charts
  (:require [jsonista.core :as j]))

(defn random-alphabetical [len]
  (let [chars (->> (range (int \a) (+ 26 (int \a)))
                   (map char)
                   (into []))]
    (->> (take len (repeatedly #(rand-nth chars)))
         (reduce (fn [prev curr] (str prev curr)) ""))))

(defn- embedded-vega-lite-chart
  [{:keys [:chart/id
           :chart/spec-name
           :vega-lite/spec]
    :or {id (random-alphabetical 20)
         spec-name (random-alphabetical 20)}}]
  [:div
   [:script {:type "text/javascript"}
    (format "var %s = %s; vegaEmbed('#%s', %s)"
            spec-name (j/write-value-as-string spec) id spec-name)]
   [:div {:id id}]])

(defn line-schema
  [{:keys [:chart/x-field :chart/y-field :chart/x-field-title :chart/y-field-title
           :chart/values :chart/height :chart/width]
    :or {height 600 width 800} :as m}]
  {"$schema"  "https://vega.github.io/schema/vega-lite/v5.json",
   "data"     {"values" values}
   "width" width
   "height" height
   "encoding" {"order" {"value" 0},
               "x"     {"field" x-field, "title" x-field-title, "type" "quantitative"},
               "y"     {"field" y-field,
                        "title" y-field-title
                        "type"  "quantitative"}},
   "mark"     {"type" "line", "tooltip" true}})

(defn line-chart
  "Return a line chart with x and y starting at 0.

  Params
  ------
  - `:chart/x-field` The name of the x field in the chart/values
  - `:chart/y-field` The name of the y field in the chart/values
  - `:chart/x-field-title`: The title of the x field to be displayed
  - `:chart/y-field-title`: The title of the y field to be displayed
  - `:chart/values`: a sequence of maps with the x and y values

  Example
  -------

  ```
  (line-chart #:chart {:x-field 'time', :x-field-title 'Time',
                       :y-field 'value' :y-field-title 'Value'
                       :values [{:time 10, :value 5},
                                {:time 20, :value 4},
                                {:time 30, :value 2}]})
  ```
  "
  [{:keys [:chart/x-field :chart/y-field
           :chart/x-field-title :chart/y-field-title
           :chart/values] :as m}]
  (assert (and x-field y-field x-field-title y-field-title values))
  (embedded-vega-lite-chart {:vega-lite/spec (line-schema m)}))

(def bar-schema
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :description "A simple bar chart with embedded data."
   :encoding {:x {:axis {:labelAngle 0} :field "x" :type "quantitative"}
              :y {:field "y" :type "quantitative"}}
   :mark "bar"})


(defn bar-chart
  [{vs :chart/values}]
  (let [vl-schema (-> (assoc-in bar-schema [:data :values] vs)
                      j/write-value-as-string)]
    [:div
     [:script {:type "text/javascript"}
      (str "var vlSpec = "  vl-schema  ";\n"
           "vegaEmbed('#this-bar-chart', vlSpec);")]
     [:div#this-bar-chart]]))
