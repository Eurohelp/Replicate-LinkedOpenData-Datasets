#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://blzg-write:8080/blazegraph/namespace/kb/sparql"
def CSVParkings = "CSVToRDFParkings/data/parkings.csv"
def NewCSVParkings = "CSVToRDFParkings/newdata/parkings.csv"
def RmlConfigurationFile = "CSVToRDFParkings/csvtordfconfigurationfile.ttl"
def RDFParkings = "shacl/parkings.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/parkings"
def CompleteGraphUri = "http://blzg-write:8080/blazegraph/namespace/kb/sparql?context-uri=http://lod.eurohelp.es/dataset/parkings"
def SHACLfile = "shacl/shacl-parkings.ttl"
def SHACLReportCheckingQuery = "shacl/query.sparql"
def SHACLReportFile = "shacl/report.ttl"
def SilkConfiguration = "silk/silk-test.xml"
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
def date = sdf.format(new Date())
def LinksSilk = "silk/accepted_links.nt"

node {
    try {
        stage('Remove data from blazegraph') {
            sh 'curl --get -X DELETE -H "Accept: application/xml" ' + SPARQLendpoint + ' --data-urlencode "?c=<' + NamedGraph + '>"'
        }
        stage('Checkout pipeline') {
            git branch: 'pipeline-parkingsrdfcreator', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
        }
        stage('Convert CSV to RDF') {
            def ret = sh(script: 'java -jar CSVToRDFParkings/parkingsrdfcreator.jar ' + CSVParkings + ' ' + NewCSVParkings + ' ' + RmlConfigurationFile + ' ' + RDFParkings, returnStdout: true)
            if (ret.contains('No se ha generado RDF')) {
                sh 'exit 1'
            }
        }
        stage('Upload RDF to blazegraph') {
            def ret = sh(script: 'curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFParkings + ' -X POST ' + CompleteGraphUri, returnStdout: true)
            if (ret.contains('modified="0"')) {
                sh 'exit 1'
            }
        }
        stage('RDF quality') {
            def ret = sh(script: 'java -jar rdfquality/shacl-parkings.jar ' + RDFParkings + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile, returnStdout: true)
            if (ret.contains("Not valid RDF")) {
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
                body: "Ha fallado la ejecución de '${env.JOB_NAME}', el error se ha dado en: " + date + ". Revisa el error en http://blz-write:8080/jenkins/job/Replicate-Donosti-Parkings/${env.BUILD_NUMBER}",
                mimeType: 'text/html');
            currentBuild.result = 'FAILURE'
        }
    }
}