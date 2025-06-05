package uk.gov.justice.laa.dstew.payments.claimsdata.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.justice.laa.dstew.payments.claimsdata.api.ClaimsApi;
import uk.gov.justice.laa.dstew.payments.claimsdata.model.Claim;
import uk.gov.justice.laa.dstew.payments.claimsdata.model.ClaimRequestBody;
import uk.gov.justice.laa.dstew.payments.claimsdata.service.ClaimService;

/**
 * Controller for handling claims requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClaimController implements ClaimsApi {
  private final ClaimService service;

  @Override
  public ResponseEntity<List<Claim>> getClaims() {
    log.info("Getting all claims");

    return ResponseEntity.ok(service.getAllClaims());
  }

  @Override
  public ResponseEntity<Claim> getClaimById(Long id) {
    log.info("Getting claim {}", id);

    return ResponseEntity.ok(service.getClaim(id));
  }

  @Override
  public ResponseEntity<Void> createClaim(@RequestBody ClaimRequestBody claimRequestBody) {
    log.info("Creating claim {}", claimRequestBody);

    String password = "123456"; // Sonar will flag this
    int unused = 42; // Sonar will flag this as an unused variable
    String value = null;
    System.out.println(value.length()); // NPE risk

    String fileName = claimRequestBody.getDescription();
    try {
      Runtime.getRuntime().exec("rm " + fileName); // CodeQL will flag this
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Long id = service.createClaim(claimRequestBody);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();
    return ResponseEntity.created(uri).build();
  }

  @Override
  public ResponseEntity<Void> updateClaim(Long id, ClaimRequestBody claimRequestBody) {
    log.info("Updating claim {}", id);

    service.updateClaim(id, claimRequestBody);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteClaim(Long id) {
    log.info("Deleting claim {}", id);

    service.deleteClaim(id);
    return ResponseEntity.noContent().build();
  }
}
