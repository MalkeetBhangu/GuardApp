package com.wk.guestpass.guard.adapter;

import android.view.View;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.wk.guestpass.guard.R;
import com.wk.guestpass.guard.chatModels.MessagesTwo;

import java.util.Date;

/**
 * Created by osx on 14/10/17.
 */

public class OutComingLocationViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<MessagesTwo> {

    private TextView tvTime;

    public OutComingLocationViewHolder(View itemView) {
        super(itemView);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
    }

    @Override
    public void onBind(MessagesTwo message) {
        super.onBind(message);

        tvTime.setText(DateFormatter.format(new Date(message.timestamp), DateFormatter.Template.TIME));
    }


}
