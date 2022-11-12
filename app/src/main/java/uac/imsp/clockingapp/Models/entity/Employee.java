package uac.imsp.clockingapp.Models.entity;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Employee implements IEmployee {
    public final static int EMPTY_NUMBER=13,
                    INVALID_NUMBER=1,
                    EMPTY_LASTNAME=2,
                    INVALID_LASTNAME=3,
                    EMPTY_FIRSTNAME=4,
                    INVALID_FIRSTNAME=5,

                    EMPTY_USERNAME=6,
                  INVALID_USERNAME=11,

                    EMPTY_PICTURE=7, EMPTY_PASSWORD=9,
                            INVALID_PASSWORD=10,EMPTY_MAIL=14,INVALID_MAIL=12,
                            EMPTY_BIRTHDATE=15

                    ;

public  final static String SIMPLE="Simple", HEAD="Directeur",CHIEF="Chef personnel";


    //Les infos personnelles sur l'employé
    private int RegistrationNumber;

    private String Firstname;

    private String Lastname;
    private char Gender;
    private String Birthdate;

    private String MailAddress;
    private byte[] Picture;

    private String Username;
    private String Password;
    private String Type;
    private byte[] Workdays;



    //Constructeurs


    public Employee(int registrationNumber, String lastname,
                    String firstname, char gender, String birthdate,
                    String mailAddress, byte[] picture,  String username,
                    String password,String type) {

        RegistrationNumber = registrationNumber;

        Firstname = firstname;
        Lastname = lastname;
        Gender = gender;
        Birthdate = birthdate;
        MailAddress = mailAddress;
        Picture = picture;
        Username = username;
        Password = password;
        Type=type;

    }

    public Employee(int registrationNumber, String lastname, String firstname,

                    char gender, String birthdate, String mailAddress, String username,
                    String password,String type) {

        this(registrationNumber, lastname,firstname, gender,
                birthdate, mailAddress, null,  username, password,type);


    }
    public Employee(String username,String password){
        Username=username;
        Password=password;
    }

    public Employee(int registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    //getters
    public int getRegistrationNumber() {
        return RegistrationNumber;
    }

    public String getFirstname() {
        return Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public char getGender() {
        return Gender;
    }


    public String getBirthdate() {
        return Birthdate;
    }

    public String getMailAddress() {
        return MailAddress;
    }

    public  byte[] getPicture() {
        return Picture;
    }

    public  String getUsername(){
        return  Username;
    }
    public  String getPassword(){
        return  Password;
    }
    public String getType(){ return Type;}




    @Override
    public int isValid() {
        if(TextUtils.isEmpty(""+RegistrationNumber))
            return EMPTY_NUMBER;
        else if(hasInvalidNumber())
            return INVALID_NUMBER;
        else if(TextUtils.isEmpty(Lastname))
            return EMPTY_LASTNAME;
        else if (hasInvalidLastName())
            return INVALID_LASTNAME;
       else if(TextUtils.isEmpty(Firstname))
            return EMPTY_FIRSTNAME;
        else if (hasInvalidFirstname())
            return INVALID_FIRSTNAME;
        else if(TextUtils.isEmpty(Birthdate))
            return EMPTY_BIRTHDATE;
        else if (TextUtils.isEmpty(MailAddress))
            return EMPTY_MAIL;
        else if(hasInvalidEmail())
            return INVALID_MAIL;
        else if(TextUtils.isEmpty(Username))
            return EMPTY_USERNAME;
        else if (hasInvalidUsername())
            return INVALID_USERNAME;
        else if (TextUtils.isEmpty(Password))
            return EMPTY_PASSWORD;
        else if(hasInvalidPassword())
            return  INVALID_PASSWORD;

        return 0;
    }

    @Override
    public void setPassword(String password) {
        Password=password;

    }


    //Setters
    public void setRegistrationNumber(int registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;

    }
    public int validUser(){
        if(TextUtils.isEmpty(Username))
            return EMPTY_USERNAME;
        else if (hasInvalidUsername())
            return INVALID_USERNAME;
        else if (TextUtils.isEmpty(Password))
            return EMPTY_PASSWORD;
        else if(hasInvalidPassword())
            return  INVALID_PASSWORD;
        return 0;

    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public void setGender(char gender) {
        Gender = gender;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public void setMailAddress(String mailAddress) {
        MailAddress = mailAddress;

    }

    public void setPicture(byte[] picture) {
        Picture = picture;
    }
    public void setType(String type){
        Type=type;
    }

    public void setUsername(String username){
        Username=username;

    }
    public  boolean hasInvalidUsername() {


        return !Username.matches("^[A-Z][A-Za-z0-9]{5,29}$");
    }

    public boolean hasInvalidLastName() {
        String pattern = "^[A-ZÂÊÛÎÔÁÉÚÍÓÀÈÙÌÒÇ][A-Za-zâêîûôáéíúóàèùìòç]" +

                "+([-' ][A-ZÂÊÛÎÔÁÉÚÍÓÀÈÙÌÒÇ][a-zâêîûôáéíúóàèùìòç]+)?";
        return !Lastname.matches(pattern);



    }    public boolean hasInvalidFirstname(){
        String pattern ="^[A-ZÂÊÛÎÔÁÉÚÍÓÀÈÙÌÒÇ][a-zâêîûôáéíúóàèùìòç]+" +
                "([-'][A-ZÂÊÛÎÔÁÉÚÍ ÓÀÈÙÌÒÇ][a-zâêîûôáéíúóàèùìòç]+)?";
        return !Firstname.matches(pattern);


    }

    public boolean hasInvalidNumber(){
        return !String.valueOf(RegistrationNumber).matches("^[0-9]+$");


    }

    public boolean hasInvalidEmail() {
return  !Patterns.EMAIL_ADDRESS.matcher(MailAddress).matches();
    }
    public  boolean hasInvalidPassword() {

        return !(Password.length()>=6&& is_in_the_range("[A-Z]")&&

                is_in_the_range("[a-z]")&&is_in_the_range("[0-9]")&&
                is_in_the_range("\\W"));
    }
    public int nb_occur(String str, String pattern){

        Matcher matcher= Pattern.compile(pattern).matcher(str);
        int count=0;
        while(matcher.find())
            count++;
        return count;
    }
    public boolean is_in_the_range(String pattern){
        return 1<= nb_occur(Password,pattern);


    }

}