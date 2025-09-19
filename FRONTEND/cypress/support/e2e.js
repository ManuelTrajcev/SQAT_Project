// You can add global hooks or custom commands here.
// For now, leave it empty or add a simple comment.



// This file is required by Cypress for e2e tests.
// Add custom commands or global setup here if needed.

Cypress.Commands.add('login', () => {
  cy.visit('/login');
  cy.get('input[name="username"]', { timeout: 10000 }).should('be.visible').type('mt');
  cy.get('input[name="password"]').should('be.visible').type('mt');
  cy.get('#login-button').should('be.enabled').click();
  cy.url().should('eq', Cypress.config().baseUrl + '/');
});