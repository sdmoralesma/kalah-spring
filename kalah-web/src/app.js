import AirNav from "./AirNav.js";
import AirSlot from "./AirSlot.js";
import AirCrumb from "./AirCrumb.js";
import TotalView from "./views/TotalView.js";

navigator.serviceWorker.register('./stockz-worker.js')
    .then(registration => console.log("registration OK",registration))
    .catch(error => console.error('Failed to register', error));