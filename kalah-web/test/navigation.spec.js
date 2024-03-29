context('navigation', () => {
  beforeEach(() => {
    cy.visit("http://localhost:3000");
  })

  const views = ["Overview", "KalahBoard", "About"];
  views.forEach(view => {
    it(`nav by click -> ${view}`, () => {
      cy.get(`[href="#${view}"]`)
      .click().should("have.class", "a-link");
      verify(view);
    });
  })

  views.forEach(view => {
    it(`nav by hash ${view}`, () => {
      cy.visit(`#${view}`);
      cy.get(`[href="#${view}"]`).should("have.class", "a-link");
      verify(view);
    });
  });

  function verify(view) {
    cy.get('air-crumb').should('contain', view);
    cy.get('air-slot').then(ref => {
      cy.window().then(win => {
        win.customElements.whenDefined('air-slot')
        .then(() => {
          const {currentView} = ref[0];
          console.log(currentView);
          expect(currentView).to.eq(view);
        });
      })
    })
  }

});