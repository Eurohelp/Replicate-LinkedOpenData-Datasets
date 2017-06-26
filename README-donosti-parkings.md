`java -jar loadparkingdata/cleanparkingdata.jar "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas" donostiaparkingsclean.csv parkingsclean parkingsclean.json`

`java -jar csv2rdf/csvparkingsclean2rdf.jar donostiaparkingsclean.csv donostiaparkingsclean.rdf`
