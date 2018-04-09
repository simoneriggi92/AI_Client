package com.company;

public class Position {

    /*
     *  Per simulare il movimento lungo un percorso predefinito, consideriamo il percorso come
     *  costituito da un insieme di punti geolocalizzati e quindi caratterizzati dall'avere una
     *  data latitudine ed una data longitudine. Più sono i punti a disposizione, più accurato sarà
     *  il percorso.
     */

    /*
     *  Per latitudine e longitudine usiamo delle variabili a doppia precisione in quanto una lieve
     *  variazione degli angoli (latitudine e longitudine) comporta una grande variazione nella distanza.
     *  Infatti un grado di latitudine corrisponde ad un arco di 111km e un grado di longitudine corrisponde
     *  a 40075 km * cos( latitude ) / 360 !!!
     */
    private double latitude;
    private double longitude;
    private long timestamp;

//    public Position(double latitude, double longitude, long timestamp){
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.timestamp = timestamp;
//    }

    public Position(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

}

