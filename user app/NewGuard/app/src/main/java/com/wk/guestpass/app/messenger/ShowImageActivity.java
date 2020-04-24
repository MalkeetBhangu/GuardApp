package com.wk.guestpass.app.messenger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wk.guestpass.app.R;


public class ShowImageActivity extends AppCompatActivity {

    ImageView ivImage,ivGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ivImage = findViewById(R.id.ivImage);
        ivGoBack = findViewById(R.id.ivGoBack);

        if (getIntent()!=null){

            final String imageLink = getIntent().getStringExtra("Image");
            if (!imageLink.isEmpty()){

//                Picasso.with(this).load(imageLink).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.placeholder_profile).
//                        into(ivImage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(ShowImageActivity.this).load(imageLink).placeholder(R.drawable.placeholder_profile).into(ivImage);
//                            }
//                        });

                Glide.with(this).load(imageLink).dontAnimate().placeholder(R.drawable.placeholder_profile).into(ivImage);

            }

        } else{

            Toast.makeText(this,"something went wrong.. ", Toast.LENGTH_SHORT).show();
        }

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImageActivity.super.onBackPressed();
            }
        });
    }
}
