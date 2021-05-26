package uk.ac.ebi.intact.search.interactions.ws.controller;


import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.ac.ebi.intact.search.interactions.ws.RequiresSolrServer;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by anjali on 26/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InteractionSearchControllerTest {

    @ClassRule
    public static RequiresSolrServer requiresRunningServer = RequiresSolrServer.onLocalhost();

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String wsContextPath;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Ignore("requires solr running in localhost")
    public void postRequestTest(){

        String url = "http://localhost:" + port + wsContextPath + "/findInteractionWithFacet";

        // Request parameters and other properties.
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("query", "EBI-724102");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        //Execute and get the response.
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Expect Ok
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        String body = response.getBody();
        System.out.println(body);

    }
}