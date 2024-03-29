import OverviewView from "./views/OverviewView.js";
import KalahBoard from "./views/KalahBoard.js";
import AboutView from "./views/AboutView.js";

export default class AirSlot extends HTMLElement {

  constructor() {
    super();
    this.oldChild = null;
    this.currentView = null;
    this.root = this.attachShadow({mode: 'open'});
  }

  connectedCallback() {
    this.root.innerHTML = `
      <style>
      slot[name="view"] {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
      }
      </style>
      <slot name="view">VIEW</slot>
    `;
    document.addEventListener('air-nav', e => this.onNavigation(e));
    this.oldChild = this.root.querySelector("[name=view]");
  }

  onNavigation(evt) {
    const {detail} = evt;
    const {hash: linkName} = detail;
    this.currentView = linkName;
    this.loadView(linkName);
  }

  async loadView(linkName) {
    let newChild;
    switch (linkName) {
      case 'About':
        newChild = new AboutView();
        break;
      case 'KalahBoard':
        newChild = new KalahBoard();
        break;
      case 'Overview':
        newChild = new OverviewView();
        break;
      default:
        throw new Error(`Unknown route: ${linkName}`);
    }

    if (this.oldChild) {
      this.root.replaceChild(newChild, this.oldChild);
    } else {
      this.root.appendChild(newChild);
    }

    this.oldChild = newChild;
  }

}

customElements.define('air-slot', AirSlot);