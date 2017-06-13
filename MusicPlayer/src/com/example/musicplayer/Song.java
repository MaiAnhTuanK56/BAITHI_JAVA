package com.example.musicplayer;

public class Song {
    private String id;
    private String title;
    private String singer;
    private String duration;
    private String icon;
     
    public String getId(){
        return id;
    }
     
    public void setId(String id){
        this.id = id;
    }
     
    public String getTitle(){
        return title;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public String getSinger(){
        return singer;
    }
     
    public void setSinger(String singer){
        this.singer = singer;
    }
     
    public String getDuration(){
        return duration;
    }
     
    public void setDuration(String duration){
        this.duration = duration;
    }
     
    public String getIcon(){
        return icon;
    }
     
    public void setIcon(String icon){
        this.icon = icon;
    }
}
