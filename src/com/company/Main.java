package com.company;





import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.json.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main implements IClient{


    private List positions;



    public static void main(String[] args) throws RuntimeException {
        ArrayList<Position> positionsList;
        Main main = new Main();
        Simulator simulator = new Simulator();

        simulator.startSimulation();
        positionsList = GeoFunction.getPositionsList();
        CookieManager cookieManager = main.tryToLogin("simone", "1124");
        main.postPositions(positionsList,cookieManager);
    }

    @Override
    public void postPositions(ArrayList<Position>positionList,CookieManager cookieManager) {

        try {

            HttpClient client = HttpClient
                    .newBuilder()
                    .build();


      JSONArray jsonPositionList= new JSONArray(Arrays.asList(positions));
      JSONArray array = new JSONArray();

            for(int i = 0; i < positionList.size(); i++){
                JSONObject obj = new JSONObject();
                obj.put("latitude",positionList.get(i).getLatitude());
                obj.put("longitude", positionList.get(i).getLongitude());
                obj.put("temporalStamp", positionList.get(i).getTimestamp());
                array.put(obj);
            }

            //Syste m.out.println("JSON:"+array);

//            String charset = "UTF-8";
//            String param1 = "90";
//            String param2 = "45";
//            String param3="123455";
//
//            json.put("latitude", param1);
//            json.put("longitude", param2);
//            json.put("temporalStamp", param3);
//            json.put("latitude", "23");
//            json.put("longitude", "56");
//            json.put("temporalStamp", "58");

            //mJSONArray = new JSONArray(Arrays.asList(mybeanList));
           // System.out.println("" +json.toString());



//            String query = String.format("latitude=%s&longitude=%s",
//                    URLEncoder.encode(param1, charset),
//                    URLEncoder.encode(param2, charset));

            String url = "http://localhost:8080/position";

            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI(url))
                            .header("Cookie", cookieManager.getCookieStore().getCookies().get(0).toString())
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .POST(HttpRequest.BodyProcessor.fromString(array.toString()))
                            .build(),
                    HttpResponse.BodyHandler.asString()
            );


            System.out.println(response.statusCode());
            System.out.println(response.body());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
    }

    }


    @Override
    public CookieManager tryToLogin(String username, String password){
        try {

            CookieManager cookieManager = new CookieManager();
            HttpClient client = HttpClient
                    .newBuilder()
                    .cookieManager(cookieManager)
                    .build();

            String charset = "UTF-8";
            String param1 = username;
            String param2 = password;



            String query = String.format("username=%s&password=%s",
                    URLEncoder.encode(param1, charset),
                    URLEncoder.encode(param2, charset));

            String url = "http://localhost:8080/login?"+query;



            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI(url))
                            .POST(HttpRequest.noBody())
                            .build(),
                    HttpResponse.BodyHandler.asString()
            );

            System.out.println(response.statusCode());
            if(response.statusCode() != 200)
                throw new RuntimeException("Non ti sei loggato");


//            Optional<String> head = response.headers().firstValue("Set-Cookie");
            System.out.println(response.statusCode());
            System.out.println(response.body());
//            System.out.println(head.get());
            System.out.println(cookieManager.getCookieStore().getCookies().get(0));
            return cookieManager;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
