package com.wk.guestpass.app.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.wk.guestpass.app.Activities.MainActivity;
import com.wk.guestpass.app.Adpaters.HomeListAdapter;
import com.wk.guestpass.app.FragAdapters.GuestHistoryAdapter;
import com.wk.guestpass.app.Models.ListModel;
import com.wk.guestpass.app.R;
import com.wk.guestpass.app.Utilities.Config;
import com.wk.guestpass.app.Utilities.RecyclerTouchListener;
import com.wk.guestpass.app.Utilities.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GuestHistory extends Fragment {

    ImageView mBack, mLogout, mNoData, expstmp, guestroles;
    SearchView mSearchView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mGuestHistoryRecyclerView;
    RelativeLayout mMainScreen;
    ProgressBar mProgressBar;
    private SessionManager session;
    RequestQueue mRequestQueue;
    StringRequest stringRequest;
    private List<ListModel> list = new ArrayList<>();
    private GuestHistoryAdapter mGuestHistoryAdapter;
    public static final String TAG = "MyTag";
    String usersssid;
    private Dialog bottomSheetDialog;
    private TextView dname, ddate, dsetime, dintime, dcntct, dguest, dpurpose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_history, container, false);
        mBack = view.findViewById(R.id.back);
        mSearchView = view.findViewById(R.id.searchview);
        mLogout = view.findViewById(R.id.logout);
        mNoData = view.findViewById(R.id.nodaata);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mGuestHistoryRecyclerView = view.findViewById(R.id.guestHistoryRecycler);
        mMainScreen = view.findViewById(R.id.overmain);
        mProgressBar = view.findViewById(R.id.progress);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getActivity());
        HashMap<String, String> users = session.getUserDetails();
        usersssid = users.get(SessionManager.KEY_ID);


        CubeGrid cubeGrid = new CubeGrid();
        cubeGrid.setColor(getResources().getColor(R.color.colorPrimary));
        cubeGrid.start();
        mProgressBar.setIndeterminateDrawable(cubeGrid);


        mGuestHistoryRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mGuestHistoryRecyclerView.setLayoutManager(mLayoutManager);
        mGuestHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mGuestHistoryRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mGuestHistoryRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                bottomsheetdialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                historyDataList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mGuestHistoryAdapter == null) {
                    return false;
                } else {
                    mGuestHistoryAdapter.getFilter().filter(newText);
                    return true;
                }

            }
        });

        historyDataList();
    }

    public void historyDataList() {
        mMainScreen.setVisibility(View.VISIBLE);
        String url = Config.homelist;
        mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.GONE);
                        list.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("message");
                            if (status.equals("succesfull")) {

                                JSONArray applist = j.getJSONArray("guest_list");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {


                                        ListModel model = new ListModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setIds(getOne.getString("guest_id"));
                                        model.setNames(getOne.getString("guest_name"));
                                        model.setMobilenm(getOne.getString("mobile"));
                                        model.setVstpurpse(getOne.getString("visit_purpose"));
                                        model.setDatess(getOne.getString("visit_date"));
                                        model.setGuestrole(getOne.getString("guest_role"));
                                        model.setGueststatus(getOne.getString("guest_status"));
                                        model.setSettime(getOne.getString("visit_time"));
                                        model.setIntime(getOne.getString("checkin_time"));
                                        model.setTtlguest(getOne.getString("total_guest"));

                                        list.add(model);
                                        mGuestHistoryAdapter = new GuestHistoryAdapter(getActivity(), list);
                                        mGuestHistoryRecyclerView.setAdapter(mGuestHistoryAdapter);
                                        mNoData.setVisibility(View.GONE);
                                        mGuestHistoryRecyclerView.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                mNoData.setVisibility(View.VISIBLE);
                                mGuestHistoryRecyclerView.setVisibility(View.GONE);
                                mMainScreen.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            mNoData.setVisibility(View.VISIBLE);
                            mGuestHistoryRecyclerView.setVisibility(View.GONE);
                            // mainscreen.setVisibility(View.GONE);
//                            Log.e("TAG", "Something Went Wrong");
                        }
                        mMainScreen.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mMainScreen.setVisibility(View.GONE);
                        NetworkDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "d29985af97d29a80e40cd81016d939af");
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", usersssid);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
    }

    private void bottomsheetdialog(int posy) {

        bottomSheetDialog = new Dialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.popuplayout);
        // bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setCancelable(true);

        // ImageView cancel = bottomSheetDialog.findViewById(R.id.cancel);
        Button back = bottomSheetDialog.findViewById(R.id.backs);

        dname = bottomSheetDialog.findViewById(R.id.detailname);
        ddate = bottomSheetDialog.findViewById(R.id.detaildate);
        dsetime = bottomSheetDialog.findViewById(R.id.settime);
        dintime = bottomSheetDialog.findViewById(R.id.intime);
        dcntct = bottomSheetDialog.findViewById(R.id.contact);
        dguest = bottomSheetDialog.findViewById(R.id.detailttlguest);
        dpurpose = bottomSheetDialog.findViewById(R.id.visitpurpose);
        guestroles = bottomSheetDialog.findViewById(R.id.guestrole);
        expstmp = bottomSheetDialog.findViewById(R.id.expstmp);

        ListModel model = list.get(posy);

        dname.setText(model.getNames().substring(0, 1).toUpperCase() + model.getNames().substring(1));
        ddate.setText(model.getDatess());
        dsetime.setText(convertTime(model.getSettime()));
        dintime.setText(model.getIntime());
        dcntct.setText(model.getMobilenm());
        dguest.setText(model.getTtlguest() + " GUEST");
        dpurpose.setText(model.getVstpurpse());
        if (model.getGuestrole().equals("1")) {
            guestroles.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumbup));
        } else {
            guestroles.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumbdown));
        }
        if (model.gueststatus.equals("2")) {
            expstmp.setVisibility(View.VISIBLE);
        }
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private String convertTime(String times) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mmaa");

        try {
            Date date = dateFormat.parse(times);
            times = dateFormat2.format(date);
            Log.e("Time", times);
        } catch (ParseException e) {
        }
        return times;
    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(getActivity());
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                historyDataList();
            }
        });
        dialogs.show();
    }
}
