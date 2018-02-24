package com.zaylabs.truckitzaylabsv1.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String fullname;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String fullname, String phone) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phone=phone;
    }

}
// [END blog_user_class]
