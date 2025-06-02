package uk.gov.justice.laa.dstew.payments.claimsdata.mapper;

import org.mapstruct.Mapper;
import uk.gov.justice.laa.dstew.payments.claimsdata.entity.ClaimEntity;
import uk.gov.justice.laa.dstew.payments.claimsdata.model.Claim;

/**
 * The mapper between Claim and ClaimEntity.
 */
@Mapper(componentModel = "spring")
public interface ClaimMapper {

  /**
   * Maps the given claim entity to an claim.
   *
   * @param claimEntity the claim entity
   * @return the claim
   */
  Claim toClaim(ClaimEntity claimEntity);

  /**
   * Maps the given claim to an claim entity.
   *
   * @param claim the claim
   * @return the claim entity
   */
  ClaimEntity toClaimEntity(Claim claim);
}
