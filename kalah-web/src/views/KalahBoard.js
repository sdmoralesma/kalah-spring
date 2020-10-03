import AirElement from "./AirElement.js";
import {html} from "./../lit-html/lit-html.js";

export default class KalahBoard extends AirElement {

  constructor() {
    super();
    this.game = null;
    this.board = null;
  }

  connectedCallback() {
    this.viewChanged();
  }

  createView() {
    return html`
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" 
            integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" 
            crossorigin="anonymous">
      <style>
      .borderless td, .borderless th {
        border: none;
      }
      </style>
      <div class="container">
        <div class="row">
          <div class="w-100 mx-auto">
            <div id="alert" class="alert alert-secondary" role="alert">
              ${this.printStatus()}
            </div>
            <table class="table borderless">
              <tbody>
              <tr>
                <th></th>
                <td><button id="13" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(13)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(13)}</button></td>
                <td><button id="12" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(12)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(12)}</button></td>
                <td><button id="11" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(11)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(11)}</button></td>
                <td><button id="10" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(10)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(10)}</button></td>
                <td><button id="9" type="button" class="btn btn-dark btn-block"  ?disabled=${this.isDisabled(9)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(9)}</button></td>
                <td><button id="8" type="button" class="btn btn-dark btn-block"  ?disabled=${this.isDisabled(8)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(8)}</button></td>
              </tr>
              <tr>
                <th><button id="14" type="button" class="btn btn-outline-dark btn-block" disabled>${this.stonesByPitId(14)} <span class="badge badge-light">North</span></button></th>
                <th colspan="6"></th>
                <th><button id="7" type="button" class="btn btn-outline-dark btn-block" disabled>${this.stonesByPitId(7)} <span class="badge badge-light">South</span></button></th>
              </tr>
              <tr>
                <th></th>
                <td><button id="1" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(1)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(1)}</button></td>
                <td><button id="2" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(2)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(2)}</button></td>
                <td><button id="3" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(3)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(3)}</button></td>
                <td><button id="4" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(4)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(4)}</button></td>
                <td><button id="5" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(5)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(5)}</button></td>
                <td><button id="6" type="button" class="btn btn-dark btn-block" ?disabled=${this.isDisabled(6)} @click=${e => this.makeMove(e)}>${this.stonesByPitId(6)}</button></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <br>
      <br>
      <div class="container">
        <button id="new-game" type="button" class="btn btn-dark btn-lg" @click=${_ => this.createBoard()}>New Game</button>
      </div>
    `;
  }

  createBoard() {
    fetch('http://localhost:8080/games/', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'}
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      this.game = data;
      this.board = JSON.parse(data.board);
      this.viewChanged();
      console.log("new game" + JSON.stringify(this.game))
    })
    .catch(error => console.error(error));
  }

  makeMove(event) {
    event.preventDefault();
    const pitId = event.target.id;
    const gameId = this.game.id;
    console.log(gameId, pitId);
    fetch(`http://localhost:8080/games/${gameId}/pits/${pitId}`, {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'}
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      this.game = data;
      this.board = JSON.parse(data.board);
      this.viewChanged();
      console.log("move on game" + JSON.stringify(this.game));
    })
    .catch(error => console.error(error));
  }

  stonesByPitId(pitId) {
    return this.board ? this.board[pitId] : 0;
  }

  isDisabled(pitId) {
    if (this.board) {
      const side = this.board[0];
      if (side) {
        if (pitId >= 1 && pitId <= 6) {
          return true;
        }
      } else {
        if (pitId >= 8 && pitId <= 13) {
          return true;
        }
      }

      return false;
    }

    if (this.game && this.game.status === "FINISHED") {
      return true;
    }

    return true;
  }

  printStatus() {
    if (!this.game) {
      return "Please start a new game by clicking the 'New game'' button";
    }

    switch (this.game.status) {
      case 'IN_PROGRESS':
        return `Turn of player in side ${this.board[0] ? 'North' : 'South'}`
      case 'FINISHED':
        switch (this.game.winner) {
          case 'NORTH':
          case 'SOUTH':
            return `The winner is the ${this.game.winner} player!`
          case 'NONE':
            return 'The game finished in a draw!'
          default :
            return `Unknown winner: ${this.game.winner}`;
        }
      default :
        return `Unknown status: ${this.game.status}`;
    }
  }
}

customElements.define('kalah-board', KalahBoard)