package com.emon.traveller.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherModel {

        @SerializedName("coord")
        public Coord coord;
        @SerializedName("sys")
        public Sys sys;
        @SerializedName("weather")
        public ArrayList<Weather> weather = new ArrayList<Weather>();

        @SerializedName("main")
        public Main main;
        @SerializedName("wind")
        public Wind wind;
        @SerializedName("rain")
        public Rain rain;
        @SerializedName("clouds")
        public Clouds clouds;
        @SerializedName("dt")
        public float dt;
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("cod")
        public float cod;
    }

class Weather {
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
}

    class Clouds {
        @SerializedName("all")
        public float all;
    }

    class Rain {
        @SerializedName("3h")
        public float h3;
    }

    class Wind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;
    }

class Sys {
        @SerializedName("country")
        public String country;
        @SerializedName("sunrise")
        public long sunrise;
        @SerializedName("sunset")
        public long sunset;
    }

    class Coord {
        @SerializedName("lon")
        public float lon;
        @SerializedName("lat")
        public float lat;
    }