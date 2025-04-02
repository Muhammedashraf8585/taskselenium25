package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AdminPage {

    WebDriver driver;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Sidebar Admin tab
    @FindBy(xpath = "//span[text()='Admin']")
    WebElement adminTab;

    // Add Button
    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement addButton;

    // User Role Dropdown
    @FindBy(xpath = "//label[text()='User Role']/../following-sibling::div//div[contains(@class,'oxd-select-text')]")
    WebElement userRoleDropdown;

    // Status Dropdown
    @FindBy(xpath = "//label[text()='Status']/../following-sibling::div//div[contains(@class,'oxd-select-text')]")
    WebElement statusDropdown;

    // Employee Name
    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    WebElement employeeNameInput;

    // Username field
    @FindBy(xpath = "//label[text()='Username']/../following-sibling::div/input")
    WebElement usernameInput;

    // Password
    @FindBy(xpath = "//label[text()='Password']/../following-sibling::div/input")
    WebElement passwordInput;

    // Confirm Password
    @FindBy(xpath = "//label[text()='Confirm Password']/../following-sibling::div/input")
    WebElement confirmPasswordInput;

    // Save button
    @FindBy(xpath = "//button[normalize-space()='Save']")
    WebElement saveButton;

    // Search input (for username)
    @FindBy(xpath = "//label[text()='Username']/../following-sibling::div/input")
    WebElement searchUsernameInput;

    // Search button
    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    // Delete button (first result row)
    @FindBy(xpath = "//div[@role='rowgroup']//button[@type='button' and contains(@class,'oxd-icon-button')]")
    WebElement deleteButton;

    // Confirm delete in modal
    @FindBy(xpath = "//button[normalize-space()='Yes, Delete']")
    WebElement confirmDeleteButton;

    // Table rows
    @FindBy(xpath = "//div[@class='oxd-table-card']")
    List<WebElement> tableRows;

    // ========================
    // PAGE ACTION METHODS
    // ========================

    public void clickAdminTab() {
        adminTab.click();
    }

    public int getRecordCount() {
        return tableRows.size();  // Counts visible rows in the table
    }

    public void clickAddButton() {
        addButton.click();
    }

    public void fillUserForm(String username) {
        // Select User Role
        userRoleDropdown.click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='ESS']")).click();

        // Select Status
        statusDropdown.click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Enabled']")).click();

        // Type part of employee name and wait
        employeeNameInput.sendKeys("mo");

        // Wait and select from the dropdown
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstSuggestion = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@role='listbox']//span")));
        firstSuggestion.click();

        // Enter username and password
        usernameInput.sendKeys(username);
        passwordInput.sendKeys("Test@1234a");
        confirmPasswordInput.sendKeys("Test@1234a");
    }

    public void clickSave() {
        saveButton.click();
    }

    public void searchUser(String username) {
        searchUsernameInput.clear();
        searchUsernameInput.sendKeys(username);
        searchButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='rowgroup']//div[contains(text(),'" + username + "')]")
        ));
    }

    public void deleteUser(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Locate the row containing the username
            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@role='rowgroup']//div[text()='" + username + "']/ancestor::div[contains(@class,'oxd-table-card')]")
            ));

            // Scroll row into view
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);
            Thread.sleep(500);  // Optional buffer for smooth scroll

            // Locate the checkbox inside the row
            WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));

            // Wait until the checkbox is clickable (if fails, fallback to JS click)
            try {
                wait.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
            } catch (TimeoutException e) {
                System.out.println("⚠️ Fallback to JavaScript click for checkbox");
                js.executeScript("arguments[0].click();", checkbox);
            }

            // Now click the delete (trash) icon
            WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='button']//i[contains(@class,'bi-trash')]")
            ));
            deleteIcon.click();

            // Confirm deletion
            WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Yes, Delete']")
            ));
            confirmDelete.click();

        } catch (Exception e) {
            System.out.println("❌ Failed to delete user: " + username + " — " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
