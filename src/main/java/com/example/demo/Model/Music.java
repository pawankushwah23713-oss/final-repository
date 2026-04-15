package com.example.demo.Model;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "music")
public class Music {

    @Id
    private String id;

    private String title;
    private String artist;
  
    private String username;   // kisne upload kiya
    private String url;        // cloudinary URL
    private String publicId;   // delete/update ke liye
    private int likes;
    private long duration;
   
    public void setId(String id) {
        this.id = id;
    }
  
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getUsername() {
        return username;    
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPublicId() {
        return publicId;
    }
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

public Music(){

}



    
}