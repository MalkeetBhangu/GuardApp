package com.wk.guestpass.app.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wk.guestpass.app.Activities.SignUpActivity;
import com.wk.guestpass.app.R;
import com.wk.guestpass.app.Utilities.SessionManager;

public class ChangePin extends Fragment {

    EditText edtCurrentPin, edtNewPin, edtConfirmPin;
    Button btnChangPin;
    RelativeLayout rlBack, rlLogout;
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_pin, container, false);

        edtCurrentPin = view.findViewById(R.id.currentPin);
        edtNewPin = view.findViewById(R.id.newPin);
        edtConfirmPin = view.findViewById(R.id.confirmPin);
        btnChangPin = view.findViewById(R.id.btnChangePin);
        rlBack = view.findViewById(R.id.rlBack);
        rlLogout = view.findViewById(R.id.rlLogout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getActivity());

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        rlLogout.setOnClickListener(new View.OnClickListener() {
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

        btnChangPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCurrentPin.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), R.string.fill_current_pin, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (edtNewPin.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), R.string.fill_new_pin, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (edtConfirmPin.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), R.string.fill_confirm_pin, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (!edtNewPin.getText().toString().equals(edtConfirmPin.getText().toString())){
                    Toast toast = Toast.makeText(getActivity(), R.string.new_pin_and_confirm_pin_same, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(getActivity(), R.string.pin_change_successful, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
