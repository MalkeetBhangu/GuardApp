package com.wk.guestpass.guard.chatModels;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.io.Serializable;
import java.util.List;

/**
 * Created by osx on 23/09/17.
 */

public class DefaultDialog implements IDialog<MessagesTwo>, Serializable {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private List<Author> users;
    private MessagesTwo lastMessage;
    private int unreadCount;


    public DefaultDialog(String id, String dialogPhoto, String dialogName, List<Author> users, MessagesTwo lastMessage, int unreadCount) {
        this.id = id;
        this.dialogPhoto = dialogPhoto;
        this.dialogName = dialogName;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }



    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<Author> getUsers() {
        return users;
    }

    @Override
    public MessagesTwo getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(MessagesTwo message) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof DefaultDialog))
        {
            return false;
        }

        DefaultDialog that = (DefaultDialog) obj;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
