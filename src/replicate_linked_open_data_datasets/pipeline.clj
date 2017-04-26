(ns replicate-linked-open-data-datasets.pipeline
    (:require
     [grafter.tabular :refer [_ add-column add-columns apply-columns
                              build-lookup-table column-names columns
                              derive-column drop-rows graph-fn grep make-dataset
                              mapc melt move-first-row-to-header read-dataset
                              read-datasets rows swap swap take-rows
                              test-dataset test-dataset]]
     [grafter.rdf]
     [grafter.rdf.io]
     [grafter.rdf :refer [s]]
     [grafter.rdf.protocols :refer [->Quad]]
     [grafter.rdf.protocols :refer [ITripleWriteable]]
     [grafter.rdf.templater :refer [graph]]
     [grafter.rdf.io :refer [rdf-serializer]]
     [grafter.rdf.formats :refer [rdf-nquads rdf-turtle]]
     [grafter.pipeline :refer [declare-pipeline]]
     [grafter.vocabularies.qb :refer :all]
     [grafter.vocabularies.rdf :refer :all]
     [replicate-linked-open-data-datasets.prefix :refer :all]
     [replicate-linked-open-data-datasets.transform :refer :all]))

;; Declare our graph template which will destructure each row and
;; convert it into an RDF graph.  This will be the final step in our
;; pipeline definition.

;; PARKINGS
(def make-parkings-graph
  (graph-fn [{:keys [Nombre PlazasRotatorias Tipo PlazasResidentes PlazasTotales PlazasResidentesLibres
                     PlazasRotatoriasLibres Precios Latitud Longitud parkings-uri parkings-fares-uri
                     ;; Tarifas
                     fare1-uri fare2-uri fares-price1-uri fares-time1-uri fares-price2-uri fares-time2-uri
                     ;; Costes
                     Precios-Coste Precios-Tiempo] :as row}]
            (graph (base-graph "Parkings")
                   [parkings-uri
                      [rdf:a "http://dbpedia.org/resource/Category:Parking"]
                      ["http://dbpedia.org/resource/Category:Parking" (s Nombre)]
                      [plazas-rotatorias (integer PlazasRotatorias)]
                      ["http://dbpedia.org/ontology/type" (s Tipo)]
                      [plazas-residentes (integer PlazasResidentes)]
                      [plazas-totales (integer PlazasTotales)]
                      [plazas-residentes-libres (integer PlazasResidentesLibres)]
                      [plazas-rotatorias-libres (integer PlazasRotatoriasLibres)]
                      ["http://dbpedia.org/resource/Fare" parkings-fares-uri]
                      ["http://www.w3.org/2003/01/geo/wgs84_pos#lat" Latitud]
                      ["http://www.w3.org/2003/01/geo/wgs84_pos#long" Longitud]
                   ]
                   [parkings-fares-uri
                      ["https://github.com/Eurohelp/fare1" fare1-uri]
                      ["https://github.com/Eurohelp/fare2" fare2-uri]
                   ]
                   [fare1-uri
                      ["http://dbpedia.org/ontology/price" fares-price1-uri]
                      ["http://dbpedia.org/resource/Time" fares-time1-uri]
                   ]
                   [fare2-uri
                      ["http://dbpedia.org/ontology/price" fares-price2-uri]
                      ["http://dbpedia.org/resource/Time" fares-time2-uri]
                   ]
                   [fares-price1-uri
                      ["http://dbpedia.org/ontology/price" (s Precios-Coste)]
                      ["http://dbpedia.org/ontology/Currency" "http://dbpedia.org/resource/Euro"]
                   ]
                   [fares-time1-uri
                      ["http://dbpedia.org/resource/Time" (s Precios-Tiempo)]
                      ["http://dbpedia.org/resource/Category:Units_of_time" "http://dbpedia.org/resource/Minute"]
                   ]
                   [fares-price2-uri
                      ["http://dbpedia.org/ontology/price" (s Precios-Coste)]
                      ["http://dbpedia.org/ontology/Currency" "http://dbpedia.org/resource/Euro"]
                   ]
                   [fares-time2-uri
                      ["http://dbpedia.org/resource/Time" (s Precios-Tiempo)]
                      ["http://dbpedia.org/resource/Category:Units_of_time" "http://dbpedia.org/resource/Minute"]
                   ]
            )
  )
)

;; Declare a pipe so the plugin can find and run it.  It's just a
;; function from Datasetable -> Dataset.

;; PARKINGS
(defn convert-parkings-to-data
  "Pipeline to convert tabular parkings data into a different tabular format."
  [data-file]
  (-> (read-dataset data-file)
      (make-dataset move-first-row-to-header)
      (make-dataset [:Nombre :PlazasRotatorias :Tipo :PlazasResidentes :PlazasTotales
                     :PlazasResidentesLibres :PlazasRotatoriasLibres :Precios :Latitud :Longitud])
      (derive-column :Precios-Coste :Precios)
      (derive-column :Precios-Tiempo :Precios)
      ;; Cuenta el numero de filas
      ;(apply-columns {:row-number
      ;                  (fn [_]
      ;                    (grafter.sequences/integers-from 0)
      ;                  )
      ;               }
      ;)
      (mapc {
              ;; Tarifas
              :Precios-Coste makeSplitPrecio
              :Precios-Tiempo makeSplitTiempo
              ;; Geoposicionamiento
              :Latitud parseValue
              :Longitud parseValue
            }
      )
      (derive-column :parkings-uri [:Nombre :Tipo] parkings-uri)
      (derive-column :parkings-fares-uri [:Nombre] parkings-fares-uri)
      ;; Tarifas
      (derive-column :fare1-uri [:Nombre] fare1-uri)
      (derive-column :fare2-uri [:Nombre] fare2-uri)
      (derive-column :fares-price1-uri [:Nombre] fares-price1-uri)
      (derive-column :fares-time1-uri [:Nombre] fares-time1-uri)
      (derive-column :fares-price2-uri [:Nombre] fares-price2-uri)
      (derive-column :fares-time2-uri [:Nombre] fares-time2-uri)
  )
)

(defn convert-parkings-data-to-graph
  "Pipeline to convert the tabular parkings data sheet into graph data."
  [dataset]
  (-> dataset convert-parkings-to-data make-parkings-graph))

(declare-pipeline convert-parkings-to-data [Dataset -> Dataset]
                  {data-file "A data file"})

(declare-pipeline convert-parkings-data-to-graph [Dataset -> (Seq Statement)]
                  {dataset "The data file to convert into a graph."})
