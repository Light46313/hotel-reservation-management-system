package hotel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.model.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WeatherService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // =========================================================
    // THREAD POOL
    // =========================================================

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(2);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public WeatherService() {

        httpClient = HttpClient.newHttpClient();

        objectMapper = new ObjectMapper();
    }


    // =========================================================
    // GET WEATHER
    // =========================================================

    public Weather getWeather(String city) throws Exception {

        // =====================================================
        // STEP 1: Find city coordinates using Geocoding API
        // =====================================================

        String encodedCity = java.net.URLEncoder
                .encode(
                        city,
                        java.nio.charset.StandardCharsets.UTF_8
                );

        String geocodingUrl =
                "https://geocoding-api.open-meteo.com/v1/search"
                        + "?name=" + encodedCity
                        + "&count=1"
                        + "&language=en"
                        + "&format=json";


        HttpRequest geocodingRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(geocodingUrl))
                        .GET()
                        .build();


        // =====================================================
        // STEP 2: Send Geocoding HTTP Request
        // =====================================================

        HttpResponse<String> geocodingResponse =
                httpClient.send(
                        geocodingRequest,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (geocodingResponse.statusCode() != 200) {

            throw new IOException(
                    "Geocoding API request failed. HTTP status: "
                            + geocodingResponse.statusCode()
            );
        }


        // =====================================================
        // STEP 3: Parse Geocoding JSON
        // =====================================================

        JsonNode geocodingJson =
                objectMapper.readTree(
                        geocodingResponse.body()
                );


        JsonNode results =
                geocodingJson.get("results");


        if (results == null || results.isEmpty()) {

            throw new IOException(
                    "City not found: " + city
            );
        }


        JsonNode location =
                results.get(0);


        double latitude =
                location.get("latitude").asDouble();


        double longitude =
                location.get("longitude").asDouble();


        String locationName =
                location.get("name").asText();


        // =====================================================
        // STEP 4: Build Weather API URL
        // =====================================================

        String weatherUrl =
                "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=" + latitude
                        + "&longitude=" + longitude
                        + "&current=temperature_2m,relative_humidity_2m,weather_code"
                        + "&temperature_unit=celsius";


        HttpRequest weatherRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(weatherUrl))
                        .GET()
                        .build();


        // =====================================================
        // STEP 5: Send Weather HTTP Request
        // =====================================================

        HttpResponse<String> weatherResponse =
                httpClient.send(
                        weatherRequest,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (weatherResponse.statusCode() != 200) {

            throw new IOException(
                    "Weather API request failed. HTTP status: "
                            + weatherResponse.statusCode()
            );
        }


        // =====================================================
        // STEP 6: Parse Weather JSON
        // =====================================================

        JsonNode weatherJson =
                objectMapper.readTree(
                        weatherResponse.body()
                );


        JsonNode current =
                weatherJson.get("current");


        if (current == null) {

            throw new IOException(
                    "Current weather data was not found."
            );
        }


        double temperature =
                current
                        .get("temperature_2m")
                        .asDouble();


        int humidity =
                current
                        .get("relative_humidity_2m")
                        .asInt();


        int weatherCode =
                current
                        .get("weather_code")
                        .asInt();


        // =====================================================
        // STEP 7: Convert Weather Code to Text
        // =====================================================

        String condition =
                getWeatherCondition(weatherCode);


        // =====================================================
        // STEP 8: Create Weather Object
        // =====================================================

        return new Weather(
                locationName,
                temperature,
                humidity,
                condition
        );
    }


    // =========================================================
    // ASYNCHRONOUS WEATHER REQUEST
    // =========================================================

    public Future<Weather> getWeatherAsync(String city) {

        return executorService.submit(() -> {

            return getWeather(city);

        });
    }


    // =========================================================
    // WEATHER CODE CONVERSION
    // =========================================================

    private String getWeatherCondition(int weatherCode) {

        return switch (weatherCode) {

            case 0 ->
                    "Clear Sky";

            case 1, 2, 3 ->
                    "Mainly Clear / Cloudy";

            case 45, 48 ->
                    "Fog";

            case 51, 53, 55 ->
                    "Drizzle";

            case 56, 57 ->
                    "Freezing Drizzle";

            case 61, 63, 65 ->
                    "Rain";

            case 66, 67 ->
                    "Freezing Rain";

            case 71, 73, 75, 77 ->
                    "Snow";

            case 80, 81, 82 ->
                    "Rain Showers";

            case 85, 86 ->
                    "Snow Showers";

            case 95 ->
                    "Thunderstorm";

            case 96, 99 ->
                    "Thunderstorm with Hail";

            default ->
                    "Unknown";
        };
    }


    // =========================================================
    // SHUTDOWN THREAD POOL
    // =========================================================

    public void shutdown() {

        executorService.shutdown();
    }
}