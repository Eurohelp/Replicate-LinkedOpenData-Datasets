(ns replicate-linked-open-data-datasets.test
  (:require
   [clojure.test :refer :all]
   [grafter.rdf.repository :refer :all]
   [replicate-linked-open-data-datasets.pipeline :refer :all]
   [replicate-linked-open-data-datasets.transform :refer [integer]]
  )
)

(deftest test-parkings-nombre
  (testing "Value of Nombre column for row 0 should be string"
     (is (string?
            ; There is probably a better solution to achieve this
            (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 0) :Nombre )
         )
     )
  )
)

(deftest test-parkings-plazas-rotatorias
  (testing "Value of PlazasRotatorias column for row 1 should be integer"
     (is (integer?
            ; There is probably a better solution to achieve this
            (integer (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 1) :PlazasRotatorias ))
         )
     )
  )
)

(deftest test-parkings-plazas-residentes
  (testing "Value of PlazasResidentes column for row 3 should be integer"
     (is (integer?
            ; There is probably a better solution to achieve this
            (integer (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 3) :PlazasResidentes ))
         )
     )
  )
)

(deftest test-parkings-plazas-totales
  (testing "Value of PlazasTotales column for row 4 should be integer"
     (is (integer?
            ; There is probably a better solution to achieve this
            (integer (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 4) :PlazasTotales ))
         )
     )
  )
)

(deftest test-parkings-plazas-residentes-libres
  (testing "Value of PlazasResidentesLibres column for row 5 should be integer"
     (is (integer?
            ; There is probably a better solution to achieve this
            (integer (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 5) :PlazasResidentesLibres ))
         )
     )
  )
)

(deftest test-parkings-plazas-rotatorias-libres
  (testing "Value of PlazasRotatoriasLibres column for row 6 should be integer"
     (is (integer?
            ; There is probably a better solution to achieve this
            (integer (get (nth (:rows (convert-parkings-to-data "./data/parkings-data.csv")) 6) :PlazasRotatoriasLibres ))
         )
     )
  )
)
