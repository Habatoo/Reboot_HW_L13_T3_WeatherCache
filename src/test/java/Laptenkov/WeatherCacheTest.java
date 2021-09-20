package Laptenkov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

/**
 * Класс {@link WeatherCacheTest} для тестирования публичных методов
 * класса {@link WeatherProvider}.
 * @author habatoo
 */
class WeatherCacheTest {

    WeatherCache weatherCache;
    WeatherProvider weatherProviderMock;
    WeatherInfo weatherInfo_1;
    WeatherInfo weatherInfo_2;

    /**
     * Инициализация mock объекта {@link WeatherProvider} перед каждым запуском тестов.
     * Инициализация экземпляра тестируемого класса {@link WeatherCache}.
     */
    @BeforeEach
    void setUp() {
        weatherProviderMock = Mockito.mock(WeatherProvider.class);
        weatherCache = new WeatherCache(weatherProviderMock);
        weatherInfo_1 = WeatherInfo.builder()
                .setCity("Moscow")
                .setShortDescription("Cloudy")
                .setDescription("Cool and cloudy")
                .setTemperature(17.2)
                .setFeelsLikeTemperature(13.5)
                .setWindSpeed(3.5)
                .setPressure(287.3)
                .setExpiryTime(LocalDateTime.now().plusMinutes(4))
                .build();

        weatherInfo_2 = WeatherInfo.builder()
                .setCity("Omsk")
                .setShortDescription("Hot")
                .setDescription("Hot and dry")
                .setTemperature(29.2)
                .setFeelsLikeTemperature(33.5)
                .setWindSpeed(0.5)
                .setPressure(292.3)
                .setExpiryTime(LocalDateTime.of(2021, 9, 30, 12, 12, 12))
                .build();
    }

    /**
     * Удаление созданных объектов {@link WeatherProvider}
     * и {@link WeatherCache}.
     */
    @AfterEach
    void tearDown() {
        weatherProviderMock = null;
        weatherCache = null;
        weatherInfo_1 = null;
        weatherInfo_2 = null;
    }

    /**
     * Метод {@link WeatherCacheTest#getNotCity_Test} проверяет значения, которое возвращает
     * метод {@link WeatherCache#getWeatherInfo(String)},
     * при подаче входящего параметра типа {@link String} с не существующим городом.
     * <br>Проверка количества вызовов метода {@link WeatherProvider#get(String)}.
     * <br>Сценарий, при котором {@link WeatherProvider#get(String)} возвращает <code>null</code>,
     * метод {@link WeatherCache#getWeatherInfo(String)} должен возвращать <code>null</code>.
     */
    @Test
    void getNotCity_Test() {
        Mockito.when(weatherProviderMock.get("qwerty")).thenReturn(null);
        WeatherInfo weatherInfo = weatherCache.getWeatherInfo("qwerty");
        Mockito.verify(weatherProviderMock, Mockito.times(1)).get("qwerty");
        Assertions.assertNull(weatherInfo);
    }

    /**
     * Метод {@link WeatherCacheTest#getCityNotInCash_Test} проверяет значения, которое возвращает
     * метод {@link WeatherCache#getWeatherInfo(String)},
     * при подаче входящего параметра типа {@link String} с существующим городом, который не находится в кеше.
     * <br>Проверка количества вызовов метода {@link WeatherProvider#get(String)}.
     * <br>Сценарий, при котором {@link WeatherCache#getWeatherInfo(String)} возвращает объект {@link WeatherInfo}
     * c обращением к методу {@link WeatherProvider#get(String)}.
     */
    @Test
    void getCityNotInCash_Test() {
        Mockito.when(weatherProviderMock.get("Moscow")).thenReturn(weatherInfo_1);
        WeatherInfo weatherInfo = weatherCache.getWeatherInfo("Moscow");
        Mockito.verify(weatherProviderMock, Mockito.times(1)).get("Moscow");
        Assertions.assertEquals("Moscow", weatherInfo.getCity());
        Assertions.assertEquals("Cloudy", weatherInfo.getShortDescription());
        Assertions.assertEquals(17.2, weatherInfo.getTemperature());
        Assertions.assertEquals(13.5, weatherInfo.getFeelsLikeTemperature());
        Assertions.assertEquals(3.5, weatherInfo.getWindSpeed());
    }

    /**
     * Метод {@link WeatherCacheTest#getCityInCash_Test} проверяет значения, которое возвращает
     * метод {@link WeatherCache#getWeatherInfo(String)},
     * при подаче входящего параметра типа {@link String} с существующим городом, который находится в кеше.
     * с истекшим сроком expiryTime.
     * <br>Проверка количества вызовов метода {@link WeatherProvider#get(String)}.
     * <br>Сценарий, при котором {@link WeatherCache#getWeatherInfo(String)} возвращает объект {@link WeatherInfo}
     * без обращения к {@link WeatherProvider#get(String)}.
     */
    @Test
    void getCityInCash_Test() {
        Mockito.when(weatherProviderMock.get("Moscow")).thenReturn(weatherInfo_1);
        weatherCache.getWeatherInfo("Moscow");
        WeatherInfo weatherInfo = weatherCache.getWeatherInfo("Moscow");
        Mockito.verify(weatherProviderMock, Mockito.times(1)).get("Moscow");
        Assertions.assertEquals("Moscow", weatherInfo.getCity());
        Assertions.assertEquals(17.2, weatherInfo.getTemperature());
    }

    /**
     * Метод {@link WeatherCacheTest#getCityInCashNotValidDate_Test} проверяет значения, которое возвращает
     * метод {@link WeatherCache#getWeatherInfo(String)},
     * при подаче входящего параметра типа {@link String} с существующим городом, который находится в кеше
     * с не истекшим сроком expiryTime.
     * <br>Проверка количества вызовов метода {@link WeatherProvider#get(String)}.
     * <br>Сценарий, при котором {@link WeatherCache#getWeatherInfo(String)} возвращает объект {@link WeatherInfo}
     * с обращением к методу {@link WeatherProvider#get(String)}.
     */
    @Test
    void getCityInCashNotValidDate_Test() {
        Mockito.when(weatherProviderMock.get("Omsk")).thenReturn(weatherInfo_2);
        weatherCache.getWeatherInfo("Omsk");
        WeatherInfo weatherInfo = weatherCache.getWeatherInfo("Omsk");
        Mockito.verify(weatherProviderMock, Mockito.times(1)).get("Omsk");
        Assertions.assertEquals("Omsk", weatherInfo.getCity());
        Assertions.assertEquals("Hot and dry", weatherInfo.getDescription());
        Assertions.assertEquals(29.2, weatherInfo.getTemperature());
        Assertions.assertEquals(33.5, weatherInfo.getFeelsLikeTemperature());
        Assertions.assertEquals(292.3, weatherInfo.getPressure());
    }

}