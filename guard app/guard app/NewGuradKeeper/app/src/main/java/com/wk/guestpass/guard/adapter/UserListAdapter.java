package com.wk.guestpass.guard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.wk.guestpass.guard.R;
import com.wk.guestpass.guard.models.UserListModel;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<UserListModel> arrayListUserListModel;

    public UserListAdapter(Context context, ArrayList<UserListModel> arrayListUserListModel) {
        mContext = context;
        this.arrayListUserListModel = arrayListUserListModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.civUser.setImageResource(arrayListUserListModel.get(i).getUserImage());
        myViewHolder.tvUserName.setText(arrayListUserListModel.get(i).getUserName());
    }

    @Override
    public int getItemCount() {
        return arrayListUserListModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView civUser;
        TextView tvUserName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            civUser = itemView.findViewById(R.id.circularImageView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}
