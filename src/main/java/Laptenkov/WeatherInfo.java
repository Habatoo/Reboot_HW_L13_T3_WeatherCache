package Laptenkov;

import java.time.LocalDateTime;

/**
 * Класс объекта {@link WeatherInfo},
 * через объект {@link Builder} создает объект {@link WeatherInfo} в
 * котором сохраняется запрошенная информация о погоде из интернета.
 * @author habatoo.
 */
public class WeatherInfo {

    private String city;

    /**
     * Short weather description
     * Like 'sunny', 'clouds', 'raining', etc
     */
    private String shortDescription;

    /**
     * Weather description.
     * Like 'broken clouds', 'heavy raining', etc
     */
    private String description;

    /**
     * Temperature.
     */
    private double temperature;

    /**
     * Temperature that fells like.
     */
    private double feelsLikeTemperature;

    /**
     * Wind speed.
     */
    private double windSpeed;

    /**
     * Pressure.
     */
    private double pressure;

    /**
     * Expiry time of weather info.
     * If current time is above expiry time then current weather info is not actual!
     */
    private LocalDateTime expiryTime;

    public String getCity() {
        return city;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    /**
     * Конструктор класса {@link WeatherInfo}.
     * в качестве входного параметра принимает объект {@link Builder}
     * с необходимым количеством инициализированных полей.
     *
     * @param builder
     */
    public WeatherInfo(Builder builder) {
        if (null == builder) {
            throw new IllegalArgumentException("Provide correct builder");
        }

        this.city = builder.city;
        this.shortDescription = builder.shortDescription;
        this.description = builder.description;
        this.temperature = builder.temperature;
        this.feelsLikeTemperature = builder.feelsLikeTemperature;
        this.windSpeed = builder.windSpeed;
        this.pressure = builder.pressure;
        this.expiryTime = builder.expiryTime;
    }

    /**
     * Метод {@link Builder#builder} использует паттерн Builder
     * возвращает
     *
     * @return объекта типа {@link Builder}.
     */
    public static Builder builder() {
        Builder builder = new Builder();
        return builder;
    }

    /**
     * Класс {@link Builder} использует паттерн Builder
     * для создания объекта типа {@link WeatherInfo}.
     */
    public static class Builder {
        private String city;
        private String shortDescription;
        private String description;
        private double temperature;
        private double feelsLikeTemperature;
        private double windSpeed;
        private double pressure;
        private LocalDateTime expiryTime;

        public Builder() {
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTemperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setFeelsLikeTemperature(double feelsLikeTemperature) {
            this.feelsLikeTemperature = feelsLikeTemperature;
            return this;
        }

        public Builder setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder setPressure(double pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder setExpiryTime(LocalDateTime expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        /**
         * Метод {@link Builder#build} для создания объекта типа {@link WeatherInfo}.
         *
         * @return объект типа {@link WeatherInfo}.
         */
        public WeatherInfo build() {
            WeatherInfo weatherInfo = new WeatherInfo(this);
            return weatherInfo;
        }

        /**
         * Отображение всех полей объекта {@link WeatherInfo}.
         *
         * @return строку String с полями объекта {@link WeatherInfo}.
         */
        @Override
        public String toString() {
            return "Builder{" +
                    "city='" + city + '\'' +
                    ", shortDescription='" + shortDescription + '\'' +
                    ", description='" + description + '\'' +
                    ", temperature=" + temperature +
                    ", feelsLikeTemperature=" + feelsLikeTemperature +
                    ", windSpeed=" + windSpeed +
                    ", pressure=" + pressure +
                    ", expiryTime=" + expiryTime +
                    '}';
        }
    }

}