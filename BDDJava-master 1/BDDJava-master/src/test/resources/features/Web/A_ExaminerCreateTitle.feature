@createTitleFeature
Feature: Create new title

  Scenario: Launch the application
    Given Launch workbench the application

  @createTitle
  Scenario: Create new title after launch
    And I create new titles for 4 iterations
