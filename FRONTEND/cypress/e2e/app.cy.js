beforeEach(() => {
  cy.clearCookies();
  cy.clearLocalStorage();
  cy.login();
});

describe('HomePage', () => {
  it('should display the app title and welcome message', () => {
    cy.visit('/');
    cy.contains('Workspaces Management System').should('be.visible');
    cy.contains('Welcome').should('be.visible');
  });
});

describe('WorkspacesPage', () => {
  it('should display workspace tabs and grid', () => {
    cy.visit('/workspaces');
    cy.contains('All Workspaces').should('be.visible');
    cy.contains('My Workspaces').should('be.visible');
    cy.get('.progress-box, .products-box').should('exist');
  });

  it('should switch between tabs', () => {
    cy.visit('/workspaces');
    cy.contains('My Workspaces').click();
    cy.get('.products-box').should('exist');
  });
});