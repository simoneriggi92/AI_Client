package com.company;

import java.util.*;

public class Simulator {

    public static ArrayList<Position> points = new ArrayList<>();

    public static int i = 0;
    public static int tratto = 1;
    public static Random random = new Random();
    public static long randomNumber;

    public static Route simulatedRoute = new Route(
            new Position(45.06136, 7.66054),
            new Position(45.06198, 7.66098),
            new Position(45.06168, 7.66187),
            new Position(45.06102, 7.66144),
            new Position(45.06136, 7.66054)
    );

    public void startSimulation(){

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
                for (int x = 0; x < simulatedRoute.waypoints.length - 1; x++) {

                    Position start = simulatedRoute.waypoints[x];//new Position(45.06249, 7.66220);//, date.getTime());
                    Position end = simulatedRoute.waypoints[x + 1];//new Position(45.06392, 7.65828);
                   // System.out.println("***********     TRATTO NUM. " + tratto + "      ***********");
                    CalculateWaypoints(start, end);
                    tratto++;
                }
//            }
//        }, 0, 10000);   //Ciclo di 10 secondi

    }

    public static void CalculateWaypoints(Position startPoint, Position endPoint){

        double speed = 1.25; //4.5km  //Velocità media di un uomo che cammina è 4.5 km/h, cioè 1.25 m/s
        double distance = GeoFunction.distance(startPoint.getLatitude(), startPoint.getLongitude(),
                endPoint.getLatitude(), endPoint.getLongitude())*1000;//in metri
        double duration = distance/speed;
        Random randomNum = new Random();
        Date date = new Date();
        long temporaryTime = 0;

//        System.out.println("DISTANCE: " + distance);
//        System.out.println("TIME_REQ(s): "+ distance / speed);
//        System.out.println("TIME_REQ(min): "+ (distance / speed)/60);

        for(int i = 0; i < duration; i++){
            double bearing = GeoFunction.CalculateBearing(startPoint, endPoint);
            double distanceKm = speed / 1000;

            Position middlePoint = GeoFunction.CalculateDestinationLocationPoint(startPoint, bearing, distanceKm);
            if(i == 0) {
                middlePoint.setTimestamp(System.currentTimeMillis()/1000);  //Example Format: 08/04/2018 - 18:08:21
                temporaryTime = middlePoint.getTimestamp();
            }
            else {
                middlePoint.setTimestamp(temporaryTime + randomNumberInRange(0, 20));
                temporaryTime = middlePoint.getTimestamp();
            }

            points.add(middlePoint);

//            System.out.println("Latidute Point: " + points.get(i).getLatitude() + " --- Longitude Point: "
//                    + points.get(i).getLongitude() + " --- TimeStamp: " + points.get(i).getTimestamp());

            startPoint = middlePoint;
        }
    }

    public static long randomNumberInRange(long min, long max) {

        randomNumber = min+((long)(random.nextDouble()*(max-min)));
        //System.out.println("Random Number: " + number);
        return randomNumber;
    }
}

