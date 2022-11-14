package tests;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class PobedaSiteTests extends TestBase {

    @DisplayName("Проверка перехода в разделы сайта.")
    @ValueSource(strings = {"Ручная кладь", "Багаж", "Выбор места", "Страхование"})
    @ParameterizedTest(name = "Выполняется переход в раздел \"{0}\"")
    void sitePobedaPageTest(String testData) {
        step("Открытие главной страницы сайта 'Победа'", () -> {
            open("");
        });
        step("Переход в нужный раздел", () -> {
            $$(".offer_title").find(text(testData)).click();
        });
        step("Проверка заголовка на странице", () -> {
            $(".title-inner").shouldHave(text(testData));
        });
    }

    static Stream<Arguments> pobedaSiteMenuTest() {
        return Stream.of(
                Arguments.of("English", List.of("Hand baggage", "Baggage", "Seats", "Insurance", "Hotels", "Auto", "Transfer", "Excursions")),
                Arguments.of("Русский", List.of("Ручная кладь", "Багаж", "Выбор места", "Страхование", "Отели", "Авто", "Трансфер", "Экскурсии"))
        );
    }

    @DisplayName("Проверка наличия разделов сайта.")
    @MethodSource
    @ParameterizedTest(name = "Для локали {0} отображаются разделы {1}")
    void pobedaSiteMenuTest(String lang, List<String> expectedTitle) {
        step("Открытие главной страницы сайта 'Победа'", () -> {
            open("");
        });
        step("Выбор языка", () -> {
            $("#idLanguageSelector").pressEnter();
            $$(".ui-dropdown_item_option_name").find(text(lang)).click();
        });
        step("Проверка отображения нужных разделов", () -> {
            $$(".offer_title").filter(visible).shouldHave(CollectionCondition.texts(expectedTitle));
        });
    }


    static Stream<Arguments> sitePobedaMenuEnumTest() {
        return Stream.of(
                Arguments.of(Lang.ENGLISH, List.of("Online check-in", "Manage my booking", "Info/Rules")),
                Arguments.of(Lang.РУССКИЙ, List.of("Забронировать билет", "Управление бронированием", "Онлайн-регистрация", "Информация"))
        );
    }

    @DisplayName("Проверка наличия кнопок меню сайта.")
    @MethodSource
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки меню {1}")
    void sitePobedaMenuEnumTest(Lang lang, List<String> expectedButtons) {
        step("Открытие главной страницы сайта 'Победа'", () -> {
            open("");
        });
        step("Выбор языка", () -> {
            $("#idLanguageSelector").pressEnter();
            $$(".ui-dropdown_item_option_name").find(text(lang.name())).click();
        });
        step("Проверка отображения нужных кнопок", () -> {
            $$(".button_label").filter(visible).shouldHave(CollectionCondition.texts(expectedButtons));
        });
    }


    @CsvSource(value = {
            "вылет, Онлайн-табло",
            "возврат, Возврат и обмен авиабилетов",
            "выбор места, Выбор места",
            "способ оплаты, Способы оплаты"
    })

    @DisplayName("Проверка работы поиска.")
    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса \"{0}\"")
    void sitePobedaSearchTest(String testData, String expectedResult) {
        step("Открытие главной страницы сайта 'Победа'", () -> {
            open("");
        });
        step("Ввод поискового запроса", () -> {
            $(".web-search_trigger_button").click();
            $("#webSearchId").setValue(testData).pressEnter();
        });
        step("Проверка результата поиска", () -> {
            $$("li.searchresults_list_item").first().shouldHave(text(expectedResult));
        });
    }


    @DisplayName("Проверка нотификации обязательности заполнение поля ввода Email для подписки.")
    @EnumSource(Lang.class)
    @ParameterizedTest(name = "Для локали {0} проверяется заполнение Email")
    void pobedaSiteEmailTest(Lang lang) {
        step("Открытие главной страницы сайта 'Победа'", () -> {
            open("");
        });
        step("Выбор языка", () -> {
            $("#idLanguageSelector").pressEnter();
            $$(".ui-dropdown_item_option_name").find(text(lang.name())).click();
        });
        step("Проверка сообщения обязательности заполнения Email", () -> {
            $(".newsletter_form_button").click();
            $(".newsletter_feedback_message").shouldBe(visible);
        });
    }
}
