#!/usr/bin/env groovy

node {
    stage('Checkout') {
        git branch: 'java-only-temporal-pipeline', url: 'https://github.com/Eurohelp/Replicate-LinkedOpenData-Datasets.git'
    }
    stage('Obtain and cleandata') {
         echo 'Obtain and cleandata'
    }
    stage('Convert CSV to RDF') {
         echo 'Convert CSV to RDF'
    }
    stage('Upload RDF to blazegraph') {
         echo 'Upload RDF to blazegraph'
    }
}
