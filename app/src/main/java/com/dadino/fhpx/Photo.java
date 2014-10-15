package com.dadino.fhpx;


public class Photo {
    public int id;
    public String image_url;
    public String name;
    public String description;
    public User user;

    static class User {
        public String fullname;
        public String userpic_url;
    }
}
