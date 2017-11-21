#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishell/sparql"
def CSVUrumea = "CSVToRDFUrumea/data/txominea.csv"
def RmlConfigurationFile = "CSVToRDFUrumea/csvtordfconfigurationfile.ttl"
def RDFUrumea = "shacl/urumea.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/rivers"
def SHACLfile = "shacl/shacl-urumea.ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/report.ttl"
def SilkConfiguration = "silk-test.xml"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())

node {
  current_hour = new SimpleDateFormat("HH").format(new Date())
  current_hour = Integer.parseInt(current_hour)
 try {
  stage('Checkout pipeline') {
   git branch: 'pipeline-urumeardfcreator', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
  }
  stage('Convert CSV to RDF') {
   sh 'java -jar CSVToRDFUrumea/urumeardfcreator.jar ' + CSVUrumea + ' ' + RmlConfigurationFile + ' ' + RDFUrumea
  }
  stage('RDF quality') {
   sh 'java -jar rdfquality/shacl-urumea.jar ' + RDFUrumea + ' '  + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile
  }
  stage('Discovery links') {
   sh 'java -jar silk/silkrunner.jar ' + SilkConfiguration
  }
  stage('Upload RDF to blazegraph') {
   sh 'curl -X POST -H Content-Type:text/x-nquads --data-binary @' + RDFUrumea + ' ' + SPARQLendpoint
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