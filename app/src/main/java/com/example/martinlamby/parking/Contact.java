package com.example.martinlamby.parking;

/**
 * Created by martinlamby on 25.08.15.
 */

//Custom data type for contactlist (Sharing-function)

public class Contact {

    private String name;
    private String emailAddress;
    private String emailType;
    private boolean isSelected;


    public Contact(String name , String emailAddress, String emailType) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.emailType = emailType;
        isSelected = false;
    }

    public void setIsSelected(boolean selectedValue){
        isSelected = selectedValue;
    }

    public String getName(){
        return name;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public String getEmailType(){
        return emailType;
    }

    public boolean getIsSelected(){
        return isSelected;
    }


}
