/*ADD VEHICLE ACTIVITY
 Allows a user to add a new vehicle object.
 Takes user input from edit texts.
 Uses input to create a vehicle object,
 then when onClick of the add button is detected:
 Submits a HashMap containing a JSON string representation
 of the object, along with the hardcoded API Key.
 As a 'POST' request
 Similar to update activity:)

 NOTE: Originally I programmed this activity
 to take a user input for vehicle_id
 I decided this didn't really make sense.
 So I removed this feature to let the SQLite AUTOINCREMENT
 generate the IDs in sequence automatically.
 Below you will see I have commented out various vehicle_id
 related lines to achieve this.
 */

package uk.ac.mmu.advprog.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
//START CLASS
public class AddActivity extends AppCompatActivity {
    //START ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Removed vehicle_id variable as Vehicle ID is automatically generated for user using AUTOINCREMENT
//        final EditText editVehicleID = findViewById(R.id.editVehicleID);
        //Create EditText references to get user input
        final EditText editMake = findViewById(R.id.editMake);
        final EditText editModel = findViewById(R.id.editModel);
        final EditText editYear = findViewById(R.id.editYear);
        final EditText editPrice = findViewById(R.id.editPrice);
        final EditText editLicenseNumber = findViewById(R.id.editLicenseNumber);
        final EditText editColour = findViewById(R.id.editColour);
        final EditText editNumberDoors = findViewById(R.id.editNumberDoors);
        final EditText editTransmission = findViewById(R.id.editTransmission);
        final EditText editMileage = findViewById(R.id.editMileage);
        final EditText editFuelType = findViewById(R.id.editFuelType);
        final EditText editEngineSize = findViewById(R.id.editEngineSize);
        final EditText editBodyStyle = findViewById(R.id.editBodyStyle);
        final EditText editCondition = findViewById(R.id.editCondition);
        final EditText editNotes = findViewById(R.id.editNotes);
        //Add button reference - used to submit form
        final Button addButton = findViewById(R.id.addButton);

        //Onclick listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a Task Parameter object to 'wrap' the editTexts
                MyTaskParams myTaskParams = new MyTaskParams(editMake, editModel, editYear, editPrice, editLicenseNumber, editColour, editNumberDoors, editTransmission,
                        editMileage, editFuelType, editEngineSize, editBodyStyle, editCondition, editNotes);
                //Execute postData AsyncTask with 'wrapped' parameters as argument
                new postData().execute(myTaskParams);
                //After Async is finished, return to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //MyTaskParams class - used as a 'wrapper' for passing multiple parameters to AsyncTask
    private static class MyTaskParams {
        EditText editMake;
        EditText editModel;
        EditText editYear;
        EditText editPrice;
        EditText editLicenseNumber;
        EditText editColour;
        EditText editNumberDoors;
        EditText editTransmission;
        EditText editMileage;
        EditText editFuelType;
        EditText editEngineSize;
        EditText editBodyStyle;
        EditText editCondition;
        EditText editNotes;
    //    EditText editVehicleID;


        //Constructor
        MyTaskParams(EditText editMake, EditText editModel, EditText editYear, EditText editPrice, EditText editLicenseNumber,
                     EditText editColour, EditText editNumberDoors, EditText editTransmission, EditText editMileage,
                     EditText editFuelType, EditText editEngineSize, EditText editBodyStyle, EditText editCondition, EditText editNotes) {
            this.editMake = editMake;
            this.editModel = editModel;
            this.editYear = editYear;
            this.editPrice = editPrice;
            this.editLicenseNumber = editLicenseNumber;
            this.editColour = editColour;
            this.editNumberDoors = editNumberDoors;
            this.editTransmission = editTransmission;
            this.editMileage = editMileage;
            this.editFuelType = editFuelType;
            this.editEngineSize = editEngineSize;
            this.editBodyStyle = editBodyStyle;
            this.editCondition = editCondition;
            this.editNotes = editNotes;
            //    this.editVehicleID = editVehicleID;
        }
    }
    //End Inner Class

    /*AsyncTask for POST
    takes MyTaskParams 'wrapper' and returns a String
     */
    private class postData extends AsyncTask<MyTaskParams, Void, String>
    {

        @Override
        protected String doInBackground(MyTaskParams[] myTaskParams) {
            //Create GSON
            Gson gson = new Gson();
            //'Unwrap' parameters from myTaskParams and get user input
//          int vehicle_id =  Integer.valueOf(myTaskParams[0].editVehicleID.getText().toString());
            String make = myTaskParams[0].editMake.getText().toString();
            String model = myTaskParams[0].editModel.getText().toString();
            int year = Integer.valueOf(myTaskParams[0].editYear.getText().toString());
            int price = Integer.valueOf(myTaskParams[0].editPrice.getText().toString());
            String license_number = myTaskParams[0].editLicenseNumber.getText().toString();
            String colour = myTaskParams[0].editColour.getText().toString();
            int number_doors = Integer.valueOf(myTaskParams[0].editNumberDoors.getText().toString());
            String transmission = myTaskParams[0].editTransmission.getText().toString();
            int mileage = Integer.valueOf(myTaskParams[0].editMileage.getText().toString());
            String fuel_type = myTaskParams[0].editFuelType.getText().toString();
            int engine_size = Integer.valueOf(myTaskParams[0].editEngineSize.getText().toString());
            String body_style = myTaskParams[0].editBodyStyle.getText().toString();
            String condition = myTaskParams[0].editCondition.getText().toString();
            String notes = myTaskParams[0].editNotes.getText().toString();
            //Create vehicle from user input
            Vehicle v = new Vehicle(make, model, year, price, license_number, colour, number_doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes);
            //Create JSON string from vehicle
            String vehicleJSON = gson.toJson(v);
            //Test string
            System.out.println("VEHICLEJSON = " + vehicleJSON);
            //Create a hashmap and add the API key and Json as key value pair
            HashMap<String, String> params = new HashMap<>();
            params.put("A8A3625014F2582E23DD64AB6C49DF57", vehicleJSON);
            //Submit hashmap to servlet request URL
            return performPostCall("http://10.0.2.2:8005/vehicles/servletApi", params);

        }
        //end arraylist
//        @Override
//        protected  void onPostExecute(String vehicleJSON) {
//            super.onPostExecute(vehicleJSON);
//            System.out.println("VEHICLEJSON2 = " + vehicleJSON);
//
//            performPutCall("http://10.0.2.2:8005/vehicles/servletApi", vehicleJSON);
//        }
    }
    //END Async
    //Method for converting Hashmap into an urlencoded string
    private String getPostDataString(HashMap<String, String> params) throws
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
    //Method to POST Hashmap to request URL
    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            //create the connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("after conn");

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //POST data to connection using output stream & buffered writer
            System.out.println("before os");

            OutputStream os = conn.getOutputStream();
            System.out.println("after os");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String params = getPostDataString(postDataParams);
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
                Toast.makeText(AddActivity.this, "Vehicle Added ", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(AddActivity.this, "Error: Add failed", Toast.LENGTH_LONG).show();
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("response = " + response);
        return response;

    }
    //END CLASS
}
//END ONCREATE