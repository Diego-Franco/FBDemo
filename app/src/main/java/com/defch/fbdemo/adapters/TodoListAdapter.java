package com.defch.fbdemo.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.defch.fbdemo.MainActivity;
import com.defch.fbdemo.R;
import com.defch.fbdemo.model.Item;
import com.defch.fbdemo.realm.RealmController;

import io.realm.Realm;


/**
 * Created by DiegoFranco on 9/20/16.
 */

public class TodoListAdapter extends RealmRecyclerViewAdapater<Item>
{

    private Activity activity;
    public Realm realm;

    private int lastAnimatedPosition = -1;

    public TodoListAdapter(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder( LayoutInflater.from(activity).inflate(R.layout.todo_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        realm = RealmController.getInstance().getRealm();

        runAnimationHolder(holder, position);
        final Item item = getItem(position);

        ViewHolder vHolder = (ViewHolder) holder;

        vHolder.mTextView.setText(item.getAction());

        if(item.getStatus().equalsIgnoreCase("TODO"))
        {
            vHolder.statusTextView.setTextColor(activity.getResources().getColor(android.R.color.holo_red_light));
            vHolder.checkTask.setChecked(false);
        }
        else if(item.getStatus().equalsIgnoreCase("DONE"))
        {
            vHolder.statusTextView.setTextColor(activity.getResources().getColor(android.R.color.holo_green_light));
            vHolder.checkTask.setChecked(true);
        }

        vHolder.statusTextView.setText(item.getStatus());

        vHolder.checkTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                ((MainActivity)activity).deleteRow(position);
                return true;
            }
        });

        vHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)activity).editTodoItem(position);
            }
        });

        vHolder.checkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                ((MainActivity)activity).todoToDone(item, isChecked);
            }
        });
    }

    public void updateItem(Item item)
    {

    }

    public void runAnimationHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position > lastAnimatedPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity,
                    (position > lastAnimatedPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            holder.itemView.startAnimation(animation);
            lastAnimatedPosition = position;
        }
    }

    @Override
    public int getItemCount()
    {
        return getRealmAdapter() != null ? getRealmAdapter().getCount() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public LinearLayout container;
        public TextView mTextView;
        public TextView statusTextView;
        public CheckBox checkTask;
        public ViewHolder(View v) {
            super(v);
            container = (LinearLayout) v.findViewById(R.id.container);
            mTextView = (TextView) v.findViewById(R.id.list_item);
            statusTextView = (TextView) v.findViewById(R.id.list_item_status);
            checkTask = (CheckBox) v.findViewById(R.id.check_task);
        }
    }
}
