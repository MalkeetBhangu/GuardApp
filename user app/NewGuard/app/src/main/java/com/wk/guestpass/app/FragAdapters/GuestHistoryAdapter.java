package com.wk.guestpass.app.FragAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.wk.guestpass.app.Models.ListModel;
import com.wk.guestpass.app.R;

import java.util.ArrayList;
import java.util.List;

public class GuestHistoryAdapter extends RecyclerView.Adapter<GuestHistoryAdapter.MyViewHolder> implements Filterable {

    private Context context;
    int lastPosition = 0;
    private List<ListModel> list;
    private List<ListModel> secondList;

    public GuestHistoryAdapter(Context context, List<ListModel> list) {
        this.context = context;
        this.list = list;
        this.secondList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ListModel model = list.get(position);
        holder.tvGuestName.setText(model.getNames().substring(0, 1).toUpperCase() + model.getNames().substring(1));
        holder.tvGuestContact.setText(model.getMobilenm());
        holder.tvTotalGuest.setText(model.getTtlguest() + " Guest");
        holder.tvVisitTime.setText(model.getSettime());
        holder.tvInTime.setText(model.getIntime());
        holder.tvDate.setText(model.getDatess());

        if (model.getGuestrole().equals("1")) {
            holder.ivThumb.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thumbup));
        } else {
            holder.ivThumb.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thumbdown));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public android.widget.Filter getFilter() {

        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    list = secondList;
                } else {

                    ArrayList<ListModel> filteredList = new ArrayList<>();

                    for (ListModel androidVersion : secondList) {

                        if (androidVersion.getNames().toLowerCase().contains(charString) ||
                                androidVersion.getNames().contains(charString) ||
                                androidVersion.getMobilenm().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<ListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumb;
        TextView tvGuestName, tvGuestContact, tvTotalGuest, tvInTime, tvVisitTime, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.thumb);
            tvGuestName = itemView.findViewById(R.id.guestName);
            tvGuestContact = itemView.findViewById(R.id.guestContact);
            tvTotalGuest = itemView.findViewById(R.id.totalGuest);
            tvInTime = itemView.findViewById(R.id.inTime);
            tvVisitTime = itemView.findViewById(R.id.visitTime);
            tvDate = itemView.findViewById(R.id.date);
        }
    }
}
