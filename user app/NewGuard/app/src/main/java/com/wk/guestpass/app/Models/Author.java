package com.wk.guestpass.app.Models;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

/**
 * Created by osx on 15/09/17.
 */

public class Author implements IUser, Serializable {


    public String id;
    public String name;
    public String avatar;
    public boolean online;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
