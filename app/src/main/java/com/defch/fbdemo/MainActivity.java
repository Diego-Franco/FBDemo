package com.defch.fbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.defch.fbdemo.adapters.RealmItemsAdapter;
import com.defch.fbdemo.adapters.TodoListAdapter;
import com.defch.fbdemo.model.Item;
import com.defch.fbdemo.realm.RealmController;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    RecyclerView recycler;

    @BindView(R.id.edittext)
    EditText eText;

    @BindView(R.id.add_button)
    Button addButton;

    private TodoListAdapter adapter;
    private Realm realm;
    private RealmResults<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.realm = RealmController.with(this).getRealm();

        setupRecycler();

        RealmController.with(this).refresh();

        items = realm.where(Item.class).findAll();
        setRealmAdapter(items);

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eText.getText().toString().isEmpty()) {
                    addItem(eText.getText().toString());
                }
            }
        });

        realm.addChangeListener(realmListener);
    }

    RealmChangeListener realmListener = new RealmChangeListener()
    {
        @Override
        public void onChange()
        {
            items = realm.where(Item.class).findAll();
            setRealmAdapter(items);
        }
    };

    private void addItem(final String action)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                Item item = realm.createObject(Item.class);
                item.setId(adapter.getItemCount() + 1);
                item.setAction(action);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmListener);
    }

    private void setRealmAdapter(RealmResults<Item> items) {
        RealmItemsAdapter realmItemsAdapter = new RealmItemsAdapter(this.getApplicationContext(), items, true);
        adapter.setRealmAdapter(realmItemsAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler()
    {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new TodoListAdapter(this);
        recycler.setAdapter(adapter);
    }

    public void deleteRow(final int position)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Item> deleteItem = realm.where(Item.class).findAll();

                Item item = deleteItem.get(position);

                item.removeFromRealm();

            }
        });
    }

}
