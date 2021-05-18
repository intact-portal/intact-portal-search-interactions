# intact-search-interactions
Web Services for accessing Interaction details from solr index.

Has Following end points:

* /interactions/findInteractions/queryString
* /interactions/findInteractionWithFacet/queryString?species=?&interactionTypesFilter=?&detectionMethods=?&hostOrganisms=?&negativeFilter=?&minMIScore=?&maxMIScore=?&intraSpeciesFilter=?&page=?&pageSize=?
  (all the parameters are optional)

## Prerequisites

1. Running instance of solr 7.3.1 with interactions core created and indexed (Details below)
2. [jdk 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Step for pointing the application to an running instance of solr for eg. [http://example/solr]
```
1. Open intact-search-interactions/src/main/resources/application.properties
   a. Update 'spring.data.solr.host' property with your running instance of solr 'http://example/solr'
```

## Quickstart

```
1. cd intact-portal-indexer
2. mvn clean compile
3. Run /home/anjali/intellij_projects/intact-search-interactions/src/main/java/uk/ac/ebi/intact/search/interactions/InteractionsApplication.java
4. Access endpoints at http://localhost:8080
```
 
## Running the tests

```
1. Execute : mvn test

```

