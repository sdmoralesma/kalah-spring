import AirElement from "./AirElement.js";
import {html} from "./../lit-html/lit-html.js";

export default class AboutView extends AirElement {

  constructor() {
    super();
    this.name = "duke";
  }

  connectedCallback() {
    this.viewChanged();
  }

  createView() {
    return html`
    <article>
      <h3>powered by web standards && ${this.name}</h3>
      <p class="has-line-data" data-line-start="4" data-line-end="5">Kalah is a Spring-based, mobile-friendly vanilla JS powered HTML5 game.</p> 
    </article>
    `;
  }

}

customElements.define('about-view', AboutView);