package steps;

import base.WebDriverManager;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.AdminPage;
import static org.testng.Assert.assertEquals;

public class OrangeHRMSteps {

    WebDriver driver = WebDriverManager.getDriver();
    LoginPage loginPage = new LoginPage(driver);
    AdminPage adminPage = new AdminPage(driver);
    int initialCount;
    String savedUsername; // 🔐 Save this to reuse in delete step

    @Given("I launch the OrangeHRM login page")
    public void launchPage() {
        driver.get("https://opensource-demo.orangehrmlive.com/");
    }

    @When("I login with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }

    @And("I go to the Admin section")
    public void goToAdmin() {
        adminPage.clickAdminTab();
    }

    @Then("I note down the current record count")
    public void getCount() {
        initialCount = adminPage.getRecordCount();
    }

    @When("I add a new user with username {string}")
    public void addUser(String baseUsername) {
        // Add a timestamp to make username unique
        savedUsername = baseUsername + System.currentTimeMillis();
        adminPage.clickAddButton();
        adminPage.fillUserForm(savedUsername);
        adminPage.clickSave();
    }

    @Then("I verify that the record count increased by 1")
    public void verifyIncrease() {
        assertEquals(adminPage.getRecordCount(), initialCount + 1);
    }
    @When("I search for the saved user and delete the user")
    public void deleteSavedUser() {
        adminPage.searchUser(savedUsername);
        adminPage.deleteUser(savedUsername);

    }


    int finalCount;

    @Then("I verify that the record count decreased by 1")
    public void verifyDecrease() {
        finalCount = adminPage.getRecordCount();
        System.out.println("Initial count: " + initialCount);
        System.out.println("Final count: " + finalCount);
        assertEquals(finalCount, initialCount, "❌ Record count mismatch after deletion.");
    }

}
