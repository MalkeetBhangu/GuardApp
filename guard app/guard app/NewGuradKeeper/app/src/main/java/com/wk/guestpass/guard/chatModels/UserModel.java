package com.wk.guestpass.guard.chatModels;

import java.io.Serializable;

/**
 * Created by visionmac3 on 15/11/17.
 */

public class UserModel implements Serializable {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    String email;
    String name;
    String profilePicLink;
}
