package com.example.apptoyselling.model;

public class MessageModel {
    boolean success;
    String message;
    String name;

    public MessageModel(boolean success, String message, String name) {
        this.success = success;
        this.message = message;
        this.name = name;
    }

    public MessageModel() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
