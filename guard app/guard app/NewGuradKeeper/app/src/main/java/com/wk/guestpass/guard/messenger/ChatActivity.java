package com.wk.guestpass.guard.messenger;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.camera.DefaultCameraModule;
import com.esafirm.imagepicker.features.camera.OnImageReadyListener;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.orhanobut.logger.Logger;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.wk.guestpass.guard.R;
import com.wk.guestpass.guard.adapter.IncomingLocationViewHolder;
import com.wk.guestpass.guard.adapter.OutComingLocationViewHolder;
import com.wk.guestpass.guard.chatModels.Author;
import com.wk.guestpass.guard.chatModels.MessagesTwo;
import com.wk.guestpass.guard.chatModels.UserModel;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;


public class ChatActivity extends AppCompatActivity {

    String mChatUserKey, mConversationId;
   /* DatabaseReference mUserRef;
    DatabaseReference mConversationRef;
    StorageReference mStorageRef;*/
    MessagesListAdapter<MessagesTwo> messageAdapter;
    ArrayList<MessagesTwo> mMessageList;
    DefaultCameraModule mDefaultCameraModule;
    boolean mHidden = true;
    int SELECT_IMAGE = 100;
    int CAPTURE_IMAGE = 101;
    int SELECT_AUDIO = 102;
    int PLACE_PICKER_REQUEST = 501;
    int REQUEST_CAMERA_PERMISSION = 1000;
    int REQUEST_LOCATION_PERMISSION = 1001;
    Byte CONTENT_TYPE_LOCATION = 2;
    String currentFirebaseId;
    UserModel userDetails;
    Uri imageUri;

    TextView tvName;
    ImageView ivGoBack;
    MessageInput input;
    MessagesList messagesList;
    LinearLayout llItems;
    ImageButton text_img_btn, gallery_img_btn, camera_img_btn, location_img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        if (getIntent() != null) {
//            mChatUserKey = getIntent().getStringExtra("userId");
//            mChatUserKey = "dfg123";
            mChatUserKey = "123456";
        }

        mMessageList = new ArrayList<>();
       /* mUserRef = FirebaseDatabase.getInstance().getReference().child(AppConstants.NODE_USERS);
        mUserRef.keepSynced(true);
        mConversationRef = FirebaseDatabase.getInstance().getReference().child(AppConstants.NODE_CONVERSATIONS);
        mConversationRef.keepSynced(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();*/
        mDefaultCameraModule = new DefaultCameraModule();
//        currentFirebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        currentFirebaseId = "XYZ123";
//        userDetails = Hawk.get(AppConstants.USER_DETAILS);
//        currentFirebaseId = "123456";
currentFirebaseId = "dfg123";
        tvName = findViewById(R.id.tvName);
        ivGoBack = findViewById(R.id.ivGoBack);
        input = findViewById(R.id.input);
        messagesList = findViewById(R.id.messagesList);
        llItems = findViewById(R.id.llItems);
        text_img_btn = findViewById(R.id.text_img_btn);
        gallery_img_btn = findViewById(R.id.gallery_img_btn);
        camera_img_btn = findViewById(R.id.camera_img_btn);
        location_img_btn = findViewById(R.id.location_img_btn);

        MessageHolders messageHolders = new MessageHolders().registerContentType(CONTENT_TYPE_LOCATION,
                IncomingLocationViewHolder.class, R.layout.item_custom_incoming_location_message,
                OutComingLocationViewHolder.class, R.layout.item_custom_outcoming_location_message,
                new MessageHolders.ContentChecker<MessagesTwo>() {
                    @Override
                    public boolean hasContentFor(MessagesTwo message, byte type) {

                        return type == CONTENT_TYPE_LOCATION && (message.getLocation() != null && message.location.placeAddress != null
                                && message.location.placeName != null);
                    }
                }


        );

        messageAdapter = new MessagesListAdapter<MessagesTwo>(currentFirebaseId, messageHolders, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {


//                Picasso.with(ChatActivity.this).load(url).resize(800, 700).centerCrop().into(imageView);

                    Glide.with(ChatActivity.this).load(url).dontAnimate().override(800,700).centerCrop().into(imageView);
//                Picasso.with(ChatActivity.this).load(url)
//                        .resize(800, 700).centerCrop()
//                        .placeholder(R.drawable.ic_tab_profile)
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .into(imageView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(ChatActivity.this).load(url).resize(800, 700).centerCrop().into(imageView);
//                            }
//                        });



            }
        });



        messageAdapter.setOnMessageClickListener(new MessagesListAdapter.OnMessageClickListener<MessagesTwo>() {
            @Override
            public void onMessageClick(MessagesTwo message) {
                switch (message.type) {
                    case "photo":

                        Intent lauchShowImage = new Intent(ChatActivity.this, ShowImageActivity.class);
                        lauchShowImage.putExtra("Image", message.image.url);
                        startActivity(lauchShowImage);
                        break;

                    case "location":

                        double latitude = message.location.lat;
                        double longitude = message.location.lng;
                        String label = message.location.placeName;
                        String uriBegin = "geo:$latitude,$longitude";
                        String query = message.location.placeAddress + "," + latitude + "," + longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Logger.d(uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;


                }
            }
        });
        /*mUserRef.child(mChatUserKey).child(AppConstants.NODE_CREDENTIALS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                tvName.setText(userModel.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                inputMessage(input);
                return true;
            }
        });

        input.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                llItems.setVisibility(View.VISIBLE);
                input.setVisibility(View.GONE);

            }
        });

        text_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llItems.setVisibility(View.GONE);
                input.setVisibility(View.VISIBLE);
            }
        });

        camera_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CAMERA_PERMISSION);
                    }
                    return;
                }


                startActivityForResult(mDefaultCameraModule.getCameraIntent(ChatActivity.this), CAPTURE_IMAGE);
            }
        });

        gallery_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ChatActivity.this)
                        .returnAfterFirst(true) // set whether pick or camera action should return immediate result or not. For pick image only work on single mode
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle(getString(R.string.msg_select_image)) // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .theme(R.style.ImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                        .enableLog(true) // disabling log
                        .start(SELECT_IMAGE); // start image picker activity with request code
            }
        });

        location_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_LOCATION_PERMISSION);
                    }
                    return;
                }

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ChatActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadMessages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE &&
                resultCode == Activity.RESULT_OK && data!=null) {


            List<Image> images = ImagePicker.getImages(data);
            File actualImage = new File(images.get(0).getPath());

            File compressedFile = null;
            try {
                compressedFile = new Compressor(this)
                        .setMaxWidth(560)
                        .setMaxHeight(480)
                        .setQuality(60)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(actualImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uriFile = Uri.fromFile(compressedFile);
            uploadImageMessage(uriFile);


        } else if (requestCode == CAPTURE_IMAGE &&
                resultCode == Activity.RESULT_OK) {


            mDefaultCameraModule.getImage(this, data, new OnImageReadyListener() {
                @Override
                public void onImageReady(List<Image> images) {
                    if (images != null) {
                        File actualImage = new File(images.get(0).getPath());

                        File compressedFile = null;
                        try {
                            compressedFile = new Compressor(ChatActivity.this)
                                    .setMaxWidth(560)
                                    .setMaxHeight(480)
                                    .setQuality(60)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(actualImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Uri uriFile = Uri.fromFile(compressedFile);
                        uploadImageMessage(uriFile);
                    }
                }
            });


        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            uploadLocationMessage(PlacePicker.getPlace(this, data));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE);

        } else {
            Toast.makeText(this, "Allow the permission to work Scroll effectively.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadLocationMessage(final Place place) {


       /* mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey).child(AppConstants.NODE_LOCATION).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mConversationId = dataSnapshot.getValue(String.class);
                    String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();

                    //Preparing message here
                    MessagesTwo message = new MessagesTwo();
                    message.id = message_push_id;

                    MessagesTwo.Location location = new MessagesTwo.Location(place.getLatLng().latitude, place.getLatLng().longitude, place.getName().toString(), place.getAddress().toString());

                    message.location = location;

                    message.content = place.getLatLng().latitude + ":" + place.getLatLng().longitude;
                    message.isRead = false;
                    message.fromID = currentFirebaseId;
                    message.toID = mChatUserKey;
                    message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                    message.type = "location";                         //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                   *//* Author author = new Author();
                    author.id = currentFirebaseId;  //TODO: QUERY TO DATABASE THEN MOVE THE CODE TO THAT CALLBACK
                    author.name = userDetails.getName();
                    author.avatar = userDetails.getProfilePicLink();
                    author.online = true;

                    message.author = author;*//*

                    HashMap<String, Object> valueMap = new HashMap<String, Object>();
                    valueMap.put(mConversationId + "/" + message_push_id, message);
                    mConversationRef.updateChildren(valueMap);
                } else {
                    final String conversation_push_id = mConversationRef.push().getKey();

                    mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey).child(AppConstants.NODE_LOCATION)
                            .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mUserRef.child(mChatUserKey).child(AppConstants.NODE_CONVERSATIONS).child(currentFirebaseId).child(AppConstants.NODE_LOCATION)
                                        .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();

                                            //Preparing message here
                                            MessagesTwo message = new MessagesTwo();
                                            message.id = message_push_id;

                                            MessagesTwo.Location location = new MessagesTwo.Location(place.getLatLng().latitude, place.getLatLng().longitude, place.getName().toString(), place.getAddress().toString());

                                            message.location = location;

                                            message.content = place.getLatLng().latitude + ":" + place.getLatLng().longitude;
                                            message.isRead = false;
                                            message.fromID = currentFirebaseId;
                                            message.toID = mChatUserKey;
                                            message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                                            message.type = "location";                         //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                                            Author author = new Author();
                                            author.id = currentFirebaseId;  //TODO: QUERY TO DATABASE THEN MOVE THE CODE TO THAT CALLBACK
                                            author.name = userDetails.getName();
                                            author.avatar = userDetails.getProfilePicLink();
                                            author.online = true;

                                            message.author = author;

                                            HashMap<String, Object> valueMap = new HashMap<String, Object>();
                                            valueMap.put(conversation_push_id + "/" + message_push_id, message);
                                            mConversationRef.updateChildren(valueMap);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }


    private void uploadImageMessage(Uri uriFile) {


        /*String child = UUID.randomUUID().toString();
        mStorageRef.child("messagePics")
                .child(child).putFile(uriFile)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> p0) {

                        if (p0.isSuccessful()) {

                            mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey).child(AppConstants.NODE_LOCATION).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        mConversationId = dataSnapshot.getValue(String.class);
                                        String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();


                                        //Preparing message here
                                        MessagesTwo message = new MessagesTwo();
                                        message.id = message_push_id;

                                       p0.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                 imageUri =uri;
                                            }
                                        });

                                        MessagesTwo.Image image = new MessagesTwo.Image(imageUri.toString());

                                        message.image = image;
                                        message.content = imageUri.toString();
                                        message.isRead = false;
                                        message.fromID = currentFirebaseId;
                                        message.toID = mChatUserKey;
                                        message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                                        message.type = "photo";                          //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                                        Author author = new Author();
                                        author.id = currentFirebaseId;  //TODO: QUERY TO DATABASE THEN MOVE THE CODE TO THAT CALLBACK
                                        author.name = userDetails.getName();
                                        author.avatar = userDetails.getProfilePicLink();
                                        author.online = true;

                                        message.author = author;

                                        HashMap<String, Object> valueMap = new HashMap<String, Object>();
                                        valueMap.put(mConversationId + "/" + message_push_id, message);
                                        mConversationRef.updateChildren(valueMap);

                                    } else {
                                        final String conversation_push_id = mConversationRef.push().getKey();

                                        mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey).child(AppConstants.NODE_LOCATION)
                                                .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mUserRef.child(mChatUserKey).child(AppConstants.NODE_CONVERSATIONS).child(currentFirebaseId).child(AppConstants.NODE_LOCATION)
                                                            .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //THREAD WITH CURRENT USER DIALOG EXIST ALREADY --------------------------------------------------------------------------------------------

                                                                String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();


                                                                //Preparing message here
                                                                MessagesTwo message = new MessagesTwo();
                                                                message.id = message_push_id;
                                                                p0.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        imageUri =uri;
                                                                    }
                                                                });

                                                                MessagesTwo.Image image = new MessagesTwo.Image(imageUri.toString());

                                                                message.image = image;
                                                                message.content = imageUri.toString();
                                                                message.isRead = false;
                                                                message.fromID = currentFirebaseId;
                                                                message.toID = mChatUserKey;
                                                                message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                                                                message.type = "photo";                          //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                                                                Author author = new Author();
                                                                author.id = currentFirebaseId;  //TODO: QUERY TO DATABASE THEN MOVE THE CODE TO THAT CALLBACK
                                                                author.name = userDetails.getName();
                                                                author.avatar = userDetails.getProfilePicLink();
                                                                author.online = true;

                                                                message.author = author;

                                                                HashMap<String, Object> valueMap = new HashMap<String, Object>();
                                                                valueMap.put(conversation_push_id + "/" + message_push_id, message);
                                                                mConversationRef.updateChildren(valueMap);
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });*/
    }


    private void inputMessage(final CharSequence input) {

       /* mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey)
                .child(AppConstants.NODE_LOCATION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mConversationId = dataSnapshot.getValue(String.class);
                    String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();

                    MessagesTwo message = new MessagesTwo();
                    message.id = message_push_id;
                    message.content = input.toString().trim();
                    message.isRead = false;
                    message.fromID = currentFirebaseId;
                    message.toID = mChatUserKey;
                    message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                    message.type = "text";                          //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                    *//*Author author = new Author();
                    author.id = currentFirebaseId;
                    author.name = userDetails.getName();
                    author.avatar = userDetails.getProfilePicLink();
                    author.online = true;
                    message.author = author;*//*

                    HashMap<String, Object> valueMap = new HashMap<String, Object>();
                    valueMap.put(mConversationId + "/" + message_push_id, message);
                    mConversationRef.updateChildren(valueMap);
                } else {
                    final String conversation_push_id = mConversationRef.push().getKey();

                    mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey).child(AppConstants.NODE_LOCATION)
                            .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mUserRef.child(mChatUserKey).child(AppConstants.NODE_CONVERSATIONS).child(currentFirebaseId).child(AppConstants.NODE_LOCATION)
                                        .setValue(conversation_push_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String message_push_id = mConversationRef.child(mChatUserKey).push().getKey();

                                            MessagesTwo message = new MessagesTwo();
                                            message.id = message_push_id;
                                            message.content = input.toString().trim();
                                            message.isRead = false;
                                            message.fromID = currentFirebaseId;
                                            message.toID = mChatUserKey;
                                            message.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
                                            message.type = "text";                          //TODO: LOGIC TO CHECK THE CONTENT OF MESSAGE

                                            Author author = new Author();
                                            author.id = currentFirebaseId;
                                            author.name = userDetails.getName();
                                            author.avatar = userDetails.getProfilePicLink();
                                            author.online = true;
                                            message.author = author;

                                            HashMap<String, Object> valueMap = new HashMap<String, Object>();
                                            valueMap.put(conversation_push_id + "/" + message_push_id, message);
                                            mConversationRef.updateChildren(valueMap);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    private void loadMessages() {

        tvName.setText("Guard Name");


        MessagesTwo messageSend = new MessagesTwo();
        messageSend.content = "Hello";
        messageSend.id = "dfsdh";
        messageSend.isRead = false;
        messageSend.fromID = currentFirebaseId;
        messageSend.toID = mChatUserKey;
        messageSend.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
        messageSend.type = "text";
        if (messageSend.author == null) {
            messageSend.author = new Author();
            messageSend.author.id = messageSend.fromID;
            messageSend.author.avatar = "https://firebasestorage.googleapis.com/v0/b/hollame-c9bd7.appspot.com/o/usersProfilePics%2FXR3I4jCpZ4?alt=media&token=c11f2967-3ba1-4bb0-98fb-d7db4f036167";
            messageSend.author.name = "iOS";
            messageSend.author.online = true;
        }


        MessagesTwo messageReceiver = new MessagesTwo();
        messageReceiver.content = "Hii";
        messageReceiver.id = "1234";
        messageReceiver.isRead = false;
        messageReceiver.fromID = currentFirebaseId;
        messageReceiver.toID = mChatUserKey;
        messageReceiver.timestamp = System.currentTimeMillis();  //TODO: SET FIREBASE SERVER TIMESTAMP
        messageReceiver.type = "text";
        if (messageReceiver.author == null) {
            messageReceiver.author = new Author();
            messageReceiver.author.id = messageReceiver.toID;
            messageReceiver.author.avatar = "https://firebasestorage.googleapis.com/v0/b/hollame-c9bd7.appspot.com/o/usersProfilePics%2FXR3I4jCpZ4?alt=media&token=c11f2967-3ba1-4bb0-98fb-d7db4f036167";
            messageReceiver.author.name = "iOS";
            messageReceiver.author.online = true;
        }

        messageAdapter.addToStart(messageSend, true);
        messageAdapter.addToStart(messageReceiver, true);
        messageAdapter.addToStart(messageSend, true);
        messageAdapter.addToStart(messageReceiver, true);
        messageAdapter.addToStart(messageSend, true);
        messageAdapter.addToStart(messageReceiver, true);
        messagesList.setAdapter(messageAdapter);

       /* mUserRef.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).child(mChatUserKey)
                .child(AppConstants.NODE_LOCATION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mConversationId = dataSnapshot.getValue(String.class);

                    mConversationRef.child(mConversationId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MessagesTwo message = dataSnapshot.getValue(MessagesTwo.class);
                            if (message.author == null) {
                                message.author = new Author();
                                message.author.id = message.fromID;
                                message.author.avatar = "https://firebasestorage.googleapis.com/v0/b/hollame-c9bd7.appspot.com/o/usersProfilePics%2FXR3I4jCpZ4?alt=media&token=c11f2967-3ba1-4bb0-98fb-d7db4f036167";
                                message.author.name = "iOS";
                                message.author.online = true;
                            }
                            if (message.type.equals("photo")) {
                                message.image = new MessagesTwo.Image();
                                message.image.url = message.content;
                            } else if (message.type.equals("location")) {
                                message.location = new MessagesTwo.Location();
                                String[] latlong = message.content.split(":");
                                message.location.lat = Double.parseDouble(latlong[0]);
                                message.location.lng = Double.parseDouble(latlong[1]);
                                message.location.placeName = "";
                                message.location.placeAddress = "";
                            }
                            messageAdapter.addToStart(message, true);
                            messagesList.setAdapter(messageAdapter);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        llItems.setVisibility(View.GONE);
        input.setVisibility(View.VISIBLE);
    }
}
