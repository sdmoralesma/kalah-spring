import Stocks from "./Stocks.js";

export default class ListView extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(){
    addEventListener('air-stocks', _ => this.render());
    return this.render();
  }

  render() {
    this.innerHTML = `
      <style>
        header {
        background: var(--air-brown, red);
        }
      </style>
      <header>
      <h2>the stocks</h2>
      </header>
      ${this.table()}
    `;
  }

  table() {
    return `
      <table>
      <thead>      
        <tr>
          <th>name</th><th>price</th><th>amount</th>
        </tr>
      </thead>
      <tbody>
      ${this.content()}
      </tbody>
      </table>
    `;
  }

  content() {
    return Stocks.all()
    .map(stock => this.row(stock))
    .reduce((previous, current) => previous + current);
  }

  row({name, price, amount}) {
    return `
      <tr>
      <td>${name}</td><td>${price}</td><td>${amount}</td>
      </tr>
    `;
  }
}

customElements.define('list-view', ListView);