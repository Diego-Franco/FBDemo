package com.defch.fbdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

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

    private void addItem(final Item addItem)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                Item item = realm.createObject(Item.class);
                item.setAction(addItem.getAction());
                item.setStatus(addItem.getStatus());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add)
        {
            showEditDialog(null);
        }
        return false;
    }

    private void showEditDialog(Item item)
    {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment alertDialog = EditItemDialogFragment.newInstance(item);
        alertDialog.setItemDialogListener(itemDialogListener);
        alertDialog.show(fm, "fragment_alert");
    }

    EditItemDialogFragment.ItemDialogListener itemDialogListener = new EditItemDialogFragment.ItemDialogListener()
    {
        @Override
        public void finishedItem(Item item)
        {
            addItem(item);
        }
    };

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

    private void removeItemFromDB(final int position)
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

    public void deleteRow(final int position)
    {
       AlertDialog aDialog =  new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.remove_item)
                .setMessage(R.string.remove_item_msg )
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                removeItemFromDB(position);
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                            }
                        }).create();

        aDialog.show();
    }

    public void editTodoItem(int position)
    {

        RealmResults<Item> deleteItem = realm.where(Item.class).findAll();

        Item item = deleteItem.get(position);

        showEditDialog(item);
    }

}
