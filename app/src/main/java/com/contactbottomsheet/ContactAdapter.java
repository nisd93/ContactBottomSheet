package com.contactbottomsheet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contactbottomsheet.contactutils.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisarg W on 1/2/2018.
 */

public class ContactAdapter extends
        RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private List<Contact> itemModels;
    private Context context;
    private OnItemClickListener clickListener;
    public ContactAdapter(Context context,
                         List<Contact> itemModels, OnItemClickListener clickListener) {
        this.context = context;
        this.itemModels = itemModels;
        this.clickListener=clickListener;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.row_contact, viewGroup, false);
        ContactHolder viewHolder = new ContactHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int i) {
        Contact cm = itemModels.get(i);

        holder.tvData.setText(cm.displayName);

        if (!cm.phoneNumbers.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String phoneNumber : cm.phoneNumbers) {
                builder.append(phoneNumber).append(" / ");
            }
            holder.tvNumber.setText(builder.toString());
        }


    }

    public Contact getAppModel(int position) {
        return itemModels.get(position);
    }

//    public ArrayList<Contact> getAllAppModel() {
//        return itemModels;
//    }

//    public void SetOnItemClickListener(
//            final OnItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }

    @Override
    public int getItemCount() {
        return itemModels == null ? 0 : itemModels.size();
    }
    public void addAll(List<Contact> temp) {
        itemModels.addAll(temp);
        ContactAdapter.this.notifyItemInserted(itemModels.size() - 1);
    }
    public void animateTo(ArrayList<Contact> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    public Contact removeItem(int position) {
        final Contact model = itemModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Contact model) {
        itemModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Contact model = itemModels.remove(fromPosition);
        itemModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(ArrayList<Contact> newModels) {
        for (int i = itemModels.size() - 1; i >= 0; i--) {
            final Contact model = itemModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Contact> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Contact model = newModels.get(i);
            if (!itemModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Contact> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Contact model = newModels.get(toPosition);
            final int fromPosition = itemModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position, Contact data);
    }

    class ContactHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView tvData;
        public TextView tvNumber;

        public ContactHolder(View itemView) {
            super(itemView);
            tvData= itemView.findViewById(R.id.tv_name);
            tvNumber= itemView.findViewById(R.id.tv_number);
            itemView.setOnClickListener(this);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getPosition(),itemModels.get(getPosition()));
            }
        }
    }
}