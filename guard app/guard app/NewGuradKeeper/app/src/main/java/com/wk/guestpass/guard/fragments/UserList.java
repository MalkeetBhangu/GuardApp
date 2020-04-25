package com.wk.guestpass.guard.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.wk.guestpass.guard.MainActivity;
import com.wk.guestpass.guard.R;
import com.wk.guestpass.guard.RecyclerTouchListener;
import com.wk.guestpass.guard.SessionManager;
import com.wk.guestpass.guard.adapter.UserListAdapter;
import com.wk.guestpass.guard.models.UserListModel;

import java.util.ArrayList;
import java.util.HashMap;


public class UserList extends Fragment {

    ImageView ivBack, ivLogout;
    RecyclerView usersRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView searchView;
    private SessionManager session;
    private String usersssid;
    private ArrayList<UserListModel> arrayListUserListModel;
    private UserListAdapter userListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ivBack = view.findViewById(R.id.back);
        ivLogout = view.findViewById(R.id.logout);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        searchView = view.findViewById(R.id.searchview);
        usersRecyclerView = view.findViewById(R.id.userRecycler);

        session = new SessionManager(getActivity());
        arrayListUserListModel = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HashMap<String, String> users = session.getUserDetails();
        usersssid = users.get(SessionManager.KEY_ID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                builder.setMessage(R.string.logout_dialog_message);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.logoutUser();
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        UserListModel userListModel = new UserListModel();
        userListModel.setUserName("User 1");
        userListModel.setUserImage(R.drawable.ic_user);

        UserListModel userListModel2 = new UserListModel();
        userListModel2.setUserName("User 2");
        userListModel2.setUserImage(R.drawable.ic_user);

        UserListModel userListModel3 = new UserListModel();
        userListModel3.setUserName("User 3");
        userListModel3.setUserImage(R.drawable.ic_user);

        arrayListUserListModel.add(userListModel);
        arrayListUserListModel.add(userListModel2);
        arrayListUserListModel.add(userListModel3);

        usersRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(mLayoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());


        usersRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                usersRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, final int position) {

            }
        }));

        userListAdapter = new UserListAdapter(getActivity(),arrayListUserListModel);
        usersRecyclerView.setAdapter(userListAdapter);
    }
}
