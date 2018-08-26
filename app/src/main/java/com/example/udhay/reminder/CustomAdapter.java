package com.example.udhay.reminder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udhay.reminder.ReminderContract.ReminderTable;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context mContext ;
    private Cursor cursor;

    public Cursor getCursor() {
        return cursor;
    }



    public CustomAdapter(Context context){
        mContext = context;
        refreshCursor();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_iemview , viewGroup , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        cursor.moveToPosition(i);
        viewHolder.getMessageTextView().setText(cursor.getString(cursor.getColumnIndex(ReminderTable.COLUMN_MESSAGE)));
        int importance = cursor.getInt(cursor.getColumnIndex(ReminderTable.COLUMN_IMPORTANCE));
        switch(importance){
            case Reminder.IMPORTANCE_LOW:
                viewHolder.getImageView().setImageResource(android.R.color.black);
                break;
            case Reminder.IMPORTANCE_INTERMEDIATE:
                viewHolder.getImageView().setImageResource(android.R.color.holo_blue_bright);
                break;
            case Reminder.IMPORTANCE_HIGH:
                viewHolder.getImageView().setImageResource(android.R.color.holo_green_light);
                break;
             default:
                viewHolder.getImageView().setImageResource(android.R.color.holo_blue_bright);
        }


    }

    @Override
    public int getItemCount() {
      return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView messageTextView;

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getMessageTextView() {
            return messageTextView;
        }

        public ViewHolder(View itemView ){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

    }

    public void refreshCursor(){
        cursor =  new ReminderOpenHelper(mContext).getReadableDatabase().query(ReminderContract.ReminderTable.TABLE_NAME , null , null, null ,null , null , null );

    }
}
