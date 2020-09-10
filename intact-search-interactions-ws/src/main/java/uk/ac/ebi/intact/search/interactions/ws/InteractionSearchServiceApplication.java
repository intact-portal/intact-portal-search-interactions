package uk.ac.ebi.intact.search.interactions.ws;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@EnableSolrRepositories(basePackages = "uk.ac.ebi.intact.search",
        schemaCreationSupport = true)
@SpringBootApplication(scanBasePackages = "uk.ac.ebi.intact.search")
public class InteractionSearchServiceApplication extends SpringBootServletInitializer {

    @Value("${spring.data.solr.host}")
    private String solrHost;

    public static void main(String[] args) {
        SpringApplication.run(InteractionSearchServiceApplication.class, args);
    }

    //This enables the option to pack the app as a .war for external tomcats
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InteractionSearchServiceApplication.class);
    }

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder(solrHost).build();
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrClient());
    }

    /*
     * Hack for https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
     * */
    @Bean
    public boolean isEmbeddedSolr() {
        return false;
    }
}
