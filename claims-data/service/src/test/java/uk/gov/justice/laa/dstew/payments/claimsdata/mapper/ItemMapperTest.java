package uk.gov.justice.laa.dstew.payments.claimsdata.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.dstew.payments.claimsdata.entity.ClaimEntity;
import uk.gov.justice.laa.dstew.payments.claimsdata.model.Claim;

@ExtendWith(MockitoExtension.class)
class ClaimMapperTest {
  private static final Long CLAIM_ID = 123L;
  private static final String CLAIM_NAME = "Claim One";
  private static final String CLAIM_DESCRIPTION = "This is Claim One.";

  @InjectMocks private ClaimMapper claimMapper = new ClaimMapperImpl();

  @Test
  void shouldMapToClaimEntity() {
    Claim claim = Claim.builder().id(CLAIM_ID).name(CLAIM_NAME).description(CLAIM_DESCRIPTION).build();

    ClaimEntity result = claimMapper.toClaimEntity(claim);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(CLAIM_ID);
    assertThat(result.getName()).isEqualTo(CLAIM_NAME);
  }

  @Test
  void shouldMapToClaim() {
    ClaimEntity claimEntity = new ClaimEntity(CLAIM_ID, CLAIM_NAME, CLAIM_DESCRIPTION);

    Claim result = claimMapper.toClaim(claimEntity);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(CLAIM_ID);
    assertThat(result.getName()).isEqualTo(CLAIM_NAME);
  }
}
