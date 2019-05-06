/* JOSHUA SPECTOR ANDROID APPLICATION 17089600
Hi Kaleem, hope you are well
Enjoy the read, I am fairly chuffed with how this project turned out :-)

MAIN ACTIVITY
GET:
This activity gets all the vehicle information from the SQLite database,
and displays it to the user in a scrollable listview
DELETE:
The user can longclick on an item to delete it through
a cancelable alertdialog
ADD:
The user can click on the add button to add a new vehicle
UPDATE:
The user can click on an entry in the listview to
view all that entry's data
and update the entry

I've done some additional styling as you can see in the emulator.

 * ALL SERVER REQUESTS ARE ACCOMPLISHED THROUGH ASYNC TASKS
AND SUBMIT A HARDCODED API KEY TO BE VERIFIED SERVER-SIDE *
 */


package uk.ac.mmu.advprog.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import android.os.StrictMode;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView;
import android.widget.Toast;


import org.json.*;

import javax.net.ssl.HttpsURLConnection;
//START CLASS
public class MainActivity extends AppCompatActivity {
    //START ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StrictMode no longer necessary as AsyncTasks implemented for all server requests
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        //Set layout
        setContentView(R.layout.activity_main);
        //Create references to add button (create record) and listview
        //Create Arraylist to hold vehicle information
        final Button createRecord = findViewById(R.id.createRecord);
        final ArrayList<Vehicle> allVehicles = new ArrayList<>();
        final ListView vehicleListView = findViewById(R.id.vehicleListView);
        //Create MyTaskParams 'wrapper' to hold arraylist and listview
        MyTaskParams params = new MyTaskParams(allVehicles, vehicleListView);
        //execute getData Async with params 'wrapper'
        //This will theoretically display a list of all vehicles to the user
        new getData().execute(params);


        //Listview onclick listener to go to DetailsActivity (Update page)
        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Create a toast to display which vehicle the user has pressed
                Toast.makeText(MainActivity.this, "You pressed " + allVehicles.get(i).getVehicle_id() + ": " + allVehicles.get(i).getMake(), Toast.LENGTH_SHORT).show();
                // declare a new intent and give it the context and
                // specify the activity to start
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                // add/put the selected vehicle object in to the intent which will
                // be passed over to the details activity
                intent.putExtra("vehicle", allVehicles.get(i));
                // launch the activity
                startActivity(intent);

            }
        });
        //Listview onlongclick listener to display a popup that allows the user to delete an item on the listview
        vehicleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                //Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //Set Title and Message for the dialog popup
                builder.setTitle("Delete Vehicle Entry");
                builder.setMessage("Are you sure you want to delete this entry?" );
                //Set the dialog to be cancelable (If the user changes their mind)
                builder.setCancelable(true);
                //Setup button for "Yes" case
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Close dialog window
                                dialog.cancel();
                                //test code to check vehicle_id
                                //System.out.println(String.valueOf("testdelete" + allVehicles.get(position).getVehicle_id()));

                                //Get vehicle_id of the item clicked on
                                String vehicle_id = String.valueOf(allVehicles.get(position).getVehicle_id());
                                //Execute deleteData Async with vehicle_id as parameter
                                new deleteData().execute(vehicle_id);
                                //Create a new ArrayList, Listview and Parameter 'wrapper'
                                final ArrayList<Vehicle> allVehicles = new ArrayList<>();
                                final ListView vehicleListView = findViewById(R.id.vehicleListView);
                                MyTaskParams params = new MyTaskParams(allVehicles, vehicleListView);
                                //Execute getData Async to update the Listview
                                new getData().execute(params);
                                //I originally used recreate(); to update the page after a delete
                                //This was less efficient so i replaced it.
                                //recreate();

                            }
                        });
                //Setup negative button
                builder.setNegativeButton(
                        "No",
                        //Cancel the dialog
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Create and display the alert dialog
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }


        });

        //Create record onclick listener
        createRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create intent and start Add Activity
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);

            }
        });
    }
//end oncreate

    //MyTaskParams 'wrapper' class to contain ListView and Arraylist
    //I used this class here as it seemed like an
    //elegant solution to pass two parameters of different types
    //to the get data Async
    private static class MyTaskParams {
        ArrayList<Vehicle> allVehicles;
        ListView vehicleListView;


        MyTaskParams(ArrayList<Vehicle> allVehicles, ListView vehicleListView) {
            this.allVehicles = allVehicles;
            this.vehicleListView = vehicleListView;
        }
    }

    //Start GET Async
    private class getData extends AsyncTask<MyTaskParams, Void, MyTaskParams>
    {

        @Override
        protected MyTaskParams doInBackground(MyTaskParams[] params) {
            //Create inputstream object for reading get response later
            InputStream in = null;
            //Unpack arraylist and listview from the params 'wrapper'
            ArrayList<Vehicle> allVehicles = params[0].allVehicles;
            ListView vehicleListView = params[0].vehicleListView;
            try {
                //create request url with API key parameter attached
                URL url = new URL("http://10.0.2.2:8005/vehicles/servletApi?apikey=A8A3625014F2582E23DD64AB6C49DF57");
                //create and open connection option
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //create buffered input stream that gets the connection input stream
                in = new BufferedInputStream(conn.getInputStream());
                /* I tried to modify this method so that it would
                use an output stream to send the API key to the doGet for verification
                while also getting the buffered input stream
                This didn't work as the data went to the doPost method
                instead of the doGet
                SO I didn't use the commented code below and instead sent the APIkey as
                an url parameter as you can see above.
                 */
//                OutputStream os = conn.getOutputStream();
//                String apikey = "A8A3625014F2582E23DD64AB6C49DF57";
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(apikey);
//                //clear the writer
//                writer.flush();
//                writer.close();
//                //close output stream
//                os.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert input stream to string
            String response = convertStreamToString(in);
            //print response to android monitor log cat
            System.out.println("Server response = " + response);

            try {
                // declare a new json array and pass it the string response from the server
                // this will convert the string into a JSON array which can then be iterated
                // over using a loop
                JSONArray jsonArray = new JSONArray(response);
                // instantiate the vehicle array and set the size
                // to the amount of vehicle objects returned by the server

                // use a for loop to iterate over the JSON array
                for (int i = 0; i < jsonArray.length(); i++) {
                    //Get vehicle variables from the current JSON element
                    String make = jsonArray.getJSONObject(i).get("make").toString();
                    int vehicle_id = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                    String model = jsonArray.getJSONObject(i).get("model").toString();
                    int year = Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                    int price = Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                    String license_number = jsonArray.getJSONObject(i).get("license_number").toString();
                    String colour = jsonArray.getJSONObject(i).get("colour").toString();
                    int number_doors = Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                    String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                    int mileage = Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                    String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                    int engine_size = Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                    String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                    String condition = jsonArray.getJSONObject(i).get("condition").toString();
                    String notes = jsonArray.getJSONObject(i).get("notes").toString();

                    // print the make to log cat
                    System.out.println("make = " + make);
                    // create a vehicle object using the json element and add it to the arraylist
                    Vehicle vehicle = new Vehicle(vehicle_id, make, model, year, price, license_number, colour, number_doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes);
                    allVehicles.add(vehicle);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //test code
            System.out.println("testdoinbackground");
            //create a new 'wrapper' taskParams and add the arraylist and updated listview
            MyTaskParams taskParams = new MyTaskParams(allVehicles, vehicleListView);
            //return the 'wrapper' for use in postexecute
            return taskParams;
        }
        //end arraylist
        @Override
        protected  void onPostExecute(MyTaskParams taskParams) {
            super.onPostExecute(taskParams);
            //'unwrap' taskParams
            ArrayList<Vehicle> vehicleArrayList = taskParams.allVehicles;
            ListView vehicleListView = taskParams.vehicleListView;
            //process the vehicles ArrayList and set up ListView with vehicleHeaders

            int[] vehicleIDs = new int[vehicleArrayList.size()];
            String[] vehicleNames = new String[vehicleArrayList.size()];
            String[] vehicleHeaders = new String[vehicleArrayList.size()];
            //Iterate over vehicleArrayList to populate the three arrays
            for (int i = 0; i<vehicleArrayList.size(); i++)
            {
                vehicleIDs[i] = vehicleArrayList.get(i).getVehicle_id();
                vehicleNames[i] = vehicleArrayList.get(i).getMake();
                vehicleHeaders[i] = (vehicleIDs[i] + ": " + vehicleNames[i]);

            }
            //Setup ArrayAdapter with the vehicleHeaders String[]
            //Note the R.layout.mytextview
            //This contains some of the styling I did for this project
            //Which you can see in the res folder :)
            ArrayAdapter vehicleArrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.mytextview, R.id.myTextView, vehicleHeaders);
            //Set the adapter to the Listview
            //The main page will now theoretically show a list of vehicles!
            vehicleListView.setAdapter((vehicleArrayAdapter));
        }

    }
    //end getVehicles ASYNC task

    //Converts Hashmap containing key value pair into an url encoded string
    //Where the key and value are separated by an '='
    private String getDeleteDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    //Method to convert input stream from get data into  a String object
    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //DELETE data Async task. Takes a String (vehicle_id) and returns a String
    private class deleteData extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String[] vehicle_ids) {
            //get the vehicle_id from the parameter array
            String vehicle_id = vehicle_ids[0];
            //Create a hashmap containing the API key and vehicle_id
            HashMap<String, String> params = new HashMap<>();
            params.put("A8A3625014F2582E23DD64AB6C49DF57",vehicle_id);
            //Use performDeleteCall to send the params hashmap to the request URL
            return performDeleteCall("http://10.0.2.2:8005/vehicles/servletApi", params);

        }

    }
    //Method to send Hashmap containing the API key and vehicle_id to doDelete method on server end
    public String performDeleteCall(String requestURL, HashMap<String,String> deleteDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            //create the connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //POST data to connection using output stream & buffered writer
            System.out.println("before os");
            OutputStream os = conn.getOutputStream();
            System.out.println("after os");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String params = getDeleteDataString(deleteDataParams);
            //POST key/value data (url encoded) to server
            writer.write(params);

            //clear the writer
            writer.flush();
            writer.close();
            //close output stream
            os.close();

            //get the server response code to determine what to do next
            //i.e success/error

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(MainActivity.this, "Vehicle Deleted", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(MainActivity.this, "Error: Delete Failed", Toast.LENGTH_LONG).show();
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("response = " + response);
        return response;

    }

}
