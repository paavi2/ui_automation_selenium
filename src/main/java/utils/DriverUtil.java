package utils;

import driver.BaseDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class DriverUtil extends BaseDriver {
    private WebDriver driver;
    Actions actions ;

    String screenShots_Path = "outputs/screenshots/";
    public DriverUtil(){
        driver = getDriver();
        actions = new Actions(driver);
    }

    public void explicitWait(By byObj,int timeOut){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.elementToBeClickable(byObj));
    }

    public WebElement fluentWait(By obj, int timeOut){
        FluentWait wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(Exception.class);

        WebElement element = (WebElement) wait.until(new Function<WebDriver,WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(obj);
            }
        });
        return element;
    }

    public void getScreenshot(String fileName){
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        File destination = new File(screenShots_Path+fileName);

        try {
            Files.copy(screenshot.toPath(),destination.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WebElement findElement(By obj){
        return driver.findElement(obj);
    }

    public List<WebElement> findElements(By obj){
        return driver.findElements(obj);
    }

    public boolean isElementPresent(By obj, int seconds){
        try{
            explicitWait(obj,seconds);
            return findElement(obj).isDisplayed();
        }catch (Exception e){
            return false;
        }
    }

    public void click(By obj){
        fluentWait(obj,20).click();
    }

    public void sendKeys(By obj, String text){
        fluentWait(obj,20).sendKeys(text);
    }

    public void longPress(By obj){
        actions.clickAndHold(findElement(obj))
                .pause(Duration.ofMillis(3000))
                .release()
                .perform();
    }

    public void rightClick(By obj){
        actions.contextClick(findElement(obj)).perform();
    }

    public void leftClick(By obj){
        actions.click(findElement(obj)).perform();
    }

    public void dragAndDrop(By source, By destination){
        actions.dragAndDrop(findElement(source),findElement(destination)).perform();
    }

    public void scrollUpByCoords(){
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-500)");
    }

    public void scrollDownByCoords(){
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");
    }

    public void scrollLeftByCoords(){
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(-500,0)");
    }

    public void scrollRightByCoords(){
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(500,0)");
    }

    public void scrollToTheElement(By obj){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(obj));
    }

    public WebElement getElementInsideShadowDom(By shadowRootBy, By innerElement){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement rootEle = (WebElement)js.executeScript("return arguments[0].shadowRoot",findElement(shadowRootBy));
        return rootEle.findElement(innerElement);
    }

    public WebDriver switchToFrame(By obj){
        return driver.switchTo().frame(findElement(obj));
    }

    public WebDriver switchToMainFrame(By obj){
        return driver.switchTo().defaultContent();
    }

    public WebDriver switchToFrameByIndex(int index){
        return driver.switchTo().frame(index);
    }

    public WebDriver switchToFrameByText(String frameName){
        return driver.switchTo().frame(frameName);
    }

    public WebDriver switchToParentFrame(){
        return driver.switchTo().parentFrame();
    }

    public void acceptAlert(){
        driver.switchTo().alert().accept();
    }

    public void dismissAlert(){
        driver.switchTo().alert().dismiss();
    }

    public String getTextInAlert(){
        return driver.switchTo().alert().getText();
    }
}
