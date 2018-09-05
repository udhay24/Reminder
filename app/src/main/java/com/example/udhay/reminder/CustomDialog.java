package com.example.udhay.reminder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialog extends DialogFragment {


    public CustomDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String sortOption = getActivity().getSharedPreferences(getString(R.string.shared_preference_file) , Context.MODE_PRIVATE)
                .getString(getString(R.string.sort_by) , Reminder.SORT_BY_TIME);

        int position = 0;
        if(sortOption.equals(Reminder.SORT_BY_TIME)){
            position = 0;
        }else if (sortOption.equals(Reminder.SORT_BY_IMPORTANCE)){
            position = 1;
        }


        builder.setTitle(getResources().getString(R.string.sort_by))
                .setSingleChoiceItems(R.array.sort_by_array, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                                getActivity().getSharedPreferences(getString(R.string.shared_preference_file) , Context.MODE_PRIVATE)
                                        .edit().putString(getString(R.string.sort_by) , Reminder.SORT_BY_TIME).apply();

                                break;
                            case 1:
                                getActivity().getSharedPreferences(getString(R.string.shared_preference_file) , Context.MODE_PRIVATE)
                                        .edit().putString(getString(R.string.sort_by) , Reminder.SORT_BY_IMPORTANCE).apply();
                                break;
                            default:
                                    getActivity().getSharedPreferences(getString(R.string.shared_preference_file) , Context.MODE_PRIVATE)
                                            .edit().putString(getString(R.string.sort_by) , Reminder.SORT_BY_TIME).apply();
                        }
                        MainActivity.customAdapter.refreshCursor();
                        MainActivity.customAdapter.notifyDataSetChanged();
                        getDialog().dismiss();

                    }
                });
        return builder.create();
    }

}
