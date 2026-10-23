package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999"); // Открываем страницу входа в браузере
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $("button[type=button]").click(); // Кнопка "Войти"

        // Проверка, что мы вошли
        $("h2").shouldHave(text("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active"); // Пользователь не в базе

        $("[name=login]").setValue(notRegisteredUser.getLogin());
        $("[name=password]").setValue(notRegisteredUser.getPassword());
        $("button[type=button]").click();

        $(".notification").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[name=login]").setValue(blockedUser.getLogin());
        $("[name=password]").setValue(blockedUser.getPassword());
        $("button[type=button]").click();

        $(".notification").shouldHave(text("Ошибка! Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[name=login]").setValue(wrongLogin); // Неверный логин
        $("[name=password]").setValue(registeredUser.getPassword());
        $("button[type=button]").click();

        $(".notification").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(wrongPassword); // Неверный пароль
        $("button[type=button]").click();

        $(".notification").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }
}