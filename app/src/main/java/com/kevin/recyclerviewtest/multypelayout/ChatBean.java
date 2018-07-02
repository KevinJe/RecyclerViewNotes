package com.kevin.recyclerviewtest.multypelayout;

/**
 * Created by Kevin Jern on 2018/5/5 21:09.
 */
public class ChatBean {
    private int type;
    private String text;

    public ChatBean(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
