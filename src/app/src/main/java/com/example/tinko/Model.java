package com.example.tinko;

// model class to show data

public class Model {

    // zero argument constructor, required for firebase
    Model(){}

    // setting variables
    String turl;
    String name;
    String username;
    String link;

    // constructor
    public Model(String turl, String name, String username, String link) {
        this.turl = turl;
        this.name = name;
        this.username = username;
        this.link = link;
    }

    // getters & setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
