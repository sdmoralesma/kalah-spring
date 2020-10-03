import AirElement from "./AirElement.js";
import {html} from "./../lit-html/lit-html.js";

export default class OverviewView extends AirElement {

  constructor() {
    super();
  }

  connectedCallback() {
    addEventListener('air-stocks', _ => this.onViewChanged());
    this.viewChanged();
  }

  createView() {
    return html`
      <style>
      .chart div {
        font: 10px sans-serif;
        background-color: steelblue;
        text-align: right;
        padding: 3px;
        margin: 1px;
        color: white;
      }      
      </style>
      <div class="chart"></div>
      <h1>hello world</h1>
      `;
  }
}

customElements.define('overview-view', OverviewView);