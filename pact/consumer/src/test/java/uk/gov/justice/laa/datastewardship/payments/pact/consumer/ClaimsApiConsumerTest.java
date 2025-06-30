package uk.gov.justice.laa.datastewardship.payments.pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Manual Pact Consumer Tests for Claims API
 * 
 * These tests define contracts from a frontend application perspective,
 * based on the OpenAPI specification in claims-data/api/open-api-specification.yml
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "claims-api-provider")
public class ClaimsApiConsumerTest {

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    // =============================================================================
    // GET /api/v1/claims - Get all claims
    // =============================================================================
    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact getAllClaimsSuccessContract(PactDslWithProvider builder) {
        return builder
            .given("multiple claims exist")
            .uponReceiving("a request to get all claims")
            .path("/api/v1/claims")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(createJsonHeaders())
            .body(new PactDslJsonArray()
                .object()
                    .integerType("id", 1)
                    .stringType("name", "Legal Aid Claim 1")
                    .stringType("description", "Criminal legal aid representation")
                .closeObject()
                .object()
                    .integerType("id", 2)
                    .stringType("name", "Legal Aid Claim 2")
                    .stringType("description", "Civil legal aid consultation")
                .closeObject())
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAllClaimsSuccessContract")
    void testGetAllClaims_Success(MockServer mockServer) throws Exception {
        HttpGet request = new HttpGet(mockServer.getUrl() + "/api/v1/claims");
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        
        String responseBody = EntityUtils.toString(response.getEntity());
        assertThat(responseBody).contains("Legal Aid Claim 1");
        assertThat(responseBody).contains("Legal Aid Claim 2");
    }

    // =============================================================================
    // GET /api/v1/claims/{id} - Get claim by ID
    // =============================================================================
    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact getClaimByIdSuccessContract(PactDslWithProvider builder) {
        return builder
            .given("claim with id 1 exists")
            .uponReceiving("a request to get claim with id 1")
            .path("/api/v1/claims/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(createJsonHeaders())
            .body("""
                {
                  "id": 1,
                  "name": "Legal Aid Claim 1",
                  "description": "Criminal legal aid representation"
                }
                """)
            .toPact();
    }

    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact getClaimByIdNotFoundContract(PactDslWithProvider builder) {
        return builder
            .given("claim with id 999 does not exist")
            .uponReceiving("a request to get claim with id 999")
            .path("/api/v1/claims/999")
            .method("GET")
            .willRespondWith()
            .status(404)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getClaimByIdSuccessContract")
    void testGetClaimById_Success(MockServer mockServer) throws Exception {
        HttpGet request = new HttpGet(mockServer.getUrl() + "/api/v1/claims/1");
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        
        String responseBody = EntityUtils.toString(response.getEntity());
        assertThat(responseBody).contains("Legal Aid Claim 1");
    }

    @Test
    @PactTestFor(pactMethod = "getClaimByIdNotFoundContract")
    void testGetClaimById_NotFound(MockServer mockServer) throws Exception {
        HttpGet request = new HttpGet(mockServer.getUrl() + "/api/v1/claims/999");
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    // =============================================================================
    // POST /api/v1/claims - Create new claim
    // =============================================================================
    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact createClaimSuccessContract(PactDslWithProvider builder) {
        return builder
            .given("system is ready to accept new claims")
            .uponReceiving("a request to create a new claim")
            .path("/api/v1/claims")
            .method("POST")
            .headers(createJsonHeaders())
            .body("""
                {
                  "name": "New Legal Aid Claim",
                  "description": "Family law legal aid case"
                }
                """)
            .willRespondWith()
            .status(201)
            .toPact();
    }

    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact createClaimValidationErrorContract(PactDslWithProvider builder) {
        return builder
            .given("system validates claim data")
            .uponReceiving("a request to create a claim with invalid data")
            .path("/api/v1/claims")
            .method("POST")
            .headers(createJsonHeaders())
            .body("""
                {
                  "name": "",
                  "description": ""
                }
                """)
            .willRespondWith()
            .status(400)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createClaimSuccessContract")
    void testCreateClaim_Success(MockServer mockServer) throws Exception {
        HttpPost request = new HttpPost(mockServer.getUrl() + "/api/v1/claims");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("""
            {
              "name": "New Legal Aid Claim",
              "description": "Family law legal aid case"
            }
            """));
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

    @Test
    @PactTestFor(pactMethod = "createClaimValidationErrorContract")
    void testCreateClaim_ValidationError(MockServer mockServer) throws Exception {
        HttpPost request = new HttpPost(mockServer.getUrl() + "/api/v1/claims");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("""
            {
              "name": "",
              "description": ""
            }
            """));
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
    }

    // =============================================================================
    // PUT /api/v1/claims/{id} - Update claim
    // =============================================================================
    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact updateClaimSuccessContract(PactDslWithProvider builder) {
        return builder
            .given("claim with id 1 exists and can be updated")
            .uponReceiving("a request to update claim with id 1")
            .path("/api/v1/claims/1")
            .method("PUT")
            .headers(createJsonHeaders())
            .body("""
                {
                  "name": "Updated Legal Aid Claim",
                  "description": "Updated criminal legal aid representation"
                }
                """)
            .willRespondWith()
            .status(205)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "updateClaimSuccessContract")
    void testUpdateClaim_Success(MockServer mockServer) throws Exception {
        HttpPut request = new HttpPut(mockServer.getUrl() + "/api/v1/claims/1");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("""
            {
              "name": "Updated Legal Aid Claim",
              "description": "Updated criminal legal aid representation"
            }
            """));
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(205);
    }

    // =============================================================================
    // DELETE /api/v1/claims/{id} - Delete claim
    // =============================================================================
    @Pact(consumer = "claims-frontend-app")
    public RequestResponsePact deleteClaimSuccessContract(PactDslWithProvider builder) {
        return builder
            .given("claim with id 1 exists and can be deleted")
            .uponReceiving("a request to delete claim with id 1")
            .path("/api/v1/claims/1")
            .method("DELETE")
            .willRespondWith()
            .status(205)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "deleteClaimSuccessContract")
    void testDeleteClaim_Success(MockServer mockServer) throws Exception {
        HttpDelete request = new HttpDelete(mockServer.getUrl() + "/api/v1/claims/1");
        
        HttpResponse response = httpClient.execute(request);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(205);
    }

    // Helper method to create JSON headers
    private Map<String, String> createJsonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
