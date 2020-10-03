import AirElement from "./AirElement.js";
import {html} from "./../lit-html/lit-html.js";

export default class AboutView extends AirElement {

  constructor() {
    super();
  }

  connectedCallback() {
    this.viewChanged();
  }

  createView() {
    return html`
    <article>
      <h1 class="code-line" data-line-start=0 data-line-end=1 ><a id="About_Kalah_0"></a>About Kalah Game</h1>
      <p class="has-line-data" data-line-start="2" data-line-end="3">Kalah is a Spring-based, mobile-friendly, vanilla JS powered HTML5 game.</p>
      <h3 class="code-line" data-line-start=4 data-line-end=5 ><a id="Tech_4"></a>Tech</h3>
      <p class="has-line-data" data-line-start="6" data-line-end="7">Kalah uses a number of open source projects to work properly:</p>
      <ul>
      <li class="has-line-data" data-line-start="8" data-line-end="9"><a target="_blank" href="https://spring.io/">Spring Technology</a> - For development of cloud-ready REST services</li>
      <li class="has-line-data" data-line-start="9" data-line-end="10"><a target="_blank" href="https://junit.org/junit5/">JUnit 5</a> - For unit- and system- tests</li>
      <li class="has-line-data" data-line-start="10" data-line-end="11"><a target="_blank" href="https://rest-assured.io/">Rest Assured</a> - Fluent API to validate REST calls</li>
      <li class="has-line-data" data-line-start="11" data-line-end="12"><a target="_blank" href="http://vanilla-js.com/">Vanilla JS</a> - No frameworks, just the platform</li>
      <li class="has-line-data" data-line-start="12" data-line-end="13"><a target="_blank" href="https://gatling.io/">Gatling</a> - For performance testing</li>
      <li class="has-line-data" data-line-start="13" data-line-end="14"><a target="_blank" href="https://www.cypress.io/">Cypress</a> - To run end-to-end testing</li>
      <li class="has-line-data" data-line-start="14" data-line-end="15"><a target="_blank" href="https://intuit.github.io/karate/">Karate</a> - Gherkin based tool to write system test that can be reused for performance simulations</li>
      <li class="has-line-data" data-line-start="15" data-line-end="17"><a target="_blank" href="https://rollupjs.org/guide/en/">rollup.js</a> - To bundle the javascript modules in a single distributable</li>
      </ul> 
    </article>
    `;
  }

}

customElements.define('about-view', AboutView);