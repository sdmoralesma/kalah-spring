context("kalah", () => {

  beforeEach(() => {
    cy.visit("http://localhost:8080/");
  });

  it('create game and pick a pit for both sides', () => {

    cy.get('[href="#KalahBoard"]').click().should("have.class", "a-link");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="alert"]').should("contain.text", "Please start a new game");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="new-game"]').click();

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="alert"]').should("contain.text", "Turn of player in side South");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('button[id="6"]').click();

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="alert"]').should("contain.text", "Turn of player in side North");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('button[id="13"]').click();

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="alert"]').should("contain.text", "Turn of player in side South");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="7"]').should("contain.text", "1");

    cy.get('air-slot').shadow().find('kalah-board').shadow()
    .find('[id="14"]').should("contain.text", "1");

  });

});