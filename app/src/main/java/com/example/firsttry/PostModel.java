package com.example.firsttry;

public class PostModel {
    private String email;
    private String imgUrl;
    private String text;

    public PostModel() {}

    public PostModel(String email, String imgUrl, String text) {
        this.email = email;
        this.imgUrl = imgUrl;
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "email='" + email + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

