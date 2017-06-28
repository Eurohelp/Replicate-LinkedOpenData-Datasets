#!/usr/bin/env groovy

// Def vars

node {
    stage('Checkout') {
        echo '>>> Checkout pipeline from GitHub'
        git branch: 'java-only-temporal-pipeline', url: 'https://github.com/Eurohelp/Replicate-LinkedOpenData-Datasets.git'
    }
    stage('Obtain and cleandata') {
        echo '>>> Obtain and cleandata'
        sh 'java -jar loadparkingdata/cleanparkingdata.jar "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas" donostiaparkingsclean.csv parkingsclean parkingsclean.json'
    }
    stage('Convert CSV to RDF') {
         echo '>>> Convert CSV to RDF'
         sh 'java -jar csv2rdf/csvparkingsclean2rdf.jar donostiaparkingsclean.csv donostiaparkingsclean.nquads http://lod.eurohelp.es/dataset/parkings'
    }
    stage('Upload RDF to blazegraph') {
         echo '>>> Upload RDF to blazegraph'
         sh 'curl -X POST -H Content-Type:text/x-nquads --data-binary @donostiaparkingsclean.nquads http://172.16.0.81:58080/blazegraph/sparql'
    }
}
