package com.swjtu.client;

import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.swjtu.api.TransApi;
import com.swjtu.ws.Location;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherInquiryClient {

    //define the api key
    private static final String APP_ID = "20191208000364199";
    private static final String SECURITY_KEY = "3H2SX1cvxWw4B2pbjDJA";

    public static void main(String[] args) throws Exception {

        WeatherInquiryClient w = new WeatherInquiryClient();
        String city = w.chooseCity();
        if (city.equals(null)){
            System.out.println("Your input is invalid!");
            return;
        }else{
            System.out.println("The city you are looking for is " + city);
            String param = w.invokeHeWeather(city);
            String[] strArr = param.split("#");
            String cond_txt = strArr[0];
            String wind_dir = strArr[1];

            // translate the wind direction and weather from chinese to english
            cond_txt = w.invokeTranslate(cond_txt);
            wind_dir = w.invokeTranslate(wind_dir);

            // start translation
            System.out.println("\nStart Translation...............\n");
            System.out.println("Wind Direction: " + wind_dir);
            System.out.println("Weather: " + cond_txt);
        }
    }

    /**
     * get the city name from input and check it
     * @return legal city name
     * @throws Exception
     */
    public String chooseCity() throws Exception{
        URL url = new URL("http://localhost:8888/ws/location?wsdl");
        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.swjtu.com/", "LocationImplService");

        Service service = Service.create(url, qname);

        Location location = service.getPort(Location.class);

        System.out.println("Enter the place of weather you want to query.");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        String loca = location.getLocation(input);

        return loca;
    }

    /**
     * call the weather api
     * @param city
     * @return json with detail weather information
     */
    public String invokeHeWeather(String city){
        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;

        String key = "66948042f9cd42b9aa07ba299917d33d";

        try
        {
            // Include your Google key
            String urlString = "https://free-api.heweather.net/s6/weather/now?location=" + city + "&key=" + key;

//            System.out.println(urlString);
            System.out.print('\n');

            url = new URL (urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String reply;
            reply = theReader.readLine();
//            System.out.println(reply);

            JSONObject obj = JSONObject.fromObject(reply);
            JSONArray jsonArray = obj.getJSONArray("HeWeather6");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject jsonObject1 = jsonObject.getJSONObject("now");
            JSONArray jsonArray1 = JSONArray.fromObject(jsonObject1);
            String cond_txt = jsonArray1.getJSONObject(0).getString("cond_txt");
            String tmp = jsonArray1.getJSONObject(0).getString("tmp");
            String hum = jsonArray1.getJSONObject(0).getString("hum");
            String pres = jsonArray1.getJSONObject(0).getString("pres");
            String wind_sc = jsonArray1.getJSONObject(0).getString("wind_sc");
            String wind_dir = jsonArray1.getJSONObject(0).getString("wind_dir");
            String wind_spd = jsonArray1.getJSONObject(0).getString("wind_spd");

            System.out.println("Temperature: " + tmp + " degrees Celsius");
            System.out.println("Relatively Humidity: " + hum);
            System.out.println("Atmospheric Pressure: " + pres);
            System.out.println("Wind Speed: " + wind_spd + " km/h");
            System.out.println("Wind Power: " + wind_sc);
            System.out.println("Wind Direction: " + wind_dir);
            System.out.println("Weather: " + cond_txt);

            return cond_txt+"#"+wind_dir;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * call the translate api
     * @param query
     * @return translation in English
     */
    public String invokeTranslate(String query){
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String result = api.getTransResult(query, "auto", "en");
//        System.out.println(result);

        try {
            JSONObject jsonObject = JSONObject.fromObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
//        System.out.println(jsonArray);
            String translate = jsonArray.getJSONObject(0).getString("dst");

            return translate;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
