package com.wk.guestpass.app.adapters;

import android.view.View;
import android.widget.TextView;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.wk.guestpass.app.Models.MessagesTwo;
import com.wk.guestpass.app.R;

import java.util.Date;

/**
 * Created by osx on 14/10/17.
 */

public class IncomingLocationViewHolder extends MessageHolders.IncomingTextMessageViewHolder<MessagesTwo> {

    private TextView tvTime;


    public IncomingLocationViewHolder(View itemView) {
        super(itemView);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);


    }

    @Override
    public void onBind(MessagesTwo message) {
        super.onBind(message);


        tvTime.setText(DateFormatter.format(new Date(message.timestamp), DateFormatter.Template.TIME));


    }


}
