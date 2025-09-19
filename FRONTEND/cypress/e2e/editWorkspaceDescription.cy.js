describe('Edit Workspace Dialog', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/workspaces');
    cy.contains('My Workspaces').click();
    cy.contains('My Workspaces').should('have.attr', 'aria-selected', 'true');
  });

  it('should edit the workspace name and save successfully', () => {
    cy.contains('button', 'Edit').first().click();
    cy.contains('Edit Workspace').should('be.visible');

    cy.get('input[name="description"]').clear().type('New Workspace Description');

    cy.get('#edit-button').click();
    cy.contains('Edit Workspace').should('not.exist');
    cy.contains('New Workspace Description').should('be.visible');

    cy.contains('button', 'Edit').first().click();
    cy.contains('Edit Workspace').should('be.visible');

    cy.get('input[name="description"]').clear().type('Old Workspace Description');

    cy.get('#edit-button').click();
    cy.contains('Edit Workspace').should('not.exist');
    cy.contains('Old Workspace Description').should('be.visible');
  });
});