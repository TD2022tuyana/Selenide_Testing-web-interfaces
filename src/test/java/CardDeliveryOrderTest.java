import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDeliveryOrderTest {
    private String generateFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 3);
        Date futureDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(futureDate);
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        String futureDate = generateFutureDate();
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(futureDate);
    }

    @AfterEach
    public void tearDown() {
        // Close the browser or clear any data if needed
    }

    @Test
    public void testOrderForm() {

        // Fill out the form fields with valid values
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        // Check if the form's success notification is visible
        $("[data-test-id=notification]").waitUntil(visible, 15000);
        $("[data-test-id=notification]").shouldHave(text("Успешно! Встреча успешно забронирована на " + generateFutureDate()));
    }

    @Test
    public void testCityWithHyphen() {
        $("[data-test-id=city] input").setValue("Йошкар-Ола");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000);
        $("[data-test-id=notification]").shouldHave(text("Успешно! Встреча успешно забронирована на " + generateFutureDate()));
    }

    @Test
    public void testCityWithSpace() {
        $("[data-test-id=city] input").setValue("Нижний Новгород");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000);
        $("[data-test-id=notification]").shouldHave(text("Успешно! Встреча успешно забронирована на " + generateFutureDate()));
    }

    @Test
    public void testNameWithHyphen() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Михаил Салтыков-Щедрин");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000);
        $("[data-test-id=notification]").shouldHave(text("Успешно! Встреча успешно забронирована на " + generateFutureDate()));
    }

    @Test
    public void testNameWithApostrophe() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Шарль д’Артаньян");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id='name'].input_invalid .input__sub").getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorMessage);
    }

    @Test
    public void testShortName() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Го Сон");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000);
        $("[data-test-id=notification]").shouldHave(text("Успешно! Встреча успешно забронирована на " + generateFutureDate()));
    }

    @Test
    public void testUncheckedCheckbox() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id=agreement].input_invalid .checkbox__text").getText().trim();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", errorMessage);
    }

    @Test
    public void testEmptyCityField() {
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id=city].input_invalid .input__sub").getText().trim();
        assertEquals("Поле обязательно для заполнения", errorMessage);
    }

    @Test
    public void testEmptyNameField() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=phone] input").setValue("+79213124343");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id='name'].input_invalid .input__sub").getText().trim();
        assertEquals("Поле обязательно для заполнения", errorMessage);
    }

    @Test
    public void testEmptyPhoneField() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id=phone].input_invalid .input__sub").getText().trim();
        assertEquals("Поле обязательно для заполнения", errorMessage);
    }

    @Test
    public void testShortPhoneNumber() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Дмитрий Пучков");
        $("[data-test-id=phone] input").setValue("1234567");
        $("[data-test-id=agreement] span").click();
        $x("//*[@class='button__content']").click();
        String errorMessage = $("[data-test-id=phone].input_invalid .input__sub").getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorMessage);
    }

}