package uk.ac.ebi.intact.search.interactions.ws.controller;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import uk.ac.ebi.intact.search.interactions.ws.RequiresSolrServer;

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
    @Autowired
    private InteractionSearchController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contexLoads() {
        assertNotNull(controller);
    }

    @Test
    public void uploadBatchFileAndSearchFunctionality() {

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("batchfiles/2_interactors.txt"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity =
                new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
        String uploadFileUrl = "http://localhost:" + port + "/intact/ws/interaction/uploadBatchFile";

        ResponseEntity<String> response = restTemplate.exchange(uploadFileUrl, HttpMethod.POST, entity, String.class, "");

        // Expect Ok
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject jsonResponse = null;
        try {
            JSONParser parser = new JSONParser();
            jsonResponse = (JSONObject) parser.parse(response.getBody());
        } catch (Exception e) {
            assertEquals(true, false);
        }
        assertNotNull(jsonResponse);

        String uploadedBatchFileName = (String) jsonResponse.get("data");
        assertEquals(true, uploadedBatchFileName.startsWith("file_"));

        String searchWithFileUrl = "http://localhost:" + port + "/intact/ws/interaction/findInteractionWithFacet" +
                "?query=" + uploadedBatchFileName
                + "&batchSearch=" + true
                + "&page=0"
                + "&pageSize=10";

        ResponseEntity<String> response2 = restTemplate.getForEntity(searchWithFileUrl, String.class);
        System.out.println();


    }
}
