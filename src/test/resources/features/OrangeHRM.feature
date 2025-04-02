Feature: Admin User Management

  Scenario: Add and delete a user
    Given I launch the OrangeHRM login page
    When I login with username "Admin" and password "admin123"
    And I go to the Admin section
    Then I note down the current record count
    When I add a new user with username "TestUser"
    Then I verify that the record count increased by 1
    When I search for the saved user and delete the user
    Then I verify that the record count decreased by 1
