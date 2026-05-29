package java.ua.kpi.charity.utils;

public class ErrorMessages {
    public static final String UNKNOWN_MODE_ERROR = "Помилка: невідомий режим роботи екрану";
    public static final String EMPTY_FIELDS_ERROR = "Усі обов'язкові поля мають бути заповнені!";
    public static final String CAMPAIGN_NAME_TOO_SHORT_ERROR = "Назва кампанії має містити щонайменше 10 символів!";
    public static final String ORGANIZER_NAME_TOO_SHORT_ERROR = "ПІБ має бути не коротшим за 8 символів!";
    public static final String ORGANIZER_NAME_FORMAT_ERROR = "Введіть повне ПІБ (Прізвище, Ім'я, По-батькові, розділені пробілами)!";
    public static final String START_DATE_TOO_EARLY_ERROR = "Дата початку не може бути меншою за сьогоднішню!";
    public static final String START_DATE_TOO_EARLY_TOAST = "Некоректні дати: Дата початку не може бути в минулому.";
    public static final String END_DATE_EARLIER_THEN_START_ERROR = "Дата кінця має бути більшою за дату початку!";
    public static final String END_DATE_EARLIER_THEN_START_TOAST = "Некоректні дати: Дата кінця має бути після дати початку.";
    public static final String AMOUNT_ERROR = "Сума має бути більшою за 0!";
    public static final String AMOUNT_FORMAT_ERROR = "Некоректний формат суми!";
    public static final String DELETE_CAMPAIGN_ERROR = "Помилка: неможливо видалити кампанію із зібраними коштами.";
}
