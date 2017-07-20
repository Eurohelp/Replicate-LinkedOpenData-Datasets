#!/usr/bin/env groovy

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/sparql"
def URLParkingsDonosti = "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas"
def CSVParkingsClean = "donostiaparkingsclean.csv"
def RDFParkingsClean = "donostiaparkingsclean.nq"
def NamedGraph = "http://lod.eurohelp.es/dataset/parkings"
def SHACLfile = "shacl/parkings.ttl"
def SHACLReportCheckingQuery = "shacl/report.sparql"
def SHACLReportFile = "shacl/report.ttl"

node {
    stage('Remove data from blazegraph'){
        sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
    }
    stage('Checkout pipeline') {
        git branch: 'RDFTests', url: 'https://github.com/Eurohelp/Replicate-LinkedOpenData-Datasets.git'
    }
    stage('Obtain and clean data') {
        sh 'java -jar loadparkingdata/cleanparkingdata.jar "' + URLParkingsDonosti + '" ' + CSVParkingsClean + ' temp_parkingsclean temp_parkingsclean.json'
    }
    stage('Convert CSV to RDF') {
         sh 'java -jar csv2rdf/csvparkingsclean2rdf.jar ' + CSVParkingsClean + ' ' + RDFParkingsClean + ' ' + NamedGraph
    }
    stage('RDF quality') {
         sh 'java -jar rdfquality/SHACLValidator.jar ' + RDFParkingsClean + ' ' + NamedGraph + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLfile
    }
    stage('Upload RDF to blazegraph') {
         sh 'curl -X POST -H Content-Type:text/x-nquads --data-binary @' + RDFParkingsClean + ' ' + SPARQLendpoint
    }
}
