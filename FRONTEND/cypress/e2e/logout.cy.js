describe('Logout', () => {
  beforeEach(() => {
    cy.login();
  });

  it('should log out and redirect to login page', () => {
    // Adjust the selector below to match your actual logout button
    cy.contains('Logout').should('be.visible').click();

    // After logout, should redirect to login page
    cy.url().should('include', '/login');
    cy.contains('Login').should('be.visible');
  });
});