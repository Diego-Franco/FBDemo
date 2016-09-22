package com.defch.fbdemo.model;

import io.realm.RealmObject;

/**
 * Created by DiegoFranco on 9/20/16.
 */

public class Item extends RealmObject
{
    private String action;

    private String status;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
