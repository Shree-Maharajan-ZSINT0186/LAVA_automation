@application
Feature: Create new application

  @launch
  Scenario: Create new application
  
    Given Launch the application
    And Navigate to the Application tab
    And Agent create a new application
    Then Application Id should be created
    Then The added application details should be saved sucessfully

  @createTitleAndParcles
  Scenario: Add Titles and Parcels
  When Agent should be navigated to the Title tab
    And Agent click on the Title search Icon
    And Agent add a Title

  @createParties
  Scenario: Add Parties
  Then Agent should be navigated to the Parties tab
    When Agent click on the Parties search Icon
    And Agent add a Party

    @createServies
  Scenario: Add Service
  Then Agent should be navigated to the Service tab
  Then Validation for the service should be displayed
  And Agent add a Service

