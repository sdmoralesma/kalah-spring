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
      <h1>Kalah</h1>
      <p><b>Kalah</b>, also called <b>Kalaha</b> or <b>Mancala</b>, is a game in the mancala family invented in the United States by William Julius Champion, Jr. in 1940. This game is sometimes also called "Kalahari", possibly by false etymology from the Kalahari desert in Namibia.</p>
      <p>As the most popular and commercially available variant of mancala in the West, Kalah is also sometimes referred to as Warri or Awari, although those names more properly refer to the game Oware.</p>
      <p>For most of its variations, Kalah is a solved game with a first-player win if both players play perfect games. The Pie rule can be used to balance the first-player's advantage.</p>
    <p>The game provides a Kalah board and a number of <i>seeds</i> or counters.  The board has 6 small pits, called houses, on each side; and a big pit, called an end zone, at each end. The object of the game is to capture more seeds than one's opponent.</p>

    <ol><li>At the beginning of the game, four seeds are placed in each house.  This is the traditional method.</li>
    <li>Each player controls the six houses and their seeds on the player's side of the board.  The player's score is the number of seeds in the store to their right.</li>
    <li>Players take turns <i>sowing</i> their seeds.  On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.</li>
    <li>If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.</li>
    <li>If the last sown seed lands in the player's store, the player gets an additional move.  There is no limit on the number of moves a player can make in their turn.</li>
    <li>When one player no longer has any seeds in any of their houses, the game ends.  The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.</li></ol>
      `;
  }
}

customElements.define('overview-view', OverviewView);