@application
Feature: Create new application using existing titles and approve application

  @launch
  Scenario: Create new application using existing titles and approve application
    Given Launch the application
    And Navigate to the Application tab
    And Agent create a new application
    Then Application Id should be created
    Then The added application details should be saved successfully
    Then Agent should be navigated to the Title tab
    And Agent add Titles
    Then Agent should be navigated to the Parties tab
    When Agent click on the Parties search Icon
    And Agent add a Party
    Then Agent should be navigated to the Service tab
    And Agent add a Mortgage Registration Service
    And Agent add a Lease Registration Service
    And Agent add a Transfer of Ownership Service
    And Agent Validates and Lodge the application

    Given Launch workbench the applications
    Then Navigate to workqueue and assign myself and click process
    And Examiner resets the layout
    And Examiner clicks on the mortgage process icon
    And Examiner validates and completes mortgage registration service
    And Examiner clicks on the lease process icon
    And Examiner cancels the lease registration service
    And Examiner clicks on the Transfer Of Ownership process icon
    And Examiner validates and completes Transfer Of Ownership service
    And Examiner reverts the service
    And Examiner clicks on the Transfer Of Ownership process icon
    And Examiner validates and completes Transfer Of Ownership service
    And Examiner clicks on the approve icon

