package com.wk.guestpass.app.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.wk.guestpass.app.Activities.MainActivity;
import com.wk.guestpass.app.R;
import com.wk.guestpass.app.Utilities.SessionManager;

import java.util.HashMap;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    RelativeLayout rlBack, rlLogout;
    EditText edtPhoneNumber, edtUserName, edtUserEmail, edtAlternativeNumber;
    Button btnUpdate, btnChangePin;
    CircularImageView civProfileImage;
    private static final int PICK_IMAGE_IN_GALLERY = 101;
    private static final int PICK_IMAGE_FROM_CAMERA = 111;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 1111;
    private SessionManager session;
    ImageButton ivChangeProfile;
    String userEmail, userPhoneNumber, userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        rlBack = view.findViewById(R.id.rlBack);
        rlLogout = view.findViewById(R.id.rlLogout);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtUserName = view.findViewById(R.id.edtUserName);
        edtUserEmail = view.findViewById(R.id.edtEmail);
        edtAlternativeNumber = view.findViewById(R.id.edtAlternativeNumber);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        civProfileImage = view.findViewById(R.id.circularImageView);
        ivChangeProfile = view.findViewById(R.id.ivProfile);
        btnChangePin = view.findViewById(R.id.btnChangePin);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getActivity());
        HashMap<String, String> users = session.getUserDetails();
        userEmail = users.get(SessionManager.KEY_USER_EMAIL);
        userPhoneNumber = users.get(SessionManager.KEY_MOB);
        userName = users.get(SessionManager.KEY_USER);

        edtUserName.setText(userName);
        edtUserEmail.setText(userEmail);
        edtPhoneNumber.setText(userPhoneNumber);

        edtPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity(), R.string.you_cannot_edit_it, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

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

        ivChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetProfile();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        btnChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                transaction.replace(R.id.homepage, new ChangePin());
                transaction.commit();
            }
        });
    }

    private void openBottomSheetProfile(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_gallery);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

        ImageButton imageButtonGallery = bottomSheetDialog.findViewById(R.id.ibGallery);
        ImageButton imageButtonCamera = bottomSheetDialog.findViewById(R.id.ibCamera);
        ImageButton imageButtonRemovePhoto = bottomSheetDialog.findViewById(R.id.ibRemove);


        imageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, PICK_IMAGE_IN_GALLERY);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE);
                    }
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, PICK_IMAGE_IN_GALLERY);
                }

                bottomSheetDialog.dismiss();
            }
        });

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, PICK_IMAGE_FROM_CAMERA);
                    }else {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{CAMERA,READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE_CAMERA);
                    }
                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, PICK_IMAGE_FROM_CAMERA);
                }

                bottomSheetDialog.dismiss();
            }
        });

        imageButtonRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.remove_profile_photo);
                builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        civProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void updateData() {
        if (edtUserName.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), R.string.name_is_remaining, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (edtUserEmail.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), R.string.enter_the_email, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getActivity(), R.string.successful, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_IN_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            civProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == PICK_IMAGE_FROM_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            civProfileImage.setImageBitmap(photo);
        }
    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int result1 = ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_IN_GALLERY);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.permission_gallery, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, PICK_IMAGE_FROM_CAMERA);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.permission_camera, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
