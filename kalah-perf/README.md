# Kalah Performance Test

### Setup
* Users: 5500
* Time period: 10 sec
* Hardware: 
    * Processor: 2,9 GHz 6-Core Intel Core i9
    * Memory: 32 GB 2400 MHz DDR4
    * 500GB Flash Disk

### Results

![report](docs/images/gatling-report.png)    

### Analysis
1. The system as a whole can handle around 320 requests per second with a failure rate of 1% for 5500 concurrent users 
over 10 seconds time period.

2. The creation of a game tends to be slower compared to other operations, however to notice that given the nature of the game, 
the users will be more likely be using the retrieval operations more intensively.

3. The response times are below 100ms for all operations. If the app uses a real DBMS (not h2), it would be advisable to 
measure the impact of the new topology.

4. Executing the same exercise with **6000** users generates a failure rate o f

### Future work
1. While executing the tests, there were considerable differences in the results by changing the 
version of the maven-gatling-plugin between versions 3.1.0 and 3.0.5. I preferred to keep the results based on the 
latter because the new release might

### Resources

* [Gatling Report generated](/reports/gamessimulation-20200930150031699/index.html)
* [Karate Test Automation](https://intuit.github.io/karate/karate-gatling/)