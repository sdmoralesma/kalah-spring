# Build
    
    mvn clean package

### Run application

    mvn spring-boot:run 

### Execute system sest

Switch to the "-st" module and perform:

    mvn compile failsafe:integration-test
    
### Execute performance test

Switch to the "-perf" module and perform:
   
    mvn clean test-compile gatling:test
    
To run the scenarios first and then the performance test use:
        
    mvn clean test    