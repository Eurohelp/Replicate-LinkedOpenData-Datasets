#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishell/sparql"
def URLParkingsDonosti = "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas"
def CSVParkingsClean = "donostiaparkingsclean.csv"
def RDFParkingsClean = "donostiaparking.nq"
def NamedGraph = "http://lod.eurohelp.es/dataset/parkings"
def SHACLfile = "shacl/parkings.ttl"
def SHACLReportCheckingQuery = "shacl/report.sparql"
def SHACLReportFile = "shacl/report.ttl"
def SilkConfiguration = "silk-test.xml"
sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())

node {
  current_hour = new SimpleDateFormat("HH").format(new Date())
  current_hour = Integer.parseInt(current_hour)
 try {
  stage('Remove data from blazegraph') {
   sh 'curl --get -X DELETE -H "Accept: application/xml' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
  }
  stage('Checkout pipeline') {
   git branch: 'feature-silk', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
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
  stage('Discovery links') {
   sh 'java -jar silk/silkrunner.jar ' + SilkConfiguration
  }
  stage('Upload RDF to blazegraph') {
   sh 'curl -X POST -H Content-Type:text/x-nquads --data-binary @' + RDFParkingsClean + ' ' + SPARQLendpoint
  }
 } catch (err) {
  stage('Notify failure') {
	println "Se ha producido un fallo se enviara un correo notificandolo"
   mail(to: 'dmuv7@hotmail.com',
    subject: "Fallo en ${env.JOB_NAME}",
    body: "Ha fallado la ejecución de '${env.JOB_NAME}', el error se ha dado en: " + date + " y ha sido --> " + err.toString(),
    mimeType: 'text/html');
  }
 }
}