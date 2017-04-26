(ns replicate-linked-open-data-datasets.transform
  (:require
    [clojure.string :as st]
    [replicate-linked-open-data-datasets.prefix :refer :all]
  )
)

;;; You can specify transformation functions in this namespace for use
;;; within the pipeline.

(defn integer [s]
  (Integer/parseInt s)
)

;Convertidor a Float o Integer
(defmulti parseValue class)
(defmethod parseValue :default            [x] x)
(defmethod parseValue nil                 [x] nil)
(defmethod parseValue java.lang.Character [x] (Character/getNumericValue x))
(defmethod parseValue java.lang.String    [x] (if (= "" x)
                                                nil
                                                (if (.contains x ".")
                                                  (Double/parseDouble x)
                                                  (Integer/parseInt x)
                                                )
                                              ))

(defn urlify [sr]
  (st/replace (st/trim sr) #"\(|\)|\s|\/|\." "-")
)

;; PARKINGS
;Metodos que hacen split a un string determinado
;; Tiempo
(defn makeSplitTiempo [string]
  (def pepi (st/split string #";"))
  (def valor (st/split (nth pepi 0) #" "))
  (nth valor 0)
)

;; Precio
(defn makeSplitPrecio [string]
  ; Condicion para comprobar si el campo de precio contiene o no datos
  ;(if-not (.contains string "-")
  ;  (
      (def pepi (st/split string #";"))
      (def valor (st/split (nth pepi 0) #":"))
      (get valor 1)
  ;    (println valor)
  ;  )
  ;  (println "Else")
  ;)
)

(defn parkings-uri [a b]
  (base-id
    (str "Parkings/" (urlify
        (str a "-" b)
      )
    )
  )
)

(defn parkings-fares-uri [a]
  (base-id
    (str "Parkings/fares/" (urlify a))
  )
)

;; Tarifas
(defn fare1-uri [nombre]
  ;; Aqui deberia de llegar el 'precio-tiempo' en vez de el nombre del parking
  (base-id
    ; Hay que cambiar el segundo 'nombre' por 'precio-tiempo'
    ;(str "Parkings/fares/" (urlify nombre) "/fare1/" (urlify preciotiempo))
    (str "Parkings/fares/" (urlify nombre) "/fare1/" (urlify nombre))
  )
)

(defn fare2-uri [nombre]
  ;; Aqui deberia de llegar el 'precio-tiempo' en vez de el nombre del parking
  (base-id
    ; Hay que cambiar el segundo 'nombre' por 'precio-tiempo'
    ;(str "Parkings/fares/" (urlify nombre) "/fare2/" (urlify preciotiempo))
    (str "Parkings/fares/" (urlify nombre) "/fare2/" (urlify nombre))
  )
)

(defn fares-price1-uri [nombre]
  (base-id
    (str "Parkings/fares/" (urlify nombre) "/fare1/Price")
  )
)

(defn fares-price2-uri [nombre]
  (base-id
    (str "Parkings/fares/" (urlify nombre) "/fare2/Price")
  )
)

(defn fares-time1-uri [nombre]
  (base-id
    (str "Parkings/fares/" (urlify nombre) "/fare1/Time")
  )
)

(defn fares-time2-uri [nombre]
  (base-id
    (str "Parkings/fares/" (urlify nombre) "/fare2/Time")
  )
)

(def plazas-rotatorias (base-domain "/property/plazas-rotatorias"))
(def plazas-residentes (base-domain "/property/plazas-residentes"))
(def plazas-totales (base-domain "/property/plazas-totales"))
(def plazas-residentes-libres (base-domain "/property/plazas-residentes-libres"))
(def plazas-rotatorias-libres (base-domain "/property/plazas-rotatorias-libres"))
