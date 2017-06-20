`java -jar loadparkingdata/cleanparkingdata.jar "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas" donostiaparkingsclean.csv parkingsclean parkingsclean.json`


`lein grafter run replicate-linked-open-data-datasets.pipeline/convert-parkings-to-data ./loadparkingdata/donostiparkings.csv ./loadparkingdata/grafter-donostiparkings.csv`

`lein grafter run replicate-linked-open-data-datasets.pipeline/convert-parkings-data-to-graph ./loadparkingdata/donostiparkings.csv ./output/data/donostiparkings.rdf`
