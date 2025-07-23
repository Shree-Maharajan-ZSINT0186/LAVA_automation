Feature: Login to the LAC application

  @login
  Scenario: Login to the LAC application
    Given Agent want to launch the application
    And Agent should see the login page
    And Agent select the Tenant value
    When Agent enter "UserName" and "Password"
    And Agent click on the login button
    Then Agent should see the dashboard

  @logout
  Scenario: Logout to the LAC application
    When Agent click on the profile icon
    And Agent click the Logout button
    
    
