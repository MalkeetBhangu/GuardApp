package com.wk.guestpass.guard.chatModels;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by osx on 15/09/17.
 */

public class MessagesTwo implements IMessage, MessageContentType.Image, MessageContentType, Serializable {



    public String id;
    public String content;
    public boolean isRead;
    public long timestamp;
    public String type;
    public String fromID;
    public String toID;
    public Author author;
    public Image image;
    public Video video;
    public Audio audio;
    public Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return content;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(timestamp);
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public static class Image implements Serializable {

        public String url;

        public Image(String url) {
            this.url = url;
        }

        public Image() {
        }
    }

    public static class Video implements Serializable {

        public String url;
        public int duration;
        public String thumbnail;

        public Video(String url, int duration, String thumbnail) {
            this.url = url;
            this.duration = duration;
            this.thumbnail = thumbnail;
        }

        public Video() {
        }
    }

    public static class Audio implements Serializable {

        public String url;
        public int duration;

        public Audio(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public Audio() {
        }
    }


    public static class Location implements Serializable {
        public double lat;
        public double lng;
        public String placeName;
        public String placeAddress;

        public Location(double lat, double lng, String placeName, String placeAddress) {
            this.lat = lat;
            this.lng = lng;
            this.placeName = placeName;
            this.placeAddress = placeAddress;
        }

        public Location() {
        }
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
