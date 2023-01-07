package scootertests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.praktikum_services.qa_scooter.MainPage;
import ru.praktikum_services.qa_scooter.OrderPage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class orderScooter {

    //Добавь необходимые поля
    private String button;
    private String name;
    private String lastname;
    private String address;
    private String metroStation;
    private String phoneNumber;
    private String rentalPeriod;

    public orderScooter(String button, String name, String lastname, String address, String metroStation, String phoneNumber, String rentalPeriod) {
        this.button = button;
        this.name = name;
        this.lastname = lastname;
        this.address = address;
        this.metroStation = metroStation;
        this.phoneNumber = phoneNumber;
        this.rentalPeriod = rentalPeriod;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderScooter() {
        //Сгенерируй тестовые данные
        return new Object[][] {
                {"upperButtonOrder", "Василий", "Васильев", "Москва", "Студенческая", "89999000000", "сутки"},
                {"lowerButtonOrder", "Иван", "Иванов", "Ростов", "Беляево", "89999000001", "двое суток"},
        };
    }

    private WebDriver driver;

    @Before
    public void setUp(){

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

     @Test
    public void testOrderScooter () {

        new MainPage(driver)
            .open()
            .scrollButtonOrder(button)
            .clickButtonOrder(button);

        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillInputName(name);
        orderPage.fillInputLastname(lastname);
        orderPage.fillInputAddress(address);
        orderPage.fillInputPhoneNumber(phoneNumber);
        orderPage.clickInputMetroStation();

        int p = 0;
         List<WebElement> station = driver.findElements(OrderPage.metroStationList());

        for (int i=0; i < station.size(); i++ ) {
            if (station.get(i).getText().equals(metroStation)){
               p=i;
               break;
            }
         }

         // скролинг до элемента
         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", station.get(p));

         // проверка кликабильности
         ExpectedConditions.elementToBeClickable(station.get(p));

         // нажатие на элемент списка «Станции метро»
         station.get(p).click();

         // нажатие кнопки далее
         orderPage.clickButtonNext();

         // вызов меню срока аренды
         orderPage.clickInputRentalPeriod();

         // выбор срока аренды
         p = 0;
         List<WebElement> rental = driver.findElements(OrderPage.getRentalPeriodList());

         for (int i=0; i < rental.size(); i++ ) {
             if (rental.get(i).getText().equals(metroStation)){
                 p=i;
                 break;
             }
         }

         // скролинг до элемента
         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", rental.get(p));

         // проверка кликабильности
         ExpectedConditions.elementToBeClickable(rental.get(p));

         // нажатие на элемент списка «Станции метро»
         rental.get(p).click();

         // определение текущей даты
         Date dateNow = new Date();
         SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");

         // ввод текущей даты
         orderPage.fillInputWhenBring(formatForDateNow.format(dateNow));

         // завершение заказа
         orderPage.clickButtonOrder();
         orderPage.clickButtonConfirmationOrder();
         assertTrue(driver.findElement(OrderPage.getHeaderStatusOrder()).getText().equals("Заказ оформлен"));
    }


    @After
    public void tearDown() {
        driver.quit(); // Закрыть браузер
    }
}
