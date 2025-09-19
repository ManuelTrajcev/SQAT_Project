describe('Access Workspace Popup', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/workspaces');
  });

  it('should show Access workspace popup when clicking View on ALL WORKSPACES tab', () => {
    cy.contains('All Workspaces').should('have.attr', 'aria-selected', 'true');
    cy.contains('button', 'View').first().click();
    cy.contains('Workspace Info').should('be.visible');
    cy.contains('button', 'Cancel').click();
    cy.contains('Workspace Info').should('not.exist');
  });
});