@assignToMyself
Feature: Assign application to examiner

  @assignToMyself
  Scenario: Assign application to myself

    Given Launch workbench the applications
    Then Navigate to workqueue and assign myself
    Then Search and click Application to process


  @cancelApplication
  Scenario: Cancel the application
    Given Examiner clicks the cancel button
    And   gives the reason for cancellation

