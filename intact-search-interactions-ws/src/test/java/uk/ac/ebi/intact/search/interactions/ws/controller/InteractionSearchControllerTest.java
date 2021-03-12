package uk.ac.ebi.intact.search.interactions.ws.controller;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import sun.net.www.http.HttpClient;
import uk.ac.ebi.intact.search.interactions.ws.RequiresSolrServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    public void uploadBatchFileFunctionality() {

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

    @Test
    @Ignore
    public void postRequestTest(){
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost( "http://localhost:" + port + wsContextPath + "/findInteractionWithFacet");

// Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("query", "EBI-724102"));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            org.apache.http.HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {

                    BufferedReader is =
                            new BufferedReader(new InputStreamReader(instream));
                    String line=null;
                    while ((line = is.readLine( )) != null) {
                        System.out.println(line);


                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
