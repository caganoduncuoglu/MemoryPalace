package com.atm.memoryPalace.utils.database;

import com.google.android.gms.maps.model.LatLng;


public class LocationUtil {

    final static String API_KEY = "AIzaSyBJlxDaiRQSOjK7qB82K3OedZeT8IcXsEA";


    public static String getMapImageUrl(LatLng latLng){
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        return "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=13&size=600x300&maptype=roadmap&markers=color:red%7Alabel:C%7C$"+lat+","+lng+"&key="+API_KEY;

    }
}
