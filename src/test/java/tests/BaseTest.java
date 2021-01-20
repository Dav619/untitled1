package tests;

import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.testng.SoftAsserts;
import io.qameta.allure.selenide.AllureSelenide;
import listeners.AllureReporter;
import org.openqa.selenium.JavascriptException;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.*;


@Listeners({AllureReporter.class, SoftAsserts.class})
public class BaseTest {

    SelenideElement title = $("#search-section > div > div.row > div > h1");

    @BeforeSuite
    public void initDriver() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .includeSelenideSteps(false)
                .savePageSource(false));
        assertionMode = AssertionMode.SOFT;
        baseUrl = "https://staff.am/";
        pageLoadStrategy = "eager";
        browser = Browsers.CHROME;
        pollingInterval = 500;
        holdBrowserOpen = false;
        savePageSource = false;
        startMaximized = true;
        fastSetValue = true;
        screenshots = false;
        headless = false;
        timeout = 20000;
    }

    @BeforeMethod
    public void beforeMethodSignIn() {
        open("/");
        sleep(5000);
        removePopup();
        removeRecaptcha();

    }
    public String  text(){
        String text = title.getText();
        return text;
    }

    @Test
    public void test(){
        Assert.assertEquals(text(), "CAREER SEARCH STARTS HERE.");

    }
    /**
     * Clear caches of browser.
     * You can call this method in tests.(If necessary)
     */
    protected void clearCaches() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
        refresh();
    }

    /**
     * Tear down.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        closeWebDriver();
    }

    private void removeRecaptcha() {
        try {
            executeJavaScript("document.querySelector('[ng-show='recaptcha.key']').remove()");
            executeJavaScript("document.querySelector('div.gift-bet-t-c.new-recaptcha-v').remove()");
        } catch (JavascriptException ignore) {
        }
    }

    private void removePopup() {
        try {
            executeJavaScript("document.querySelector('vbet-homework-popup').remove()");
        } catch (JavascriptException ignore) {
        }
    }
}
