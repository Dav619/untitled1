package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.Integer.parseInt;

public class BasePage {
    public static Map<String, String> localMap = new HashMap<>();
    public static Set<Cookie> cookies = new HashSet<>();

    public static void getStorage() {
        LocalStorage localStorage = ((WebStorage) getWebDriver()).getLocalStorage();
        for (String s : localStorage.keySet()) {
            localMap.put(s, localStorage.getItem(s));
        }
        cookies = getWebDriver().manage().getCookies();
    }

    public static void setStorage() {
        Selenide.sleep(1000);
        for (Map.Entry<String, String> item : localMap.entrySet()) {
            ((WebStorage) getWebDriver()).getLocalStorage().setItem(item.getKey(), item.getValue());
        }
        for (Cookie c : cookies) {
            getWebDriver().manage().addCookie(c);
        }
    }

    public SelenideElement toFrame(SelenideElement element) {
        switchTo().defaultContent();
        try {
            if (!$$(element.getSearchCriteria()).isEmpty()) {
                return element;
            }
            for (int j = 0; j < 10; j++) {
                switchTo().defaultContent();
                int iframe = $$("iframe").size();
                for (int i = 0; i < iframe; i++) {
                    switchTo().defaultContent();
                    switchTo().frame(i);
                    if (!$$(element.getSearchCriteria()).isEmpty()) {
                        return element;
                    } else {
                        Selenide.sleep(500);
                    }
                }
            }
        } catch (NoSuchElementException ignore) {
        }
        return element;
    }

    protected void clickByJs(SelenideElement element) {
        element.click(usingJavaScript());
    }

    protected Integer getIntFromString(SelenideElement element) {
        int integer = 0;
        try {
            integer = parseInt(element.getText().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ignore) {
        }
        return integer;
    }
}
