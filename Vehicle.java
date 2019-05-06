package uk.ac.mmu.advprog.myapplication;

import java.io.Serializable;

public class Vehicle implements Serializable {

    /** The make. */
    //Private variables
    private String make;

    /** The model. */
    private String model;

    /** The body style. */
    private String body_style;

    /** The condition. */
    private String condition;

    /** The notes. */
    private String notes;

    /** The license number. */
    private String license_number;

    /** The colour. */
    private String colour;

    /** The transmission. */
    private String transmission;

    /** The fuel type. */
    private String fuel_type;

    /** The year. */
    private int year;

    /** The price. */
    private int price;

    /** The number doors. */
    private int number_doors;

    /** The mileage. */
    private int mileage;

    /** The engine size. */
    private int engine_size;

    /** The vehicle id. */
    private int vehicle_id;


    /**
     * Instantiates a new vehicle.
     *
     * @param vehicle_id the vehicle id
     * @param make the make
     * @param model the model
     * @param year the year
     * @param price the price
     * @param license_number the license number
     * @param colour the colour
     * @param number_doors the number doors
     * @param transmission the transmission
     * @param mileage the mileage
     * @param fuel_type the fuel type
     * @param engine_size the engine size
     * @param body_style the body style
     * @param condition the condition
     * @param notes the notes
     */
    //Constructor
    public Vehicle(int vehicle_id, String make, String model, int
            year, int price, String license_number, String colour, int
                           number_doors, String transmission, int mileage, String fuel_type,
                   int engine_size, String body_style, String condition, String
                           notes) {

        this.vehicle_id = vehicle_id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.license_number = license_number;
        this.colour = colour;
        this.number_doors = number_doors;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuel_type = fuel_type;
        this.engine_size = engine_size;
        this.body_style = body_style;
        this.condition = condition;
        this.notes = notes;

    }
    //Constructor
    public Vehicle(String make, String model, int
            year, int price, String license_number, String colour, int
                           number_doors, String transmission, int mileage, String fuel_type,
                   int engine_size, String body_style, String condition, String
                           notes) {

        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.license_number = license_number;
        this.colour = colour;
        this.number_doors = number_doors;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuel_type = fuel_type;
        this.engine_size = engine_size;
        this.body_style = body_style;
        this.condition = condition;
        this.notes = notes;

    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    //toString, getters and setters to provide access to variables
    @Override
    public String toString() {
        return "Vehicle [make=" + make + ", model=" + model + ", body_style=" + body_style + ", condition=" + condition
                + ", notes=" + notes + ", license_number=" + license_number + ", colour=" + colour + ", transmission="
                + transmission + ", fuel_type=" + fuel_type + ", year=" + year + ", price=" + price + ", number_doors="
                + number_doors + ", mileage=" + mileage + ", engine_size=" + engine_size + ", vehicle_id=" + vehicle_id
                + "]";
    }

    /**
     * Gets the make.
     *
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the make.
     *
     * @param make the new make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model.
     *
     * @param model the new model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the body style.
     *
     * @return the body style
     */
    public String getBody_style() {
        return body_style;
    }

    /**
     * Sets the body style.
     *
     * @param body_style the new body style
     */
    public void setBody_style(String body_style) {
        this.body_style = body_style;
    }

    /**
     * Gets the condition.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition.
     *
     * @param condition the new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes the new notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the license number.
     *
     * @return the license number
     */
    public String getLicense_number() {
        return license_number;
    }

    /**
     * Sets the license number.
     *
     * @param license_number the new license number
     */
    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour.
     *
     * @param colour the new colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Gets the transmission.
     *
     * @return the transmission
     */
    public String getTransmission() {
        return transmission;
    }

    /**
     * Sets the transmission.
     *
     * @param transmission the new transmission
     */
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    /**
     * Gets the fuel type.
     *
     * @return the fuel type
     */
    public String getFuel_type() {
        return fuel_type;
    }

    /**
     * Sets the fuel type.
     *
     * @param fuel_type the new fuel type
     */
    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year.
     *
     * @param year the new year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price.
     *
     * @param price the new price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Gets the number doors.
     *
     * @return the number doors
     */
    public int getNumber_doors() {
        return number_doors;
    }

    /**
     * Sets the number doors.
     *
     * @param number_doors the new number doors
     */
    public void setNumber_doors(int number_doors) {
        this.number_doors = number_doors;
    }

    /**
     * Gets the mileage.
     *
     * @return the mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Sets the mileage.
     *
     * @param mileage the new mileage
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    /**
     * Gets the engine size.
     *
     * @return the engine size
     */
    public int getEngine_size() {
        return engine_size;
    }

    /**
     * Sets the engine size.
     *
     * @param engine_size the new engine size
     */
    public void setEngine_size(int engine_size) {
        this.engine_size = engine_size;
    }

    /**
     * Gets the vehicle id.
     *
     * @return the vehicle id
     */
    public int getVehicle_id() {
        return vehicle_id;
    }

    /**
     * Sets the vehicle id.
     *
     * @param vehicle_id the new vehicle id
     */
    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }


}
