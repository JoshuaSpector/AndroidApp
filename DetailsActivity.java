/*UPDATE VEHICLE ACTIVITY
 Allows a user to update an existing vehicle object
 Takes user input from edit texts
 Uses input to create a vehicle object,
 then when onClick of the update button is detected:
 Submits a HashMap containing a JSON string representation
 Of the object
 Along with the hardcoded API Key
 As a 'PUT' request
 Similar to Add activity :)
 */

package uk.ac.mmu.advprog.myapplication;

import android.content.Intent;
import android.content.UriMatcher;
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
public class DetailsActivity extends AppCompatActivity {
    //START ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // get the intent
        Bundle extras = getIntent().getExtras();
        // create a vehicle object from the vehicle object that was passed over from
        // the MainActivity.
        final Vehicle vehicle = (Vehicle) extras.get("vehicle");
        //Create references to edittexts & updateButton for form submission
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
        final Button updateButton = findViewById(R.id.updateButton);

        //Set the values of the edittexts to the respective variables
        //of the vehicle passed over from the MainActivity
        editMake.setText(vehicle.getMake());
        editModel.setText(vehicle.getModel());
        editYear.setText(String.valueOf(vehicle.getYear()));
        editPrice.setText(String.valueOf(vehicle.getPrice()));
        editLicenseNumber.setText(vehicle.getLicense_number());
        editColour.setText(vehicle.getColour());
        editNumberDoors.setText(String.valueOf(vehicle.getNumber_doors()));
        editTransmission.setText(vehicle.getTransmission());
        editMileage.setText(String.valueOf(vehicle.getMileage()));
        editFuelType.setText(vehicle.getFuel_type());
        editEngineSize.setText(String.valueOf(vehicle.getEngine_size()));
        editBodyStyle.setText(vehicle.getBody_style());
        editCondition.setText(vehicle.getCondition());
        editNotes.setText(vehicle.getNotes());


        //Onclick listener for when user submits the form
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new myTaskParams 'wrapper' for editTexts
                MyTaskParams myTaskParams = new MyTaskParams(editMake, editModel, editYear, editPrice, editLicenseNumber, editColour, editNumberDoors, editTransmission,
                        editMileage, editFuelType, editEngineSize, editBodyStyle, editCondition, editNotes, vehicle.getVehicle_id());
                //Pass parameters to putData Async and execute
                //Should theoretically send an update SQL request to server
                new putData().execute(myTaskParams);
                //Create new intent and go back to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }


        });
    }

    //'Wrapper' class for editText parameters
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
        int vehicle_id;


        MyTaskParams(EditText editMake, EditText editModel, EditText editYear, EditText editPrice, EditText editLicenseNumber,
                     EditText editColour, EditText editNumberDoors, EditText editTransmission, EditText editMileage,
                     EditText editFuelType, EditText editEngineSize, EditText editBodyStyle, EditText editCondition, EditText editNotes, int vehicle_id) {
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
            this.vehicle_id = vehicle_id;
        }
    }

    //putData AsyncTask - takes MyTaskParams 'wrapper' and returns a string
    private class putData extends AsyncTask<MyTaskParams, Void, String>
    {

        @Override
        protected String doInBackground(MyTaskParams[] myTaskParams) {
            //Create new Gson object for string formatting
            Gson gson = new Gson();
            //'Unwrap' parameters and get user text input from edittexts
            int vehicle_id =  myTaskParams[0].vehicle_id;
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
            //Create new vehicle from user input
            Vehicle v = new Vehicle(vehicle_id, make, model, year, price, license_number, colour, number_doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes);
            //Use gson to create JSON formatted string from vehicle object
            String vehicleJSON = gson.toJson(v);
            //Test json string
            System.out.println("VEHICLEJSON = " + vehicleJSON);
            //Create new HashMap containing the API key and JSON string
            HashMap<String, String> params = new HashMap<>();
            params.put("A8A3625014F2582E23DD64AB6C49DF57", vehicleJSON);
            //Use performPutCall to submit Hashmap params to request url
            return performPutCall("http://10.0.2.2:8005/vehicles/servletApi", params);

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


    //end putData ASYNC task

    //String method to return an Url encoded String from a Hashmap
    //Key and value separated by '='
    private String getPutDataString(HashMap<String, String> params) throws
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

    //Method to submit Url encoded, 'stringified' hashmap to request URL
    public String performPutCall(String requestURL, HashMap<String, String> putDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            //create the connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //POST data to connection using output stream & buffered writer

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String params = getPutDataString(putDataParams);
            System.out.print(params + "TESTPARAMS");
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
                Toast.makeText(DetailsActivity.this, "Vehicle Updated", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(DetailsActivity.this, "Error: Update failed", Toast.LENGTH_LONG).show();
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("response = " + response);
        return response;

    }
    //END ONCREATE
}
//END CLASS