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
