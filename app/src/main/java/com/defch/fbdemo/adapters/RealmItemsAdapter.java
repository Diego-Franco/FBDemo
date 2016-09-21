package com.defch.fbdemo.adapters;

import android.content.Context;

import com.defch.fbdemo.model.Item;

import io.realm.RealmResults;

/**
 * Created by DiegoFranco on 9/20/16.
 */

public class RealmItemsAdapter extends RealmModelAdapter<Item> {

    public RealmItemsAdapter(Context context, RealmResults<Item> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
