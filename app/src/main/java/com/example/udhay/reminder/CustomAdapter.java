package com.example.udhay.reminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    private RecyclerViewClickInterface clickInterface;

    public Cursor getCursor() {
        return cursor;
    }



    public CustomAdapter(Context context , RecyclerViewClickInterface clickInterface){
        mContext = context;
        refreshCursor();
        this.clickInterface = clickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_iemview , viewGroup , false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInterface.onItemClick(viewHolder.getAdapterPosition(),view);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GradientDrawable gradientDrawable;
        cursor.moveToPosition(i);
        viewHolder.getMessageTextView().setText(cursor.getString(cursor.getColumnIndex(ReminderTable.COLUMN_MESSAGE)));
        int importance = cursor.getInt(cursor.getColumnIndex(ReminderTable.COLUMN_IMPORTANCE));

        switch(importance){
            case Reminder.IMPORTANCE_LOW:
                gradientDrawable = (GradientDrawable)mContext.getDrawable(R.drawable.circle);
                gradientDrawable.setColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
                viewHolder.getImageView().setImageDrawable(gradientDrawable);
                break;

            case Reminder.IMPORTANCE_INTERMEDIATE:
                gradientDrawable = (GradientDrawable)mContext.getDrawable(R.drawable.circle);
                gradientDrawable.setColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                viewHolder.getImageView().setImageDrawable(gradientDrawable);
                break;

            case Reminder.IMPORTANCE_HIGH:
                gradientDrawable = (GradientDrawable)mContext.getDrawable(R.drawable.circle);
                gradientDrawable.setColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                viewHolder.getImageView().setImageDrawable(gradientDrawable);
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
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preference_file) , Context.MODE_PRIVATE);
        String sortBy = preferences.getString(mContext.getString(R.string.sort_by) , Reminder.SORT_BY_TIME);
        if(sortBy.equals(Reminder.SORT_BY_TIME)) {
            cursor = new ReminderOpenHelper(mContext).getReadableDatabase().query(ReminderContract.ReminderTable.TABLE_NAME, null, null, null, null, null, null);
        }else if(sortBy.equals(Reminder.SORT_BY_IMPORTANCE)) {
            cursor = new ReminderOpenHelper(mContext).getReadableDatabase().query(ReminderContract.ReminderTable.TABLE_NAME, null, null, null, null, null, ReminderTable.COLUMN_IMPORTANCE+" DESC ");
        }
    }

}
interface RecyclerViewClickInterface{
    void onItemClick(int position , View view);
}
