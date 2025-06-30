package uk.gov.justice.laa.datastewardship.payments.pact.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mock Provider Application for Pact Testing POC
 * 
 * This is a minimal Spring Boot application that provides mock endpoints
 * to satisfy Pact contracts without requiring real business logic implementation.
 * 
 * The mock server returns hardcoded responses that match the contract expectations
 * defined in the consumer tests and OpenAPI specification.
 */
@SpringBootApplication
public class MockProviderApplication {
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting LAA Data Stewardship Payments Mock Provider...");
        System.out.println("📋 This mock server satisfies Pact contracts for POC demonstration");
        System.out.println("🎯 Endpoints available:");
        System.out.println("   GET    /api/v1/claims");
        System.out.println("   GET    /api/v1/claims/{id}");
        System.out.println("   POST   /api/v1/claims");
        System.out.println("   PUT    /api/v1/claims/{id}");
        System.out.println("   DELETE /api/v1/claims/{id}");
        System.out.println("🏥 Health: /actuator/health");
        
        SpringApplication.run(MockProviderApplication.class, args);
    }
}
