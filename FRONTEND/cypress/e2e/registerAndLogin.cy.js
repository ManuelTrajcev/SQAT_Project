describe('Register and Login Flow', () => {
  const username = `testuser_${Math.floor(Math.random() * 1000000)}`;
  const password = 'testpassword';

  it('should register a new user and login successfully', () => {
    cy.visit('/register');

    cy.get('input[name="name"]').type('Test');
    cy.get('input[name="surname"]').type('User');
    cy.get('input[name="username"]').type(username);
    cy.get('input[name="email"]').type(`${username}@example.com`);
    cy.get('input[name="password"]').type(password);
    cy.get('input[name="repeatPassword"]').type(password);
    cy.get('#role-select').click(); // Open the dropdown
    cy.get('li').contains('User').should('be.visible').click(); 

    cy.get('#register-button').click();

    cy.url().should('include', '/login');
    cy.contains('Login').should('be.visible');

    cy.get('input[name="username"]').type(username);
    cy.get('input[name="password"]').type(password);
    cy.get('#login-button').click();

    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.contains('Workspaces Management System').should('be.visible');
    cy.contains('Welcome').should('be.visible');
  });
});