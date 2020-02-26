package uk.ac.ebi.intact.search.interactions.ws.controller;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by anjali on 26/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InteractionSearchControllerTest {

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
    public void uploadBatchFileAndSearchFuntionality() {

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("batchfiles/2_interactors.txt"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity =
                new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
        String serverUrl = "http://localhost:" + port + "/intact/ws/interaction/uploadBatchFile";

        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.POST, entity, String.class, "");

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
        assertEquals(true, ((String) jsonResponse.get("data")).startsWith("file_"));
    }
}
