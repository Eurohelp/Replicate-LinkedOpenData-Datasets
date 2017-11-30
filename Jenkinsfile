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
def LinksSilk = "silk/accepted_links.nt"
def error = ""

node {
 current_hour = new SimpleDateFormat("HH").format(new Date())
 current_hour = Integer.parseInt(current_hour)
 try {
  stage('Remove data from blazegraph') {
   sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
  }
  stage('Checkout pipeline') {
   git branch: 'pipeline-parkingsrdfcreator', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
  }
  stage('Convert CSV to RDF') {
   def ret = sh(script: 'java -jar CSVToRDFParkings/rdfparkings.jar ' + CSVParkings + ' ' + NewCSVParkings + ' ' + RmlConfigurationFile + ' ' + RDFParkings, returnStdout: true)
   if (ret.contains("ARGUMENTS ERROR")) {
    error = "FAIL IN STAGE: Convert CSV to RDF. The error is with the arguments. Please, check them.\n"
    println error
    currentBuild.result = 'FAILURE'
   } else if (ret.contains("RML CONFIGURATION FILE SYNTAX ERROR")) {
    println error
    error = "FAIL IN STAGE: RML CONFIGURATION FILE SYNTAX ERROR. The error is with rml configuration file syntax. Please, check it.\n"
    currentBuild.result = 'FAILURE'
   }
  }
  stage('Upload RDF to blazegraph') {
   def ret = sh('curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFParkings + ' -X POST ' + CompleteGraphUri, returnStdout: true)
   if (ret.contains("modified=\"0\"")) {
    error = "WARNING IN STAGE: Upload RDF to blazegraph. The graph was not modified.\n"
    println error
    currentBuild.result = 'FAILURE'
   }
  }
  stage('RDF quality') {
   def ret = sh(script: 'java -jar rdfquality/shacl-parkings.jar ' + RDFParkings + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile, returnStdout: true)
   if (!ret.contains("Valid RDF")) {
    error = "FAIL IN STAGE: RDF quality. The RDF is not valid.\n"
    println error
    currentBuild.result = 'FAILURE'
   }
  }
  stage('Discovery links') {
   def ret = sh(script: 'java -jar silk/parkingssilkrunner.jar ' + SilkConfiguration, returnStdout: true)
   if (ret.contains("Wrote 0 links")) {
    error = "WARNING IN STAGE: Discovery links. Silk didn't discovered any link.\n"
    println error
    currentBuild.result = 'FAILURE'
   }
  }
  stage('Upload links discovered to blazegraph') {
   def ret = sh(script: 'curl -D- -H "Content-Type: text/plain" --upload-file ' + LinksSilk + ' -X POST ' + CompleteGraphUri, returnStdout: true)
   if (ret.contains('modified="0"')) {
    error = "WARNING IN STAGE: Upload links discovered to blazegraph. The graph was not modified.\n"
    println error
    currentBuild.result = 'FAILURE'
   }
  }
 } catch (err) {
  stage('Notify failure') {
   println "Se ha producido un fallo se enviara un correo notificandolo"
   mail(to: 'dmuv7@hotmail.com',
    subject: "Fallo en ${env.JOB_NAME}",
    body: "Ha fallado la ejecución de '${env.JOB_NAME}', el error se ha dado en: " + date + " y ha sido --> " + error,
    mimeType: 'text/html');
  }
 }
}