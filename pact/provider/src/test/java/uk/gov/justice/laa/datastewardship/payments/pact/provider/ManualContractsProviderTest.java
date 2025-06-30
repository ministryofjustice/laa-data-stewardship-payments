package uk.gov.justice.laa.datastewardship.payments.pact.provider;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Provider Verification Tests for Manual Consumer Contracts
 */
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = MockProviderApplication.class
)
@Provider("claims-api-provider")
@PactBroker(
  host               = "localhost",
  port               = "9292",
  scheme             = "http",
  enablePendingPacts = "false",          // turn strict mode on
  tags               = {"dev","manual-tests"},
  providerTags       = {"dev"},
  authentication     = @PactBrokerAuth(
    username = "pact_workshop",
    password = "pact_workshop"
  )
)
public class ManualContractsProviderTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  void before(PactVerificationContext context) {
    // point Pact at our running mock provider
    context.setTarget(new HttpTestTarget("localhost", port));
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void pactVerificationTestTemplate(PactVerificationContext context) {
    // run each interaction
    context.verifyInteraction();
  }

  // ==== provider states for your consumer ----

  @State("multiple claims exist")
  public void multipleClaimsExist() { /* no-op, mock controller handles */ }

  @State("claim with id 1 exists")
  public void claimWithId1Exists() { }

  @State("claim with id 999 does not exist")
  public void claimWithId999DoesNotExist() { }

  @State("system is ready to accept new claims")
  public void systemIsReadyToAcceptNewClaims() { }

  @State("system validates claim data")
  public void systemValidatesClaimData() { }

  @State("claim with id 1 exists and can be updated")
  public void claimWithId1ExistsAndCanBeUpdated() { }

  @State("claim with id 1 exists and can be deleted")
  public void claimWithId1ExistsAndCanBeDeleted() { }
}