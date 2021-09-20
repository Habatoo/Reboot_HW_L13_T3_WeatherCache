package Laptenkov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * Класс объекта {@link WeatherProvider},
 * запрашивает информацию о погоде из интернета.
 *
 * @author habatoo.
 */
public class WeatherProvider {

    private RestTemplate restTemplate;

    /**
     * Download ACTUAL weather info from internet.
     * You should call GET http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
     * You should use Spring Rest Template for calling requests
     *
     * @param city - city
     * @return weather info or null
     */
    public WeatherInfo get(String city) {

        String key = "bf621e74764b5f7df377c0d606b96e3e";
        String weatherUrl = String.format(
                "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, key);

        restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(weatherUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonNode main = root.path("main");
        JsonNode temperature = main.at("/temp");
        JsonNode feelsLikeTemperature = main.at("/feels_like");
        JsonNode pressure = main.at("/pressure");
        JsonNode wind = root.path("wind");
        JsonNode windSpeed = wind.at("/speed");
        JsonNode weather = root.path("weather");
        JsonNode shortDescription = weather.get(0).at("/main");
        JsonNode description = weather.get(0).at("/description");
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        WeatherInfo weatherInfo = WeatherInfo.builder()
                .setCity(city)
                .setShortDescription(shortDescription.toString())
                .setDescription(description.toString())
                .setTemperature(temperature.asDouble())
                .setFeelsLikeTemperature(feelsLikeTemperature.asDouble())
                .setWindSpeed(windSpeed.asDouble())
                .setPressure(pressure.asDouble())
                .setExpiryTime(expiryTime)
                .build();

        return weatherInfo;
    }
}