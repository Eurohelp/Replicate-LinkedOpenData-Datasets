#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql"
def RmlConfigurationFile = "JsonToRDFCalidadAire/csvtordfconfigurationfile.ttl"
def RDFCalidadAire = "all_environment_quality20017.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/calidad-aire"
def CompleteGraphUri = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql?context-uri=" + NamedGraph
def SHACLfile = "shacl/shacl-calidad-aire.ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/report.ttl"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())
def MongoDataBase= "dashboard"
def MongoCollection = "iot_environment_measurements"
def PatternToFind = "all:all:environment_airquality:201709:all"
def PathToStoreJson = "JsonToRDFCalidadDelAire/data/enviroment_airquality_201709.json"
def JsonCalidadAire = "JsonToRDFCalidadDelAire/data/enviroment_airquality_201709-0.json"


node {
    try {
     	stage('Remove data from blazegraph'){
        	sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
    	}
        stage('Checkout pipeline') {
            git branch: 'pipeline-calidaddelaire', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
        }
        stage('Get data from MongoDB') {
			def ret = sh(script: 'java -jar JsonToRDFCalidadAire/get-data.jar ' + MongoDataBase +  + ' ' + MongoCollection + ' ' + PatternToFind + ' ' + PathToStoreJson, returnStdout: true)
            if (ret.contains('No se encuentran datos con ese patron')) {
                sh 'exit 1'
            }
        }      
        stage('Convert CSV to RDF') {
            def ret = sh(script: 'java -jar JsonToRDFCalidadAire/calidaddelairerdfcreator.jar ' + JsonCalidadAire  + RmlConfigurationFile + ' ' + RDFCalidadAire, returnStdout: true)
            if (ret.contains('No se ha generado RDF')) {
                sh 'exit 1'
            }
        }
        stage('Upload RDF to blazegraph') {
            def ret = sh(script: 'curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFCalidadAire + ' -X POST ' + CompleteGraphUri, returnStdout: true)
            if (ret.contains('modified="0"')) {
                sh 'exit 1'
            }
        }
        stage('RDF quality') {
            def ret = sh(script: 'java -jar rdfquality/shacl-calidad-aire.jar ' + RDFCalidadAire + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile, returnStdout: true)
            if (ret.contains("Not valid RDF")) {
                sh 'exit 1'
            }
        }
    } catch (err) {
        stage('Notify failure') {
            println "Se ha producido un fallo se enviara un correo notificandolo"
            mail(to: 'dmuv7@hotmail.com',
                subject: "Fallo en ${env.JOB_NAME}",
                body: "Ha fallado la ejecucion de '${env.JOB_NAME}', el error se ha dado en: " + date + ". Revisa el error en http://172.16.0.81:8008/jenkins/job/Replicate-Donosti-Parkings/${env.BUILD_NUMBER}",
                mimeType: 'text/html');
            currentBuild.result = 'FAILURE'
        }
    }
}