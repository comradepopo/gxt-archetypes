package com.sencha.gxt.test.it;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sencha.gxt.test.it.rules.Retry;

public abstract class BaseTest {

  private static final Logger logger = Logger.getLogger(BaseTest.class.getName());

  private static WebDriver last;

  protected WebDriver driver;

  @Rule
  public TestWatcher quitFailedDriver = new TestWatcher() {
    @Override
    protected void failed(Throwable e, Description description) {
      last.quit();
      last = null;
      driver = null;
    }
  };

  @Rule
  public Retry retry = new Retry(3);

  @Before
  public void startBrowser() throws IOException, URISyntaxException {
    if (last != null) {
      driver = last;
      last = null;
      return;
    }

    String server = System.getProperty("webdriver.remote.server");

    if (server == null) {
      driver = createFireFoxDriver();
    } else {
      driver = createRemoteWebDriver();
    }

    driver.manage().timeouts().setScriptTimeout(5000, TimeUnit.MILLISECONDS);
  }

  @After
  public void stopBrowser() {
    try {
      // make sure there is no alert present, failing the test
      Assert.assertTrue("Alert found when trying to close browser",
          ExpectedConditions.not(ExpectedConditions.alertIsPresent()).apply(driver));
      last = driver;
    } catch (Exception ex) {
      driver.quit();
      last = null;
    }
  }

  public Dimension getBrowserWindowSize() {
    return driver.manage().window().getSize();
  }

  public Dimension setBrowserWindowPosition(Point targetPosition) {
    driver.manage().window().setPosition(targetPosition);
    return driver.manage().window().getSize();
  }

  public Dimension setBrowserWindowMaximized() {
    driver.manage().window().maximize();
    return driver.manage().window().getSize();
  }

  public void setBrowserWindowSize(Dimension targetSize) {
    driver.manage().window().setSize(targetSize);
  }

  protected void gotoUrl(String url) {
    logger.info("gotoEntrypoint() + url=" + url);
    driver.get(url);
  }

  private FirefoxDriver createFireFoxDriver() throws IOException, URISyntaxException {
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    firefoxProfile.setPreference("browser.sessionstore.resume_from_crash", false);
    firefoxProfile.setPreference("app.update.auto", false);

    return new FirefoxDriver(firefoxProfile);
  }

  // TODO factor this up a step to a runner so that it can be run across many browsers
  private RemoteWebDriver createRemoteWebDriver() throws MalformedURLException {
    String server = System.getProperty("webdriver.remote.server");
    String browserRaw = System.getProperty("browser", "firefox");
    String platformRaw = System.getProperty("platform", Platform.getCurrent().name());

    DesiredCapabilities capabilities = new DesiredCapabilities(browserRaw, "",
        Enum.valueOf(Platform.class, platformRaw));

    return new RemoteWebDriver(new URL(server), capabilities);
  }

}
