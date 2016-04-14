package com.netanelsagi.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by netanel shilo on 18/01/2016.
 */
class MembersAdapter extends ArrayAdapter<MEMBER> {

    public MembersAdapter(Context context, MEMBER[] members) {
        super(context,R.layout.member_row, members);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater memberInflate = LayoutInflater.from(getContext());
        View customView = memberInflate.inflate(R.layout.member_row, parent, false);

        MEMBER singleMember = getItem(position);
        TextView memberEmail = (TextView)customView.findViewById(R.id.tvEMail);
        TextView memberPhone = (TextView)customView.findViewById(R.id.tvPhone);

        memberEmail.setText(singleMember.getEmailAddress());
        memberPhone.setText(singleMember.getPhoneNum());
        return customView;
    }
}
