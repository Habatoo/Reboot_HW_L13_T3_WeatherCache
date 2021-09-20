package Laptenkov;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс объекта {@link WeatherCache} реализует потокобезопасный кеш,
 * который позволяет получить актуальную информацио о погоде.
 * Информация считается актуальной в течение 5 минут после загрузки из интернета.
 * @author habatoo.
 */
public class WeatherCache {

    private final Map<String, WeatherInfo> cache = new HashMap<>();
    private final WeatherProvider weatherProvider;

    /**
     * Конструктор объекта {@link WeatherCache}.
     *
     * @param weatherProvider - weather provider
     */
    public WeatherCache(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Метод {@link WeatherCache#getWeatherInfo(String)} объекта {@link WeatherCache}
     * реализует обновление информации по погоде в городах и
     * сохранение полученной информации в кеше.
     * Получает ауктуальную информацию по запрашиваемому городу и возвращает объект
     * {@link WeatherInfo} или null если такого города не существует.
     * Если информация в кеше не акутальная, либо отсуствует, то происходит
     * загрузка инормации в кеш. Значение expiryTime устанавливается как время скачивания
     * плюс 5 минут. При наличии актуальной информации в кеше загрузка информации не производится.
     *
     * @param city город для запроса.
     * @return возвращает объект {@link WeatherInfo} с информацией об актуальной погода.
     */
    public WeatherInfo getWeatherInfo(String city) {

        WeatherInfo weatherInfo = cache.get(city);

        /**
         * Позитивный сценарий: Кеш пустой, происходит загрузка новой информации по погоде из интернета.
         *
         * Запрос информации по не существующему городу (Например, по городу 'qwerty').
         * Происходит попытка загрузки информации по несуществующему городу, которая возвращает null.
         */
        if (weatherInfo == null) {
            weatherInfo = weatherProvider.get(city);
            if (weatherInfo != null) {
                synchronized (this) {
                    cache.put(city, weatherInfo);
                }
            }
        }

        /**
         * Получение актуальной информации по погоде из кеша: Кеш содержит актуальную информацию.
         * Запрос в интернет не должен выполняться
         */
        if (weatherInfo != null && weatherInfo.getExpiryTime().isAfter(LocalDateTime.now())) {
            weatherInfo = cache.get(city);
        }

        /**
         * Кеш содержит НЕактуальную информацию.
         * Кеш содержит НЕактальную информацию,
         * происходит загрузка новой информации по погоде из интернета.
         */
        if (weatherInfo != null && weatherInfo.getExpiryTime().isBefore(LocalDateTime.now())) {
            synchronized (this) {
                removeWeatherInfo(city);
                weatherInfo = weatherProvider.get(city);
                cache.put(city, weatherInfo);
            }
        }

        return weatherInfo;
    }

    /**
     * Метод {@link WeatherCache#removeWeatherInfo(String)} объекта {@link WeatherCache}
     * реализует удаление не актуальной информации из кеша.
     **/
    public void removeWeatherInfo(String city) {
        cache.remove(city);
    }

}