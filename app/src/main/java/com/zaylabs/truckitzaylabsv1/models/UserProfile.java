package com.zaylabs.truckitzaylabsv1.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class UserProfile {

    public String fullname;
    public String phone;

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }
}
// [END blog_user_class]
