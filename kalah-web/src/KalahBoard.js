import {html, render} from "./lit-html/lit-html.js";

export default class KalahBoard extends HTMLElement {

  constructor() {
    super();
    this.board = null;
  }

  connectedCallback() {
    addEventListener('kalah-move', _ => this.viewChanged());
    this.createBoard();
    this.viewChanged();
  }

  viewChanged() {
    render(this.createView(), this);
  }

  createView() {
    return html`
      <div class="container">
        <div class="row">
          <h1>Board</h1>
          <br>
          <div class="w-100 mx-auto">            
            <table class="table table-bordered">
              <tbody>
              <tr>
                <th></th>
                <td><button id="13" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(13)}</button></td>
                <td><button id="12" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(12)}</button></td>
                <td><button id="11" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(11)}</button></td>
                <td><button id="10" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(10)}</button></td>
                <td><button id="9" type="button" class="btn btn-light btn-block"  @click=${e => this.makeMove(e)}>${this.stonesByPitId(9)}</button></td>
                <td><button id="8" type="button" class="btn btn-light btn-block"  @click=${e => this.makeMove(e)}>${this.stonesByPitId(8)}</button></td>
              </tr>
              <tr>
                <th><button id="14" type="button" class="btn btn-outline-info btn-block" disabled>${this.stonesByPitId(14)}</button></th>
                <th colspan="6"></th>
                <th><button id="7" type="button" class="btn btn-outline-info btn-block" disabled>${this.stonesByPitId(7)}</button></th>
              </tr>
              <tr>
                <th></th>
                <td><button id="1" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(1)}</button></td>
                <td><button id="2" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(2)}</button></td>
                <td><button id="3" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(3)}</button></td>
                <td><button id="4" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(4)}</button></td>
                <td><button id="5" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(5)}</button></td>
                <td><button id="6" type="button" class="btn btn-light btn-block" @click=${e => this.makeMove(e)}>${this.stonesByPitId(6)}</button></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <br>
      <br>
      <button id="new-game" type="button" class="btn btn-light btn-block" @click=${_ => this.createBoard()}>New Game</button>
    `;
  }

  assignStonesToPits() {
    document.querySelectorAll('button');
  }


  createBoard() {
    fetch('http://localhost:8080/games/', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'}
    })
    .then(response => response.json())
    .catch(error => console.error(error))
    .then(data => {
      this.board = data;
      this.viewChanged();
      console.log("new board" + JSON.stringify(this.board))
    });
  }

  makeMove(event){
    event.preventDefault();
    const pitId = event.target.id;
    const gameId = this.board.id;
    console.log(gameId, pitId);
    fetch(`http://localhost:8080/games/${gameId}/pits/${pitId}`, {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'}
    })
    .then(response => response.json())
    .catch(error => console.error(error))
    .then(data => {
      this.board = data
    })
    .then(() => console.log("move on board" + JSON.stringify(this.board)))
    this.triggerBoardChangedEvent(gameId, pitId);
  }

  triggerBoardChangedEvent(gameId, pitId) {
    const event = new CustomEvent('kalah-move', {
      detail: {
        gameId,
        pitId
      }
    });
    document.dispatchEvent(event);
  }

  stonesByPitId(pitId) {
    if(this.board) {
      return JSON.parse(this.board.board)[pitId];
    }

    return -1;
  }

}

customElements.define('kalah-board', KalahBoard)