package com.wk.guestpass.app.messenger;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;





import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.wk.guestpass.app.Models.Author;
import com.wk.guestpass.app.Models.DefaultDialog;
import com.wk.guestpass.app.Models.MessagesTwo;
import com.wk.guestpass.app.Models.UserModel;
import com.wk.guestpass.app.R;
import com.wk.guestpass.app.apputils.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;


public class UserMessengerFragment extends Fragment {

    DatabaseReference mDatabaseReference;
    DatabaseReference mUserReference;
    ArrayList<DefaultDialog> listDialog;
    DialogsListAdapter<DefaultDialog> dialogListAdapter;
    ArrayList<Author> arrayListUser;
    View view;
    DialogsList dialogsList;
    String currentFirebaseId;
    FragmentActivity parentActivity;
    KProgressHUD kProgressHUD;


    public UserMessengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_messenger, container, false);
        dialogsList = view.findViewById(R.id.dialogsList);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.keepSynced(true);
        mUserReference = FirebaseDatabase.getInstance().getReference().child(AppConstants.NODE_USERS);
        mUserReference.keepSynced(true);
//        currentFirebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        parentActivity = getActivity();
//        currentFirebaseId ="XYZ123";
        listDialog = new ArrayList<DefaultDialog>();

        view.findViewById(R.id.llParent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dialogListAdapter = new DialogsListAdapter<DefaultDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
//                Picasso.with(getActivity()).load(url);
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .placeholder(R.drawable.profile_placeholder)
//                        .into(imageView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(getActivity()).load(url)
//                                        .placeholder(R.drawable.profile_placeholder)
//                                        .into(imageView);
//                            }
//                        });

                Glide.with(getActivity()).load(url).dontAnimate().placeholder(R.drawable.profile_placeholder).into(imageView);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadThread();

        dialogListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<DefaultDialog>() {
            @Override
            public void onDialogClick(DefaultDialog dialog) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userId", dialog.getId());
                startActivity(intent);
            }
        });
    }

    private void loadThread() {

        dialogsList.setAdapter(dialogListAdapter, false);
        kProgressHUD = new KProgressHUD(parentActivity);
        kProgressHUD.setLabel(getString(R.string.please_wait)).setCancellable(false).show();

        mUserReference.child(currentFirebaseId).child(AppConstants.NODE_CONVERSATIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    HashMap<String, HashMap<String, String>> conversationModel = new HashMap<>();
                    conversationModel = (HashMap) dataSnapshot.getValue();

                    for (final String key :
                            conversationModel.keySet()) {
                        Logger.d(conversationModel.get(key).get(AppConstants.NODE_LOCATION));


                        final HashMap<String, HashMap<String, String>> finalConversationModel = conversationModel;
                        mUserReference.child(key).child(AppConstants.NODE_CREDENTIALS).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                final String conversationKey = finalConversationModel.get(key).get(AppConstants.NODE_LOCATION);

                                mDatabaseReference.child(AppConstants.NODE_CONVERSATIONS).child(conversationKey)
                                        .orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            if (kProgressHUD.isShowing())
                                                kProgressHUD.dismiss();


                                            for (final DataSnapshot p0 :
                                                    dataSnapshot.getChildren()) {
                                                MessagesTwo lastMessage = p0.getValue(MessagesTwo.class);
                                                if (lastMessage.type.equals("location"))
                                                    lastMessage.content = "Location";
                                                else if (lastMessage.type.equals("photo"))
                                                    lastMessage.content = "Photo";
                                                arrayListUser = new ArrayList<Author>();
                                                if (lastMessage.author == null) {
                                                    lastMessage.author = new Author();
                                                    lastMessage.author.id = lastMessage.fromID;
                                                    lastMessage.author.avatar = "https://firebasestorage.googleapis.com/v0/b/hollame-c9bd7.appspot.com/o/usersProfilePics%2FXR3I4jCpZ4?alt=media&token=c11f2967-3ba1-4bb0-98fb-d7db4f036167";
                                                    lastMessage.author.name = "iOS";
                                                    lastMessage.author.online = true;
                                                }
                                                arrayListUser.add(lastMessage.author);
                                                DefaultDialog dialog = new DefaultDialog(key, userModel.getProfilePicLink(), userModel.getName(), arrayListUser, lastMessage, 0);


                                                if (!listDialog.contains(dialog)) {

                                                    listDialog.add(dialog);
                                                    dialogListAdapter.setItems(listDialog);
                                                    dialogListAdapter.sortByLastMessageDate();
                                                    dialogListAdapter.notifyDataSetChanged();
                                                } else {
                                                    dialogListAdapter.updateItemById(dialog);
                                                    dialogListAdapter.sortByLastMessageDate();
                                                    dialogListAdapter.notifyDataSetChanged();
                                                }
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                } else {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
