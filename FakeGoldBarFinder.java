import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class FakeGoldBarFinder {

    private WebDriver driver;

    public FakeGoldBarFinder() {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        this.driver = new ChromeDriver();
    }

    public void findFakeGoldBar() {
        driver.get("http://sdetchallenge.fetch.com/");

        // Implement the algorithm to find the fake gold bar
        List<Integer> bars = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            bars.add(i);
        }

        int fakeBar = findFake(bars);
        System.out.println("Fake bar found: " + fakeBar);

        // Click the button corresponding to the fake bar
        driver.findElement(By.xpath("//button[text()='" + fakeBar + "']")).click();

        // Get the alert message
        Alert alert = driver.switchTo().alert();
        String alertMessage = alert.getText();
        alert.accept();

        System.out.println(alertMessage);
        driver.quit();
    }

    private int findFake(List<Integer> bars) {
        // Implement the algorithm for finding the fake bar
        if (bars.size() == 1) {
            return bars.get(0);
        }

        int groupSize = bars.size() / 3;
        List<Integer> group1 = bars.subList(0, groupSize);
        List<Integer> group2 = bars.subList(groupSize, 2 * groupSize);
        List<Integer> group3 = bars.subList(2 * groupSize, bars.size());

        int result = weighGroups(group1, group2);
        if (result == 0) {
            return findFake(group3);
        } else if (result == -1) {
            return findFake(group1);
        } else {
            return findFake(group2);
        }
    }

    private int weighGroups(List<Integer> group1, List<Integer> group2) {
        // Fill the bowls with group1 and group2
        fillBowls(group1, group2);

        // Click the weigh button
        driver.findElement(By.id("weigh")).click();

        // Get the result
        WebElement resultElement = driver.findElement(By.id("result"));
        String resultText = resultElement.getText();

        // Reset the bowls
        driver.findElement(By.id("reset")).click();

        if (resultText.contains("left")) {
            return -1;
        } else if (resultText.contains("right")) {
            return 1;
        } else {
            return 0;
        }
    }

    private void fillBowls(List<Integer> group1, List<Integer> group2) {
        for (int i = 0; i < group1.size(); i++) {
            WebElement leftCell = driver.findElement(By.xpath("//div[@id='left']//input[@data-index='" + i + "']"));
            leftCell.sendKeys(String.valueOf(group1.get(i)));
        }
        for (int i = 0; i < group2.size(); i++) {
            WebElement rightCell = driver.findElement(By.xpath("//div[@id='right']//input[@data-index='" + i + "']"));
            rightCell.sendKeys(String.valueOf(group2.get(i)));
        }
    }

    public static void main(String[] args) {
        FakeGoldBarFinder finder = new FakeGoldBarFinder();
        finder.findFakeGoldBar();
    }
}
