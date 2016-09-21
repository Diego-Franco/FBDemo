package com.defch.fbdemo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DiegoFranco on 9/20/16.
 */

public class Item extends RealmObject
{
    public static int counterId = 0;

    @PrimaryKey
    private int id;

    private String action;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
