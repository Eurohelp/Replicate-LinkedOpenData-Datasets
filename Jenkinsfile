#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql"
def CSVParkings = "CSVToRDFParkings/data/parkings.csv"
def NewCSVParkings = "CSVToRDFParkings/newdata/parkings.csv"
def RmlConfigurationFile = "CSVToRDFParkings/csvtordfconfigurationfile.ttl"
def RDFParkings = "shacl/parkings.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/parkings"
def CompleteGraphUri = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql?context-uri=http://lod.eurohelp.es/dataset/parkings"
def SHACLfile = "shacl/shacl-parkings.ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/report.ttl"
def SilkConfiguration = "silk/silk-test.xml"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())
def result = manager.build.result
def LinksSilk = "silk/accepted_links.nt"

node {
  current_hour = new SimpleDateFormat("HH").format(new Date())
  current_hour = Integer.parseInt(current_hour)
 try {
   stage('Remove data from blazegraph'){
        sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
    }
   stage('Checkout pipeline') {
   git branch: 'pipeline-parkingsrdfcreator', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
  }
   stage('Convert CSV to RDF') {
   sh 'java -jar CSVToRDFParkings/parkingsrdfcreator.jar ' + CSVParkings + ' ' + NewCSVParkings + ' ' + RmlConfigurationFile + ' ' + RDFParkings
   manager.listener.logger.println "And the result is: ${result}"
  }
  stage('Upload RDF to blazegraph') {  
   sh 'curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFParkings + ' -X POST '+ CompleteGraphUri
  }
  stage('RDF quality') {
   sh 'java -jar rdfquality/shacl-parkings.jar ' + RDFParkings + ' '  + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile
  }
  stage('Discovery links') {
   sh 'java -jar silk/parkingssilkrunner.jar ' + SilkConfiguration
  }
  stage('Upload links discovered to blazegraph') {
   sh 'curl -D- -H "Content-Type: text/plain" --upload-file ' + LinksSilk +' -X POST '+ CompleteGraphUri
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