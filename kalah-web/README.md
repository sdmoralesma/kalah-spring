# Kalah-Web

### Development

1. Install

    * https://www.browsersync.io/
    * https://www.cypress.io/
    * https://rollupjs.org/

2. To serve the app for development use:

    ```shell script
    browser-sync src -f src -b "google chrome" --no-notify
    ```

3. To serve the final dist:

    Execute the script:
    ```shell script
    ./buildRollup.sh     
    ```
    
    That will create the `dist` folder necessary to run:
    
    ```shell script
    browser-sync start --ss dist/ -b "google chrome"
    ```

4. To run the end-to-end tests verify that the Spring application runs in the http://localhost:8080 
and execute Cypress with:

    ```shell script
    ./node_modules/cypress/bin/cypress open
    ```
