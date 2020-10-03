import AirNav from "./AirNav.js";
import AirSlot from "./AirSlot.js";
import AirCrumb from "./AirCrumb.js";
import AirUpdate from "./AirUpdate.js";

navigator.serviceWorker.register('./kalah-worker.js')
    .then(registration => console.log("registration OK",registration))
    .catch(error => console.error('Failed to register', error));