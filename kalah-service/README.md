# Build
    
    mvn clean package

# Run

    mvn spring-boot:run 

# System Test

Switch to the "-st" module and perform:

    mvn compile failsafe:integration-test
    
# Performance Test

Switch to the "-perf" module and perform:

    mvn clean test    