#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql"
def CSVUrumea = "CSVToRDFUrumea/data/txominea.csv"
def NewCSVUrumea = "CSVToRDFUrumea/newdata/txominea.csv"
def RmlConfigurationFile = "CSVToRDFUrumea/csvtordfconfigurationfile.ttl"
def RDFUrumea = "shacl/urumea.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/rivers"
def SHACLfile = "shacl/shacl-urumea.ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/report.ttl"
def SilkConfiguration = "silk/silk-test.xml"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())
def LinksSilk = "silk/accepted_links.nt"

node {
  current_hour = new SimpleDateFormat("HH").format(new Date())
  current_hour = Integer.parseInt(current_hour)
 try {
  stage('Checkout pipeline') {
   git branch: 'pipeline-urumeardfcreator', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
  }
  stage('Convert CSV to RDF') {
   sh 'java -jar CSVToRDFUrumea/urumeardfcreator.jar ' + CSVUrumea + ' ' + NewCSVUrumea + ' ' + RmlConfigurationFile + ' ' + RDFUrumea
  }
  stage('RDF quality') {
   sh 'java -jar rdfquality/shacl-urumea.jar ' + RDFUrumea + ' '  + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile
  }
  stage('Discovery links') {
   sh 'java -jar silk/urumeasilkrunner.jar ' + SilkConfiguration
  }
  stage('Upload RDF to blazegraph') {
  sh 'curl -D- -H Content-Type: text/turtle --upload-file ' + RDFUrumea + ' -X POST ' + SPARQLendpoint + '?context-uri=' + NamedGraph
  // Se añaden tambien los enlaces descubiertos
  sh 'curl -D- -H Content-Type: text/plain --upload-file ' + LinksSilk + ' -X POST ' + SPARQLendpoint + '?context-uri=' + NamedGraph
  
  }
 } catch (err) {
  stage('Notify failure') {
	println "Se ha producido un fallo se enviara un correo notificandolo"
  	mail(to: 'dmuv7@hotmail.com',
    subject: "Fallo en ${env.JOB_NAME}",
    body: "Ha fallado la ejecuciÃ³n de '${env.JOB_NAME}', el error se ha dado en: " + date + " y ha sido --> " + err.toString(),
    mimeType: 'text/html');
  }
 }
}