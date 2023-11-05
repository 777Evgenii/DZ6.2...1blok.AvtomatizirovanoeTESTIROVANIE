package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $(byXpath("//input[@name='login']")).setValue(registeredUser.getLogin());
        $(byXpath("//input[@name='password']")).setValue(registeredUser.getPassword());
        $(byXpath("//button[@data-test-id='action-login']")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $(byXpath("//input[@name='login']")).setValue(notRegisteredUser.getLogin());
        $(byXpath("//input[@name='password']")).setValue(notRegisteredUser.getPassword());
        $(byXpath("//button[@data-test-id='action-login']")).click();
        $(byXpath("//div[@data-test-id='error-notification' and contains(., 'Неверно указан логин или пароль')]")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $(byXpath("//input[@name='login']")).setValue(blockedUser.getLogin());
        $(byXpath("//input[@name='password']")).setValue(blockedUser.getPassword());
        $(byXpath("//button[@data-test-id='action-login']")).click();
        $(byXpath("//div[@data-test-id='error-notification' and contains(., 'Пользователь заблокирован')]")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $(byXpath("//input[@name='login']")).setValue(wrongLogin);
        $(byXpath("//input[@name='password']")).setValue(registeredUser.getPassword());
        $(byXpath("//button[@data-test-id='action-login']")).click();
        $(byXpath("//div[@data-test-id='error-notification' and contains(., 'Неверно указан логин или пароль')]")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $(byXpath("//input[@name='login']")).setValue(registeredUser.getLogin());
        $(byXpath("//input[@name='password']")).setValue(wrongPassword);
        $(byXpath("//button[@data-test-id='action-login']")).click();
        $(byXpath("//div[@data-test-id='error-notification' and contains(., 'Неверно указан логин или пароль')]")).shouldBe(visible, Duration.ofSeconds(10));
    }
}