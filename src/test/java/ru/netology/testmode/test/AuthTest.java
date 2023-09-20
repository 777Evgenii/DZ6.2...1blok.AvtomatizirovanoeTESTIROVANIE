package ru.netology.testmode.test;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
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
        $(".form [data-test-id='login'] .input__box .input__control").setValue(registeredUser.getLogin());
        $(".form [data-test-id='password'] .input__box .input__control").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $(".form [data-test-id='login'] .input__box .input__control").setValue(notRegisteredUser.getLogin());
        $(".form [data-test-id='password'] .input__box .input__control").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $(".form [data-test-id='login'] .input__box .input__control").setValue(blockedUser.getLogin());
        $(".form [data-test-id='password'] .input__box .input__control").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $(".form [data-test-id='login'] .input__box .input__control").setValue(wrongLogin);
        $(".form [data-test-id='password'] .input__box .input__control").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $(".form [data-test-id='login'] .input__box .input__control").setValue(registeredUser.getLogin());
        $(".form [data-test-id='password'] .input__box .input__control").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }
}