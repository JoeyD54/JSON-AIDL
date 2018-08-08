package com.example.playd.gmtest;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the interface
        return Binder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private final IRemoteService.Stub Binder = new IRemoteService.Stub() {
        @Override
        public String getMessage() throws RemoteException {
            return "AIDL Connected Successfully!";
        }

        @Override
        public String getJSON(String condURL) throws RemoteException {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    condURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String city = "", temperature, cityTemp;
                    try{
                        Log.d("JSON Response: ", response.toString());
                        JSONObject obsvObj = response.getJSONObject("current_observation");
                        for(int i = 0; i < obsvObj.length(); i++){
                            JSONObject dispObj = obsvObj.getJSONObject("display_location");
                            for(int j = 0; j < dispObj.length(); j++){
                                city = dispObj.getString("city");
                            }
                            temperature = obsvObj.getString("temp_f");
                            cityTemp = city + " " + temperature;
                            Log.d("CityTemp: ", cityTemp);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley","Error");
                        }
                    }
            );
            return jsonObjectRequest.toString();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {

        }
    };


}
