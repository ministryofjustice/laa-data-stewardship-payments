# 🎬 Pact Testing POC - Quick Demo

This guide will walk you through a complete demonstration of the Pact testing setup.

## 🚀 5-Minute Demo

### Step 1: Check System Status

```bash
./gradlew pact:pactStatus
```

This shows available commands and checks if services are running.

### Step 2: Start Pact Broker

```bash
./gradlew pact:startBroker
```

- Opens Pact Broker at http://localhost:9292
- Username: `pact_workshop`, Password: `pact_workshop`
- Wait ~15 seconds for startup

### Step 3: Run Manual Consumer Tests

```bash
./gradlew :pact:consumer:test 
./gradlew :pact:consumer:publishManualPacts
```

**What happens:**
- Runs manual consumer tests in `ClaimsApiConsumerTest.java`
- Generates contracts based on realistic consumer scenarios
- Publishes contracts to Pact Broker
- Check results at http://localhost:9292

### Step 4: Generate OpenAPI Contracts

```bash
./gradlew pact:publishOpenApiContracts
```

**What happens:**
- Reads `claims-data/api/open-api-specification.yml`
- Generates contracts automatically from API spec
- Publishes as separate contract version
- Compare with manual contracts in broker

### Step 5: Start Mock Provider

```bash
./gradlew :pact:provider:bootRun
```

**Endpoints available:**
- http://localhost:8080/api/v1/claims
- http://localhost:8080/api/v1/claims/1
- http://localhost:8080/actuator/health

### Step 6: Verify Provider Against Manual Contracts

```bash
# In another terminal
./gradlew :pact:provider:verifyManualContracts
```

**What happens:**
- Downloads manual contracts from broker
- Starts mock provider automatically
- Runs contract verification tests
- Shows pass/fail results


## 🎯 Complete Workflow (All-in-One)

```bash
./gradlew pact:runFullPactWorkflow
```

This runs everything automatically:
1. ✅ Starts Pact Broker
2. ✅ Runs manual consumer tests
3. ✅ Generates OpenAPI contracts  
4. ✅ Publishes all contracts
5. ✅ Verifies provider against both

## 🔍 What to Look For

### In Pact Broker (http://localhost:9292):

1. **Consumer**: `claims-frontend-app`
2. **Provider**: `claims-api-provider`
3. **Two contract versions**:
   - Manual tests (tag: `manual-tests`)
   - OpenAPI generated (tag: `openapi-generated`)

### Contract Differences:

**Manual Contracts:**
```json
{
  "description": "a request to get all claims",
  "providerState": "multiple claims exist",
  "request": { "method": "GET", "path": "/api/v1/claims" },
  "response": {
    "status": 200,
    "body": [
      {"id": 1, "name": "Legal Aid Claim 1", "description": "Criminal legal aid representation"}
    ]
  }
}
```

**OpenAPI Contracts:**
```json
{
  "description": "Get all claims - generated from OpenAPI",
  "providerState": "multiple claims exist for OpenAPI generation",
  "request": { "method": "GET", "path": "/api/v1/claims" },
  "response": {
    "status": 200,
    "body": [
      {"id": 1, "name": "OpenAPI Generated Claim 1", "description": "Auto-generated from OpenAPI spec"}
    ]
  }
}
```

### Provider Verification Results:

Both contract types should **PASS** because:
- Mock provider returns hardcoded responses
- Responses satisfy both manual and OpenAPI contract expectations
- Same API structure, different test data

## 🧪 Test Individual Components

### Test Consumer Only:
```bash
./gradlew :pact:consumer:test  # Run tests without publishing
```

### Test Provider Only:
```bash
./gradlew :pact:provider:test  # Run all provider tests
```

## 🔧 Troubleshooting

### Broker not starting:
```bash
cd pact/broker
docker-compose logs
```

### Provider tests failing:
```bash
# Check if provider is running
curl http://localhost:8080/actuator/health

# Check specific endpoint
curl http://localhost:8080/api/v1/claims
```

### No contracts in broker:
```bash
# Check publishing logs
./gradlew :pact:consumer:publishManualPacts --info
```

## 🎓 Key Learnings

1. **Manual vs OpenAPI**: Compare realistic scenarios vs comprehensive spec coverage
2. **Provider Agnostic**: Same provider satisfies both contract types
3. **Contract Evolution**: See how different approaches create different contracts
4. **Mock-First**: Test contracts before implementing real API
5. **Verification**: Ensure provider satisfies consumer expectations

## 🚀 Next Steps

1. **Implement Real Provider**: Replace mock with actual business logic
2. **Add CI/CD**: Automate contract testing in build pipeline
3. **Environment Promotion**: Use contracts for deployment validation
4. **Consumer Teams**: Have multiple teams create their own contracts
5. **Breaking Change Detection**: Use Pact to catch API breaking changes

---

**🎉 You've now seen a complete Pact contract testing workflow!**

The mock provider demonstrates that you can test API contracts before implementation, enabling true contract-driven development.
