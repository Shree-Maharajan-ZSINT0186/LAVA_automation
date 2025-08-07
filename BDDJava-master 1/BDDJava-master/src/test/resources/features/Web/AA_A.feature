@application
Feature: Create new application with existing titles, assign myself and cancel the application

  @createApplication
  Scenario: Create new application with existing titles and examiner cancel application.

    Given Launch the application
    And Navigate to the Application tab
    And Agent create a new application
    Then Application Id should be created
    Then The added application details should be saved sucessfully

    When Agent should be navigated to the Title tab
#    And Agent click on the Title search Icon
    And Agent add Titles
    Then Agent should be navigated to the Parties tab

    When Agent click on the Parties search Icon
    And Agent add a Party
    Then Agent should be navigated to the Service tab

    And Agent add a Mortgage Registration Service
    And Agent add a Lease Registration Service
    And Agent add a Transfer of Ownership Service
    And Agent Validates and Lodge the application

    Given Launch workbench the application
    And Navigate to workqueue and assign myself
    Then Search and click Application to process

    Given Examiner clicks the cancel button
    And   gives the reason for cancellation

