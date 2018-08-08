package com.example.playd.gmtest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private Boolean isServiceConnected = false;
    IRemoteService myIRemoteService = null;
    TextView isConnected, logDisplay, cityText, temperatureText;
    Button bindButton, unbindButton, getData;
    String city = "", temperature = "";
    String splitCityTemp = "";
    String serviceMessage = "", serviceJSON = "";

    String condUrl = "http://api.wunderground.com/api/90645b02d360fe14/conditions/q/Michigan/Detroit.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isConnected = findViewById(R.id.is_connected_text);
        cityText = findViewById(R.id.city_text);
        temperatureText = findViewById(R.id.temperature_text);
        logDisplay = findViewById(R.id.log_display);
        bindButton = findViewById(R.id.bind_button);
        bindButton.setOnClickListener(bindListener);
        unbindButton = findViewById(R.id.unbind_button);
        unbindButton.setOnClickListener(unbindListener);
        getData = findViewById(R.id.parse_data);
        getData.setOnClickListener(parseAIDL);
    }

    //***********************************************************************************
    // Function: Service Connection
    // Purpose: activates AIDL service. Logs when a disconnect happens unexpectedly.
    // Arguments: None
    // Returns: None
    //***********************************************************************************
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myIRemoteService = IRemoteService.Stub.asInterface(service);
            logDisplay.setText("Attached.");

            Toast.makeText(MainActivity.this, R.string.remote_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            myIRemoteService = null;
            Toast.makeText(MainActivity.this, R.string.remote_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    //***********************************************************************************
    // Function: bindListener
    // Purpose: Listener for bindButton. Calls ServiceConnection
    // Arguments: None
    // Returns: None
    //***********************************************************************************
    private View.OnClickListener bindListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RemoteService.class);
            intent.setAction(IRemoteService.class.getName());
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
            isServiceConnected = true;
            logDisplay.setText("Binding.");

        }
    };

    //***********************************************************************************
    // Function: unbindListener
    // Purpose: Listener for unbindButton. De-activates AIDL service.
    // Arguments: None
    // Returns: None
    //***********************************************************************************
    private View.OnClickListener unbindListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isServiceConnected) {
                unbindService(connection);
                isServiceConnected = false;
                logDisplay.setText("Unbinding.");

            }
        }
    };

    //***********************************************************************************
    // Function: parseAIDL
    // Purpose: Get data from AIDL.
    // Arguments: None
    // Returns: None
    //***********************************************************************************
    private View.OnClickListener parseAIDL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isServiceConnected) {
                try {
                    serviceJSON = myIRemoteService.getJSON(condUrl);
                    serviceMessage = myIRemoteService.getMessage();
                    temperatureText.setText(serviceJSON);

                } catch (RemoteException e) {
                    Log.e(TAG, "getMessage method call failed.");
                    Toast.makeText(MainActivity.this, "getMessage failed.",
                            Toast.LENGTH_SHORT).show();
                }
               /* try{
                    JSONObject obj = new JSONObject(serviceJSON);
                    parseJSON(obj);
                } catch (JSONException e){
                    Log.e(TAG, "Could not parse String from AIDL back into OBJ");
                }*/

                isConnected.setText(serviceMessage);
            }
        }
    };

    //***********************************************************************************
    // Function: parseJSON
    // Purpose: Get JSONobj from parse AIDL. Parse the JSON to print data.
    // Arguments: JSONobj
    // Returns: None
    //***********************************************************************************
    private void parseJSON(String JSONobj){
        //String city = "", temperature;

    }
}
