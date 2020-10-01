import Stocks from "./Stocks.js";
import ListView from "./ListView.js";

export default class AddView extends HTMLElement {

  constructor() {
    super();
    this.root = this.attachShadow({mode: 'open'});
  }

  connectedCallback() {
    this.root.innerHTML = `
    <style>
      form{
       width: 80%;
      }
      label {
        display: flex;
        justify-content: space-between;
      }
      input[type="number"] {
        width: 3em;        
      }
    </style>
    <form>
      <fieldset>     
        <legend>add stock</legend>
        <label for="name">name:
          <input id="name" required autocomplete="off" placeholder="name"/>
        </label>
        <label for="price">price:
          <input id="price" required type="number" min="0" value="0" placeholder="price"/>
        </label>
        <label for="amount">amount:
          <input id="amount" required type="number" min="1" placeholder="amount" value="1"/>
        </label>
        <input type="submit" value="add"/>
      </fieldset>
    </form>
    <list-view></list-view>
    `;
    this.nameInput = this.root.querySelector('#name');
    this.priceInput = this.root.querySelector('#price');
    this.amountInput = this.root.querySelector('#amount');
    this.root.querySelector('form').onsubmit = e => this.addStock(e);
  }

  addStock(event) {
    event.preventDefault();//to not submit the form
    console.log(event);
    const name = this.nameInput.value;
    const price = this.priceInput.value;
    const amount = this.amountInput.value;
    Stocks.add(name, price, amount);
  }

}

customElements.define('add-view', AddView);