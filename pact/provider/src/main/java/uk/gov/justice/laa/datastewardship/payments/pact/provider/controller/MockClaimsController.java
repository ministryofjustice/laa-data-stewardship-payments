package uk.gov.justice.laa.datastewardship.payments.pact.provider.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Mock Claims Controller for Pact Provider Testing
 * 
 * This controller provides hardcoded responses that satisfy both:
 * 1. Manual consumer test contracts
 * 2. OpenAPI-generated contracts
 * 
 * It implements all endpoints defined in the OpenAPI specification
 * without requiring real business logic or database implementation.
 */
@RestController
@RequestMapping("/api/v1/claims")
public class MockClaimsController {

    // =============================================================================
    // GET /api/v1/claims - Get all claims
    // =============================================================================
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllClaims() {
        List<Map<String, Object>> claims = List.of(
            Map.of(
                "id", 1,
                "name", "Legal Aid Claim 1",
                "description", "Criminal legal aid representation"
            ),
            Map.of(
                "id", 2,
                "name", "Legal Aid Claim 2",
                "description", "Civil legal aid consultation"
            )
        );
        
        return ResponseEntity.ok(claims);
    }

    // =============================================================================
    // GET /api/v1/claims/{id} - Get claim by ID
    // =============================================================================
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getClaimById(@PathVariable Long id) {
        if (id == 1L) {
            Map<String, Object> claim = Map.of(
                "id", 1,
                "name", "Legal Aid Claim 1",
                "description", "Criminal legal aid representation"
            );
            return ResponseEntity.ok(claim);
        } else if (id == 999L) {
            // Test case for non-existent claim
            return ResponseEntity.notFound().build();
        }
        
        // Default case - return a generic claim or 404
        return ResponseEntity.notFound().build();
    }

    // =============================================================================
    // POST /api/v1/claims - Create new claim
    // =============================================================================
    @PostMapping
    public ResponseEntity<Void> createClaim(@RequestBody Map<String, Object> claimData) {
        String name = (String) claimData.get("name");
        String description = (String) claimData.get("description");
        
        // Validate required fields as per OpenAPI specification
        if (name == null || name.trim().isEmpty() || 
            description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Mock successful creation
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =============================================================================
    // PUT /api/v1/claims/{id} - Update claim
    // =============================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClaim(@PathVariable Long id, @RequestBody Map<String, Object> claimData) {
        // Validate that claim exists (for our mock, assume id=1 exists)
        if (id != 1L) {
            return ResponseEntity.notFound().build();
        }
        
        String name = (String) claimData.get("name");
        String description = (String) claimData.get("description");
        
        // Validate required fields
        if (name == null || name.trim().isEmpty() || 
            description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Mock successful update (204 No Content as per OpenAPI spec)
        //return ResponseEntity.noContent().build();

        return ResponseEntity
            .status(HttpStatus.RESET_CONTENT)
            .build();
    }

    // =============================================================================
    // DELETE /api/v1/claims/{id} - Delete claim
    // =============================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        // For our mock, assume id=1 exists and can be deleted
        if (id != 1L) {
            return ResponseEntity.notFound().build();
        }
        
        // Mock successful deletion (204 No Content as per OpenAPI spec)
        //return ResponseEntity.noContent().build();
        return ResponseEntity
            .status(HttpStatus.RESET_CONTENT)
            .build();
    }
}
