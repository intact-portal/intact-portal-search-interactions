package uk.ac.ebi.intact.search.interactions.ws.controller;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.ClassRule;
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
import uk.ac.ebi.intact.search.interactions.ws.RequiresSolrServer;

import static org.junit.Assert.*;

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

    @Value("${server.servlet.context-path}")
    private String wsContextPath;

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

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
        String uploadFileUrl = "http://localhost:" + port + wsContextPath + "/uploadFile";

        ResponseEntity<String> response = restTemplate.exchange(uploadFileUrl, HttpMethod.POST, entity, String.class, "");

        // Expect Ok
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        String fileName = null;
        try {
            fileName = response.getBody();
        } catch (Exception e) {
            fail();
        }
        assertNotNull(fileName);

        assertTrue(fileName.startsWith("file_"));

        String searchWithFileUrl = "http://localhost:" + port + wsContextPath + "/findInteractionWithFacet" +
                "?query=" + fileName
                + "&batchSearch=" + true
                + "&page=0"
                + "&pageSize=10";

        ResponseEntity<String> fileSearchResponse = restTemplate.getForEntity(searchWithFileUrl, String.class);
        assertNotNull(fileSearchResponse);
        assertNotNull(fileSearchResponse.getBody());

        try {
            JSONParser parser = new JSONParser();
            JSONObject fileSearchJson = (JSONObject) parser.parse(fileSearchResponse.getBody());
            assertNotNull(fileSearchJson);
            assertEquals(9l, fileSearchJson.get("numberOfElements"));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void emptyFileUploadTest() {

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("batchfiles/empty_file.txt"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity =
                new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
        String uploadFileUrl = "http://localhost:" + port + wsContextPath + "/uploadFile";

        ResponseEntity<String> response = restTemplate.exchange(uploadFileUrl, HttpMethod.POST, entity, String.class, "");

        // Expectation failed
        assertEquals(response.getStatusCode(), HttpStatus.EXPECTATION_FAILED);
    }
}
