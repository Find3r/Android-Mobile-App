package com.nansoft.find3r.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nansoft.find3r.R;
import com.nansoft.find3r.helpers.CircularImageView;

/**
 * Created by Carlos on 28/10/2015.
 */
public  class UserProfileViewHolder extends RecyclerView.ViewHolder {

    public  ImageView imgvUserProfileCover;
    public  CircularImageView imgvUserProfilePhoto;
    public  TextView txtvUserProfileAddress;


    public UserProfileViewHolder(View v, Context pcontext) {

        super(v);
        imgvUserProfileCover = (ImageView) v.findViewById(R.id.imgvUserProfileCover);
        imgvUserProfilePhoto  = (CircularImageView) v.findViewById(R.id.imgvUserProfilePhoto);
        txtvUserProfileAddress = (TextView) v.findViewById(R.id.txtvUserProfileName);

    }

    public ImageView getImageView() {
        return imgvUserProfileCover;
    }

    public void setImageView(ImageView ivExample) {
        this.imgvUserProfileCover = ivExample;
    }
}