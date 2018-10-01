package uk.ac.ebi.intact.search.interactions.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * @author Elisabet Barrera
 */
@Configuration
@EnableSolrRepositories(basePackages = "uk.ac.ebi.intact.search.interactions.repository",
        schemaCreationSupport = true)
@ComponentScan(basePackages = {"uk.ac.ebi.intact.search.interactions.service"})
@SpringBootApplication
public class SolrTestConfiguration {

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient("http://localhost:8983/solr");
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

}
