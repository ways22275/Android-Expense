package com.tom.expense;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by hank on 2018/3/10.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{
    Cursor cursor;
    public static final String TAG = ExpenseAdapter.class.getSimpleName();
    /*public ExpenseAdapter(Cursor cursor){
        this.cursor = cursor;
    }*/
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public ExpenseAdapter() {

    }

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }


    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        final Expense expense = new Expense(cursor);
        holder.setModel(expense);
        holder.itemView.setTag(expense);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + expense.getInfo());
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(
                            holder.itemView,
                            expense);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null){
            return 0;
        }else {
            return cursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView infoTextView;
        TextView amountTextView;
        CheckBox readCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.item_date);
            infoTextView = itemView.findViewById(R.id.item_info);
            amountTextView = itemView.findViewById(R.id.item_amount);
            readCheckBox = itemView.findViewById(R.id.item_read);
        }

        public void setModel(final Expense expense) {
            dateTextView.setText(expense.getDate());
            infoTextView.setText(expense.getInfo());
            amountTextView.setText(expense.getAmount()+"");
            readCheckBox.setChecked((expense.getRead()==0)? false: true);
            readCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    expense.setRead(checked? 1: 0);
                    itemView.getContext().getContentResolver()
                            .update(ExpenseContract.CONTENT_URI,
                                    expense.getContentValues(),
                                    ExpenseContract.COL_ID + " = ?",
                                    new String[]{expense.getId()+""});
                }
            });
        }
    }

    public interface OnRecyclerViewItemClickListener{
        public void onItemClick(View view, Expense expense);
    }
}


