describe('Login Page', () => {
  it('should allow user to login with username and password', () => {
    cy.visit('/login');
    cy.get('input[name="username"]').type('mt');
    cy.get('input[name="password"]').type('mt');
    cy.get('#login-button').click();

    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.contains('Workspaces Management System').should('be.visible');
  });
});