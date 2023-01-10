package com.example.emzio;

public class Message {
    private String message;
    private String id_user;


    public  Message(){

    }

    Message(String msg,String id){
        this.message = msg;
        this.id_user=id;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
