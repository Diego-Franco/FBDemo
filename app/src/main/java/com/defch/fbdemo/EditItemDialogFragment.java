package com.defch.fbdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.defch.fbdemo.model.Item;

/**
 * Created by DiegoFranco on 9/22/16.
 */

public class EditItemDialogFragment extends DialogFragment
{

    private static final String ITEM_ACTION = "action";
    private static final String ITEM_STATUS = "status";

    private EditText editText;
    private Spinner spinner;
    private Button cancelBtn, okBtn;

    private Context context;

    private static ItemDialogListener itemDialogListener;

    public interface ItemDialogListener
    {
        void finishedItem(Item item);
    }

    public EditItemDialogFragment() { }


    public static EditItemDialogFragment newInstance(Item item)
    {
        EditItemDialogFragment frag = new EditItemDialogFragment();

        if(item != null)
        {
            Bundle args = new Bundle();
            args.putString(ITEM_ACTION, item.getAction());
            args.putString(ITEM_STATUS, item.getStatus());
            frag.setArguments(args);
        }
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog_fragment, container);
    }
*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_dialog_fragment);

        editText = (EditText) dialog.findViewById(R.id.edit_item_text);
        spinner = (Spinner) dialog.findViewById(R.id.edit_spinner);

        cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
        okBtn = (Button) dialog.findViewById(R.id.btn_ok);

        //Utils.setWidthToDialog(context, dialog, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(getArguments() != null)
        {
            String action = getArguments().getString(ITEM_ACTION);
            editText.setText(action);
            String status = getArguments().getString(ITEM_STATUS);
            int pos = adapter.getPosition(status);
            spinner.setSelection(pos);
        }

        editText.requestFocus();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!editText.getText().toString().isEmpty())
                {
                    Item item = new Item();
                    item.setAction(editText.getText().toString());
                    item.setStatus(spinner.getSelectedItem().toString());
                    if(itemDialogListener != null)
                    {
                        itemDialogListener.finishedItem(item);
                    }
                }
                else
                {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

        return super.onCreateDialog(savedInstanceState);
    }

    public void setItemDialogListener(ItemDialogListener itemDialogListener)
    {
        this.itemDialogListener = itemDialogListener;
    }
}
