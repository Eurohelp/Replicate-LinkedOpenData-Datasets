#!/usr/bin/env groovy

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/sparql"
def NamedGraph = "http://lod.eurohelp.es/dataset/parkings"
def URLParkingsDonosti = "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas"
def CSVParkingsClean = "donostiaparkingsclean.csv"
def RDFParkingsClean = "donostiaparkingsclean.nquads"

node {
    stage('Clean blazegraph named graph'){
        sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
    }
    stage('Checkout pipeline') {
        git branch: 'java-only-temporal-pipeline', url: 'https://github.com/Eurohelp/Replicate-LinkedOpenData-Datasets.git'
    }
    stage('Obtain and clean data') {
        sh 'java -jar loadparkingdata/cleanparkingdata.jar "' + URLParkingsDonosti + '" ' + CSVParkingsClean + ' temp_parkingsclean temp_parkingsclean.json'
    }
    stage('Convert CSV to RDF') {
         sh 'java -jar csv2rdf/csvparkingsclean2rdf.jar ' + CSVParkingsClean + ' ' + RDFParkingsClean + ' ' + NamedGraph
    }
    stage('Upload RDF to blazegraph') {
         sh 'curl -X POST -H Content-Type:text/x-nquads --data-binary @' + RDFParkingsClean + ' ' + SPARQLendpoint
    }
}
