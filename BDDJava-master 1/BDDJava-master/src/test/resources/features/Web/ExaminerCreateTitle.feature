@application
Feature: Create new title

  Scenario: Launch the application
    Given Launch workbench the application

  @createTitle
  Scenario Outline: Create new title after launch
    When Navigate to the standalone title tab
    And Add party details of new title
    And Add parcel details of new title
    And Get the newTitleID
    Then enter the new title details

    Examples:
      | iteration |
      | 1         |
      | 2         |
      | 3         |



#  @createTitleAndParcles
#  Scenario: Add Titles and Parcels
#    When Agent should be navigated to the Title tab
#    And Agent click on the Title search Icon
#    And Agent add a Title
#
#  @createParties
#  Scenario: Add Parties
#    Then Agent should be navigated to the Parties tab
#    When Agent click on the Parties search Icon
#    And Agent add a Party
#
#  @createServies
#  Scenario: Add Service
#    Then Agent should be navigated to the Service tab
#    Then Validation for the service should be displayed
#    And Agent add a Service