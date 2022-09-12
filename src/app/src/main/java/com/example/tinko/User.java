package com.example.tinko;

// this class is used to store the user data, then we send this to firebase
public class User {

    public String fname, sname, email;

    //creating two constructors

    // this will remain empty
    // so if you want to create an empty object of this class we can get
    // access to these variables
    public User(){

    }


    public User(String fname, String sname, String email){
        this.fname = fname;
        this.sname = sname;
        this.email = email;
    }
}
