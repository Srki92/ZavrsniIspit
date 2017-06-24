package com.example.androiddevelopment.agencijazanekretnine.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 24.6.17..
 */
@DatabaseTable(tableName = Nekretnina.TABLE_NAME)
public class Nekretnina  {

    public static final String TABLE_NAME= "nekretnina";
    public static final String FIELD_NEKRETNINA_ID = "id";
    public static final String TABLE_NEKRETNINA_NAME = "name";
    public static final String TABLE_NEKRETNINA_DESCRIPTION = "description";
    public static final String TABLE_NEKRETNINA_SCORE = "score";
    public static final String TABLE_NEKRETNINA_ADRESA = "adresa";
    public static final String TABLE_NEKRETNINA_PICTURES = "movies";
    public static final String TABLE_NEKRETNINA_PHONE = "phoneNumber";
    public static final String TABLE_NEKRETNINA_KVADRATURA = "kvadratura";
    public static final String TABLE_NEKRETNINA_ROOMS ="numberOfRooms";

    @DatabaseField(columnName = FIELD_NEKRETNINA_ID, generatedId = true)
    private int nId;

    @DatabaseField(columnName = TABLE_NEKRETNINA_NAME)
    private String nName;

    @DatabaseField(columnName = TABLE_NEKRETNINA_DESCRIPTION)
    private String nDescription;

    @DatabaseField(columnName = TABLE_NEKRETNINA_SCORE)
    private float nScore;

    @DatabaseField(columnName = TABLE_NEKRETNINA_ADRESA)
    private String nAdresa;

    @DatabaseField(columnName = TABLE_NEKRETNINA_PICTURES)
    private String nPictures;


    @DatabaseField(columnName = TABLE_NEKRETNINA_PHONE)
    private String nPhoneNumber;


    @DatabaseField(columnName = TABLE_NEKRETNINA_KVADRATURA)
    private String nKvadratura;


    @DatabaseField(columnName = TABLE_NEKRETNINA_ROOMS)
    private String nNumberOfRooms;


    public Nekretnina() {
    }

    public Nekretnina(int nId, String nName, String nDescription, float nScore, String nAdresa, String nPictures, String nPhoneNumber, String nKvadratura, String nNumberOfRooms) {
        this.nId = nId;
        this.nName = nName;
        this.nDescription = nDescription;
        this.nScore = nScore;
        this.nAdresa = nAdresa;
        this.nPictures = nPictures;
        this.nPhoneNumber = nPhoneNumber;
        this.nKvadratura = nKvadratura;
        this.nNumberOfRooms = nNumberOfRooms;
    }

    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public String getnDescription() {
        return nDescription;
    }

    public void setnDescription(String nDescription) {
        this.nDescription = nDescription;
    }

    public float getnScore() {
        return nScore;
    }

    public void setnScore(float nScore) {
        this.nScore = nScore;
    }

    public String getnAdresa() {
        return nAdresa;
    }

    public void setnAdresa(String nAdresa) {
        this.nAdresa = nAdresa;
    }

    public String getnPictures() {
        return nPictures;
    }

    public void setnPictures(String nPictures) {
        this.nPictures = nPictures;
    }

    public String getnPhoneNumber() {
        return nPhoneNumber;
    }

    public void setnPhoneNumber(String nPhoneNumber) {
        this.nPhoneNumber = nPhoneNumber;
    }

    public String getnKvadratura() {
        return nKvadratura;
    }

    public void setnKvadratura(String nKvadratura) {
        this.nKvadratura = nKvadratura;
    }


    public String getnNumberOfRooms() {
        return nNumberOfRooms;
    }

    public void setnNumberOfRooms(String nNumberOfRooms) {
        this.nNumberOfRooms = nNumberOfRooms;
    }
}
