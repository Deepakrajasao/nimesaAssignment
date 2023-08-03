package nimesaAssingnment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Scanner;

public class apiReport {

    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22"
            + API_KEY;

    private static JSONObject getWeatherData(String url) throws IOException, JSONException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return new JSONObject(response.toString());
    }

    private static double getTemperature(JSONObject weatherData, String date) throws JSONException {
        JSONArray data = weatherData.getJSONArray("list");

        for (int i = 0; i < data.length(); i++) {
            JSONObject entry = data.getJSONObject(i);
            String entryDate = entry.getString("dt_txt");

            if (entryDate.equals(date)) {
                JSONObject temperatureObj = entry.getJSONObject("main");
                return temperatureObj.getDouble("temp");
            }
        }

        return -1.0;
    }

    private static double getWindSpeed(JSONObject weatherData, String date) throws JSONException {
        JSONArray data = weatherData.getJSONArray("list");

        for (int i = 0; i < data.length(); i++) {
            JSONObject entry = data.getJSONObject(i);
            String entryDate = entry.getString("dt_txt");

            if (entryDate.equals(date)) {
                JSONObject windSpeedObj = entry.getJSONObject("wind");
                return windSpeedObj.getDouble("speed");
            }
        }

        return -1.0;
    }

    private static double getPressure(JSONObject weatherData, String date) throws JSONException {
        JSONArray data = weatherData.getJSONArray("list");

        for (int i = 0; i < data.length(); i++) {
            JSONObject entry = data.getJSONObject(i);
            String entryDate = entry.getString("dt_txt");

            if (entryDate.equals(date)) {
                JSONObject pressureObj = entry.getJSONObject("main");
                return pressureObj.getDouble("pressure");
            }
        }

        return -1.0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            JSONObject weatherData = getWeatherData(BASE_URL);

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Get weather");
                System.out.println("2. Get Wind Speed");
                System.out.println("3. Get Pressure");
                System.out.println("0. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter the date: ");
                        String dateForTemperature = scanner.nextLine();
                        double temperature = getTemperature(weatherData, dateForTemperature);
                        System.out.println("Temperature on " + dateForTemperature + ": " + temperature + "°C");
                        break;
                    case 2:
                        System.out.print("Enter the date: ");
                        String dateForWindSpeed = scanner.nextLine();
                        double windSpeed = getWindSpeed(weatherData, dateForWindSpeed);
                        System.out.println("Wind Speed on " + dateForWindSpeed + ": " + windSpeed + " km/h");
                        break;
                    case 3:
                        System.out.print("Enter the date: ");
                        String dateForPressure = scanner.nextLine();
                        double pressure = getPressure(weatherData, dateForPressure);
                        System.out.println("Pressure on " + dateForPressure + ": " + pressure + " hPa");
                        break;
                    case 0:
                        System.out.println("Exiting the program...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option! Please choose a valid option.");
                }
            }
        } catch (IOException | JSONException e) {
            System.out.println("Error occurred while fetching weather data: " + e.getMessage());
        }
    }
}
