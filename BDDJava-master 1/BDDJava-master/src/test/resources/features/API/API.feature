@API
Feature: Authorized login with Demo application


  @Demo2
  Scenario: Upload Images
    Given I want to fetch the payload "@uploadImage"
    When Get the end points "uploadImage"
    Then Upload the images
    
  @Demo1
  Scenario: Create New Pet Store
    Given I want to fetch the payload "@newPetStore"
    When Get the end points "newPetStore"
    Then Create new pet store
        
  @Demo
  Scenario: Get Pet ID
   Given I want to fetch the payload "@uploadImage"
    When Get the end points "getPetID"
    Then Get Pet ID
    
   @Demo
  Scenario: Delete Pet ID
   Given I want to fetch the payload "@uploadImage"
    When Get the end points "getPetID"
    Then Delete Pet ID
    
    @DemoKriti
  Scenario: Retrieve the Book details
   Given I want to fetch the payload "@Books"
    When Get the end points "getBookId"
    Then Get user ID
    
    @DemoKriti
  Scenario: Order a Book
   Given I want to fetch the payload "@BookOrder"
    When Get the end points "postOrder"
    Then Create a new order
    
    @DemoKriti
  Scenario: Update The Order
   Given I want to fetch the payload "@EditOrder"
    When Get the end points "patchOrder"
    Then Edit The Order
    

 