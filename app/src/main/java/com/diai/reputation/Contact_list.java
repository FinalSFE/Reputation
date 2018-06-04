package com.diai.reputation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Contact_list extends AppCompatActivity {
    ListView lv;
    TextView text;
    TextView text0;
    int shareNumber=5;
    int rateNumber=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if((rateNumber<=0)&&(shareNumber<=0)){
            Intent intent=new Intent(this,Profile.class);
        }

        setContentView(R.layout.activity_contact_list);
        lv = (ListView) findViewById(R.id.contactList);
        MyListAdapter listAdpter = new MyListAdapter(this);
        lv.setAdapter(listAdpter);
        text = (TextView)findViewById(R.id.shareNb);
        text0 = (TextView)findViewById(R.id.rateNb);



        Button finish=(Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , Home.class);
                startActivity(intent);
                onDestroy();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private class MyListAdapter extends BaseAdapter {

        Context context;
        ArrayList<Contact> contactList;
        LayoutInflater layoutInflater;

        public MyListAdapter(Context context) {
            //this.context = context;
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            contactList = new ArrayList<Contact>();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(new Contact(name, phone));
            }
            cursor.close();
            this.context = context;
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int i) {
            return contactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {


            final LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.contact, parent, false);
            //ImageView image=(ImageView)findViewById(R.id.profile_image);
            final TextView name = (TextView) row.findViewById(R.id.contactName);
            final TextView phoneNumber = (TextView) row.findViewById(R.id.contactPhone);
            Button share = (Button) row.findViewById(R.id.share);
            Button rate=(Button)row.findViewById(R.id.gRate);


            name.setText(contactList.get(position).name.toString());
            phoneNumber.setText(contactList.get(position).phoneNumber);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber.getText().toString())+"@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Install Reputation App");

                    startActivity(sendIntent);
                    shareNumber=shareNumber-1;
                    text.setText(Integer.toString(shareNumber));

                }
            });

            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),Profile.class);
                    rateNumber=rateNumber-1;
                    intent.putExtra("id",phoneNumber.getText().toString());
                    text0.setText(Integer.toString(rateNumber));
                    startActivityForResult(intent,10);
                }
            });
            return row;
        }

    }

    private class Contact {
        int image;
        String name;
        String phoneNumber;

        public Contact(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }

}

