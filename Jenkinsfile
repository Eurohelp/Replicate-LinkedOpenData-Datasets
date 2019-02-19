#!/usr/bin/env groovy

import java.text.SimpleDateFormat
// Entorno (local,test y para produccion en blanco
def entornoEjecu=""
def entornoBlaze=""
def entorno=""
switch (entornoEjecu) {
    case "Local":
        entornoBlaze = "localhost:8080/blazegraph"
		entorno = "localhost:8080"
        break;
    case "Test":
        entornoBlaze = "blzg-write:8080/blazegraph"
		entorno = "test.eurohelp.es"
		break;
    default:
        entornoBlaze = "blzg-write:8080/blazegraph"
		entorno = "donostia.eus"
}
 println "Los entornos seran"
 println "------------------"
 println "entornoBlaze: "+entornoBlaze
 println "Entorno: "+entorno
 println "Entorno ejecucion: "+entornoEjecu

def NameSpace="bklparkings"
def CSVBKLParkings = "CSVToRDFBKLParkings/data/BKL_dBizi_usuarios_por_mes_estacion.csv"
def NewCSVBKLParkings = "CSVToRDFBKLParkings/newdata/BKL_dBizi_usuarios_por_mes_estacion.csv"
def RmlConfigurationFile = "CSVToRDFBKLParkings/csvtordfconfigurationfile"+entornoEjecu+".ttl"
def RDFBKLParkings = "shacl/"+NameSpace+entornoEjecu+".ttl"
def NamedGraph = "http://"+entorno+"/linkeddata/graph/bklparkings"
def CompleteGraphUri = "http://"+entornoBlaze+"/namespace/kb/sparql?context-uri=" + NamedGraph
def SHACLfile = "shacl/shacl-"+NameSpace+entornoEjecu+".ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/"+NameSpace+"report"+entornoEjecu+".ttl"
def SilkConfiguration = "silk/silk-test"+entornoEjecu+".xml"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())
def LinksSilk = "silk/accepted_links.nt"

node {
    try {
        stage('Checkout pipeline') {
            git branch: 'feature-pipeline-bikeracks', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
        }
        
        stage('Convert CSV to RDF') {
             def ret = sh(script: 'java -jar CSVToRDFBKLParkings/bklparkingsrdfcreator.jar ' + CSVBKLParkings + ' ' + NewCSVBKLParkings + ' ' + RmlConfigurationFile + ' ' + RDFBKLParkings+ ' ' + entorno, returnStdout: true)
            if (ret.contains('No se ha generado RDF')) {
                sh 'exit 1'
            }
        }
        stage('RDF quality') {
            def ret = sh(script: 'java -jar rdfquality/shacl-parkings.jar ' + RDFBKLParkings + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile, returnStdout: true)
            if (ret.contains("Not valid RDF")) {
                sh 'exit 1'
            }
        }
        stage('Upload RDF to blazegraph') {
            def ret = sh(script: 'curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFBKLParkings + ' -X POST ' + CompleteGraphUri, returnStdout: true)
            if (ret.contains('modified="0"')) {
                sh 'exit 1'
            }
        }
        stage('Discovery links') {
            def ret = sh(script: 'java -jar silk/parkingssilkrunner.jar ' + SilkConfiguration, returnStdout: true)
            if (ret.contains("Wrote 0 links")) {
                sh 'exit 1'
            }
        }
        stage('Upload links discovered to blazegraph') {
            def ret = sh(script: 'curl -D- -H "Content-Type: text/plain" --upload-file ' + LinksSilk + ' -X POST ' + CompleteGraphUri, returnStdout: true)
            if (ret.contains('modified="0"')) {
                sh 'exit 1'
            }
        }
    } catch (err) {
        stage('Notify failure') {
            println "Se ha producido un fallo se enviara un correo notificandolo"
            mail(to: 'dmuv7@hotmail.com',
                subject: "Fallo en ${env.JOB_NAME}",
                body: "Ha fallado la ejecución de '${env.JOB_NAME}', el error se ha dado en: " + date + ". Revisa el error en http://"+entorno+"/jenkins/job/Replicate-Pipeline-BikeRack/${env.BUILD_NUMBER}",
                mimeType: 'text/html');
            currentBuild.result = 'FAILURE'
        }
    }
}