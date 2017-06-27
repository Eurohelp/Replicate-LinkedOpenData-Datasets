Clonar repo:
`git clone --branch java-only-temporal-pipeline https://github.com/Eurohelp/Replicate-LinkedOpenData-Datasets.git`
`cd Replicate-LinkedOpenData-Datasets/`

Obtener y limpiar datos de parkings de Donosti:
`java -jar loadparkingdata/cleanparkingdata.jar "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas" donostiaparkingsclean.csv parkingsclean parkingsclean.json`

Convertir los datos limpios a RDF:
`java -jar csv2rdf/csvparkingsclean2rdf.jar donostiaparkingsclean.csv donostiaparkingsclean.nquads http://lod.eurohelp.es/dataset/parkings`

Subir el RDF a Blazegraph:
`curl -X POST -H 'Content-Type:text/x-nquads' --data-binary '@donostiaparkingsclean.nquads' http://172.16.0.81:58080/blazegraph/sparql`
