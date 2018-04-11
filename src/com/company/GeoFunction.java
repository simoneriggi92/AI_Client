package com.company;

import java.util.ArrayList;
import java.util.Random;

public class GeoFunction {

    public GeoFunction(){

    }

    private static final double EARTH_RADIUS = 6371; // Raggio approssimativo della Terra
    private static Random random = new Random();
    /*
     * Per il calcolo della distanza tra due punti su una sfera (Terra) si ricorre alla
     * formula di haversine:
     *      hav(d/r) = hav(L2-L1) + cos(L1)cos(L2)hav(Long2-Long1)
     */

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        /*
         *  La formula di Haversine è la seguente:
         *      hav(a) = sin^2(a/2) = (1-cos(a))/2
         */
        return Math.pow(Math.sin(val / 2), 2);
    }

    /*
     *  Questa funzione permette di calcolare il bearing iniziale in gradi,
     *  cioè la direzione dalla posizione attuale al punto di destinazione;
     *  giusto per intenderci è l'angolo che si forma tra il nord (0°) e la
     *  destinazione (x°)
     */
    public static double CalculateBearing(Position startPoint, Position endPoint){
        double lat1 = Math.toRadians(startPoint.getLatitude());
        double lat2 = Math.toRadians(endPoint.getLatitude());
        double dLon = Math.toRadians(endPoint.getLongitude() - startPoint.getLongitude());

        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);
        double y = Math.sin(dLon) * Math.cos(lat2);

        double bearing = Math.atan2(y, x);

        //atan2 ritorna un valore compreso tra -180 e +180, quindi dobbiamo convertire
        //questo valore come compreso tra 0 e 360 gradi
        return (Math.toRadians(bearing) + 360) % 360;
    }

    /*
     *  Questa funzione calcola il punto di destinazione a partire da un punto avendo persorso una data
     *  distanza in km dal bearing iniziale
     */
    public static Position CalculateDestinationLocationPoint(Position point, double bearing, double distance) {

        distance = distance / EARTH_RADIUS; // convert to angular distance in radians
        bearing = Math.toRadians(bearing); // convert bearing in degrees to radians

        double lat1 = Math.toRadians(point.getLatitude());
        double lon1 = Math.toRadians(point.getLongitude());

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance) + Math.cos(lat1)
                * Math.sin(distance) * Math.cos(bearing));
        double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(distance) * Math.cos(lat1),
                Math.cos(distance) - Math.sin(lat1) * Math.sin(lat2));
        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // normalize to -180 - + 180 degrees

        return new Position(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }


    public static ArrayList<Position> getPositionsList(){

        int randomIndex = 2;
        ArrayList<Position> positionsList = new ArrayList<>();

        for(Position l : Simulator.points){
            positionsList.add(l);
        }

        /************************************************
         *          Alterazione di dati                 *
         ************************************************/
        /*  Alterazione dei dati nella lista prima di inviarla al server in modo da vefificare la validazione dei dati
        *       1. Cambiamento brusco di latitudine o longitudine
        *       2. Cambiamento di timeStamp in data precedente
        *       3. Cambiamento di latitudine e longitudine oltre i valori che li rendono validi
        */

        randomIndex = randomIntIndexInRange(1, 5, positionsList.size());  //Sostituire il max con la dimensione della lista

//        //45.46418, 9.19127     -->     Duomo di milano
//        positionsList.get(randomIndex).setLatitude(45.46418);
//        positionsList.get(randomIndex).setLongitude(9.19127);
//
//        randomIndex = randomIntIndexInRange(6, 10, positionsList.size()); //Sostituire il max con la dimensione della lista
//        positionsList.get(randomIndex).setTimestamp(1491670668);    //GMT: Saturday, 8 April 2017 16:57:48

        randomIndex = randomIntIndexInRange(11, 15, positionsList.size()); //Sostituire il max con la dimensione della lista
        positionsList.get(randomIndex).setLatitude(91.01234);
        positionsList.get(randomIndex).setLongitude(181.12345);


        return positionsList;
    }

    public static int randomIntIndexInRange(int min, int max, int sizeList) {

        int randomNum = random.nextInt((max - min) + 1) + min;
        int randomIndex = (int)(Math.random() * sizeList);
        return randomIndex;
    }

}

