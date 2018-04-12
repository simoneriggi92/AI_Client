package com.company;

import exceptions.AcceptException;
import exceptions.BadRequestException;
import exceptions.ForbiddenException;
import exceptions.NotFoundException;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.Tools;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main implements IClient{

    private List positions;
    Tools tools = new Tools();

    public static void main(String[] args) throws RuntimeException {

        String username = args[0];
        String password = args[1];
        ArrayList<Position> positionsList;
        Main main = new Main();

        /*Genero posizioni*/
        Simulator simulator = new Simulator();

        simulator.startSimulation();
        positionsList = GeoFunction.getPositionsList();
        //Systemm.out.println("NUM:-------->"+positionsList.size());
        String startTime=String.valueOf(positionsList.get(2).getTimestamp());
        String endTime = String.valueOf(positionsList.get(60).getTimestamp());

        /*COMBO FORBIDDEN*/
        main.combo1(positionsList, null, startTime, endTime);
        /*COMBO OK*/
        main.combo2(username, password, positionsList,startTime, endTime );
    }

    @Override
    public void postPositions(ArrayList<Position>positionList,CookieManager cookieManager) {


        try {
            //
            HttpClient client = HttpClient
                    .newBuilder()
                    .build();

            JSONArray jsonPositionList= new JSONArray(Arrays.asList(positions));
            JSONArray array = new JSONArray();

            for(int i = 0; i < positionList.size(); i++){
                JSONObject obj = new JSONObject();
                obj.put("latitude",positionList.get(i).getLatitude());
                obj.put("longitude", positionList.get(i).getLongitude());
                obj.put("timestamp", positionList.get(i).getTimestamp());
                array.put(obj);
            }

            String url = "http://localhost:8080/position";
            HttpResponse<String> response;
            if(cookieManager != null) {

                 response = client.send(
                        HttpRequest
                                .newBuilder(new URI(url))
                                .header("Cookie", cookieManager.getCookieStore().getCookies().get(0).toString())
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .POST(HttpRequest.BodyProcessor.fromString(array.toString()))
                                .build(),
                        HttpResponse.BodyHandler.asString()
                );
            }else{
                 response = client.send(
                        HttpRequest
                                .newBuilder(new URI(url))
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .POST(HttpRequest.BodyProcessor.fromString(array.toString()))
                                .build(),
                        HttpResponse.BodyHandler.asString()
                );
            }

            if(response.statusCode() == 200)
                System.out.println("(/Client)"+response.version().toString()+" "+response.statusCode()+"/All positions accepted!");
            if(response.statusCode() == 404)
                throw new NotFoundException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Page Not Found");
            if(response.statusCode() == 400)
                throw new BadRequestException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Bad Request: Data type is not JSON!");
            if(response.statusCode() == 403)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request");
            if(response.statusCode() == 302)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request: you have to login");
            if(response.statusCode() == 202) {
                tools.mapInvalidPositions(response.body());
                throw new AcceptException("(/Client)"+response.version().toString() + " " + response.statusCode() + "/Some accepted positions!");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch(ForbiddenException e){
            System.out.println(e.getMessage());
        }catch (BadRequestException e){
            System.out.println(e.getMessage());
        }catch(NotFoundException e){
            System.out.println(e.getMessage());
        }catch(AcceptException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public CookieManager tryToLogin(String username, String password){
        try {

            Tools tools = new Tools();
            CookieManager cookieManager = new CookieManager();
            HttpClient client = HttpClient
                    .newBuilder()
                    .cookieManager(cookieManager)
                    .build();

            String charset = "UTF-8";
            String param1 = username;
            String param2 = tools.get_MD5_Password(password);

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

            if(response.statusCode() == 200)
                System.out.println("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Login ok\n\"(/Client) Welcome "+username+"!");
            if(response.statusCode() == 404)
                throw new NotFoundException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Page Not Found");
            if(response.statusCode() == 400)
                throw new BadRequestException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Bad Request:Insert username or password");
            if(response.statusCode() == 403)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request:Login Error");
            if(response.statusCode() == 302)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request: you have to login");

            return cookieManager;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch(ForbiddenException e){
            System.out.println(e.getMessage());
        } catch (BadRequestException e){
            System.out.println(e.getMessage());
        }catch(NotFoundException e){
            System.out.println(e.getMessage());
        }
       return null;
    }

    @Override
    public void getPositions(String startTimestamp, String endTimestamp, CookieManager cookieManager)   {

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        try {
            String url = "http://localhost:8080/rangePositions?startTimestamp="+startTimestamp+"&endTimestamp="+endTimestamp;

            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI(url))
                            .header("Cookie", cookieManager.getCookieStore().getCookies().get(0).toString())
                            .GET()
                            .build(),
                    HttpResponse.BodyHandler.asString()
            );

            if(response.statusCode() == 200) {
                System.out.println("(/Client)"+response.version().toString() + " " + response.statusCode() + "/GET ");
                tools.mapRangePositions(response.body());
            }
            if(response.statusCode() == 404)
                throw new NotFoundException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Page Not Found");
            if(response.statusCode() == 400)
                throw new BadRequestException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Bad Request: Invalid t1 or t2");
            if(response.statusCode() == 403)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request");
            if(response.statusCode() == 302)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request: you have to login");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch(ForbiddenException e){
            System.out.println(e.getMessage());
        } catch (BadRequestException e){
            System.out.println(e.getMessage());
        }catch(NotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void tryToLogout(CookieManager cookieManager)    {

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        try {
            String url = "http://localhost:8080/logout";

            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI(url))
                            .header("Cookie", cookieManager.getCookieStore().getCookies().get(0).toString())
                            .GET()
                            .build(),
                    HttpResponse.BodyHandler.asString()
            );

            if(response.statusCode() == 200)
                System.out.println("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Logout successful");
            if(response.statusCode() == 404)
                throw new NotFoundException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Page Not Found");
            if(response.statusCode() == 400)
                throw new BadRequestException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Bad Request");
            if(response.statusCode() == 403)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch(ForbiddenException e){
            System.out.println(e.getMessage());
        } catch (BadRequestException e){
            System.out.println(e.getMessage());
        }catch(NotFoundException e){
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void tryToGetPositions(String startTimestamp, String endTimestamp){

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        try {
            String url = "http://localhost:8080/rangePositions?startTimestamp="+startTimestamp+"&endTimestamp="+endTimestamp;

            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI(url))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandler.asString()
            );

            if(response.statusCode() == 404)
                throw new NotFoundException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Page Not Found");
            if(response.statusCode() == 400)
                throw new BadRequestException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Bad Request");
            if(response.statusCode() == 403)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request");
            if(response.statusCode() == 302)
                throw new ForbiddenException("(/Client)"+response.version().toString()+" "+response.statusCode()+"/Forbidden Request: you have to login");

            System.out.println("\n\n"+response.body());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch(ForbiddenException e){
            System.out.println(e.getMessage());
        } catch (BadRequestException e){
            System.out.println(e.getMessage());
        }catch(NotFoundException e){
            System.out.println(e.getMessage());
        }
    }


    public void combo1(ArrayList<Position>positionList, CookieManager cookieManager, String startTime, String endTime){
        System.out.println("\n\n\"(/Client)Starting #COMBO1..");
        tryToGetPositions(startTime, endTime);
        postPositions(positionList, cookieManager);
    }

    public void combo2(String username, String password, ArrayList<Position>positionList, String startTime, String endTime){

        System.out.println("\n\n\"(/Client)Starting #COMBO2..");

        /*Tentativo di login*/
         CookieManager cookieManager = tryToLogin(username, password);
        /*POST delle posizioni(vettore di posizioni JSON)*/
        postPositions(positionList,cookieManager);
        /*GET autenticata delle posizioni*/
        getPositions(startTime, endTime, cookieManager);
        /*Logout*/
        tryToLogout(cookieManager);
    }

}
