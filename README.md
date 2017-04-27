# Replicate Linked Open Data Datasets

## Datasets

| FILE | URL | DATE |
| --- | --- | --- |
| parkings | - | 20/04/2017 |

## Usage

Pipeline name will always be one of the names in the above list. Files containing data will also start it's name with the name of the above datasets. The same will apply to the transformations functions itself.

Below, you will find clear examples of usage, having to replace 'PROJECTNAME' with one of the datasets names ;)

### Leiningen

`lein grafter run replicate-linked-open-data-datasets.pipeline/convert-PROJECTNAME-to-data ./data/PROJECTNAME-data.csv ./output/data/PROJECTNAME-data.csv`

`lein grafter run replicate-linked-open-data-datasets.pipeline/convert-PROJECTNAME-data-to-graph ./data/PROJECTNAME-data.csv ./output/data/PROJECTNAME-data.rdf`

### Tests

If you want to execute the tests you could do it with the following command:

`lein test`

### SPARQL Queries

This code will run a query over the .rdf file you specify. You can also select a file from where queries will be loaded. These files are located under 'queries' folder.

You just need to copy the name of the .rdf file and replace 'FILENAME' with one of the above datasets names, and for specifying the query file, you will need to replace 'QUERYFILE' with one of the file names you can find under 'queries' folder (without the extension, just the name).

`lein repl`

`(use 'replicate-linked-open-data-datasets.querytest)`

`(def triples (create-triple-store))`

`(.isEmpty (.getConnection triples))`

`(insert-triples triples (java.io.File. "output/data/FILENAME-data.rdf"))`

`(query-result-set triples (with-common-ns-prefixes (from-file "QUERYFILE")))`

`(clear-store triples)`

You will get an output like this one:
![Query Test Output](query-test-output.png?raw=true)
