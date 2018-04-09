package com.company;

public class Route {

    /*
     *  Una rotta (percorso) è considerata come l'insieme di una serie di
     *  Position; per questo motivo abbiamo bisogno di una apposita struttura dati
     *  per memorizzare la serie di Position che costituiscono il percorso
     */
    public Position[] waypoints;

    public Route(Position... waypoints) {
        this.waypoints = waypoints;
    }

}

