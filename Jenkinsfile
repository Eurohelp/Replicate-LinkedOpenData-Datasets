#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def SPARQLendpoint = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql"
def CSVMartutene = "CSVToRDFMartutene/data/martutene.csv"
def NewCSVMartutene = "CSVToRDFMartutene/newdata/martutene.csv"
def RmlConfigurationFile = "CSVToRDFMartutene/csvtordfconfigurationfile.ttl"
def RDFMartutene = "shacl/martutene.ttl"
def NamedGraph = "http://lod.eurohelp.es/dataset/rivers"
def CompleteGraphUri = "http://172.16.0.81:58080/blazegraph/namespace/replicate-mishel/sparql?context-uri=" + NamedGraph
def SHACLfile = "shacl/shacl-urumea-martutene.ttl"
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
            git branch: 'pipeline-urumea-martutene', url: 'https://github.com/mishel-uchuari/Replicate-LinkedOpenData-Datasets.git'
        }
        stage('Convert CSV to RDF') {
            def ret = sh(script: 'java -jar CSVToRDFMartutene/martutenerdfcreator.jar ' + CSVMartutene + ' ' + NewCSVMartutene + ' ' + RmlConfigurationFile + ' ' + RDFMartutene, returnStdout: true)
            if (ret.contains('No se ha generado RDF')) {
                sh 'exit 1'
            }
        }
        stage('Upload RDF to blazegraph') {
            def ret = sh(script: 'curl -D- -H "Content-Type: text/turtle" --upload-file ' + RDFMartutene + ' -X POST ' + CompleteGraphUri, returnStdout: true)
            if (ret.contains('modified="0"')) {
                sh 'exit 1'
            }
        }
        stage('RDF quality') {
            def ret = sh(script: 'java -jar rdfquality/shacl-martutene.jar ' + RDFMartutene + ' ' + SHACLfile + ' ' + SHACLReportCheckingQuery + ' ' + SHACLReportFile, returnStdout: true)
            if (ret.contains("Not valid RDF")) {
                sh 'exit 1'
            }
        }
        stage('Discovery links') {
            def ret = sh(script: 'java -jar silk/martutenesilkrunner.jar ' + SilkConfiguration, returnStdout: true)
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
                body: "Ha fallado la ejecuci�n de '${env.JOB_NAME}', el error se ha dado en: " + date + ". Revisa el error en http://172.16.0.81:8008/jenkins/job/Replicate-Donosti-Parkings/${env.BUILD_NUMBER}",
                mimeType: 'text/html');
            currentBuild.result = 'FAILURE'
        }
    }
}