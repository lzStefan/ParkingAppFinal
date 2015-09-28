package com.example.martinlamby.parking;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//Implementation of the share car function with  contact list from CustomContactListAdapter

public class ShareCarPositionActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private CustomContactListAdapter customContactListAdapter;
    private Contact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_car_position);

        contacts = new ArrayList<>();

        retrieveEmailContacts();

        //init ContactList
        ListView contactsList = (ListView) findViewById(R.id.contactsListView);
        customContactListAdapter = new CustomContactListAdapter(getApplicationContext(),R.layout.contacts_list_item, contacts, contactsList);
        contactsList.setAdapter(customContactListAdapter);
        customContactListAdapter.notifyDataSetChanged();

        //init SendButton
        Button sendButton = (Button) findViewById(R.id.sendCarPositionButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLocationTask shareLocationTask = new ShareLocationTask();
                shareLocationTask.execute();
            }
        });
    }
    //task that starts sendEmail after last parked car location has been successfully retrieved
    public class ShareLocationTask extends AsyncTask<Void,Void,ParkedCarLocation>{

        @Override
        protected ParkedCarLocation doInBackground(Void... params) {
            return ParseController.getLastParkedCarLocation();
        }

        @Override
        protected void onPostExecute(ParkedCarLocation parkedCarLocation) {
            super.onPostExecute(parkedCarLocation);
            if(parkedCarLocation == null){
                SignUpActivity.showErrorToast(getApplicationContext(), getString(R.string.car_not_parked));
            }else {
                sendEmail(parkedCarLocation);
            }
        }
    }

    //retrieve all Contacts that have an eMail address as well as their type and add them to the contacts ArrayList
    //if a Contact has multiple eMail addresses both will be retrieved separately
    public void retrieveEmailContacts(){
        Uri uriCoarse = ContactsContract.Contacts.CONTENT_URI;
        String projectionCoarse[] = {ContactsContract.Contacts.LOOKUP_KEY};
        Cursor contactCursorCoarse = getContentResolver().query(uriCoarse,projectionCoarse,null,null,null);

        if(contactCursorCoarse.getCount()>0){
            while(contactCursorCoarse.moveToNext()){
                String id = contactCursorCoarse.getString(contactCursorCoarse.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uriFine = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String projectionFine[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE};
                String filterFine = ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + " = ?";

                Cursor contactCursorFine = getContentResolver().query(uriFine, projectionFine, filterFine, new String[]{id}, null);

                while (contactCursorFine.moveToNext()){
                    String name=contactCursorFine.getString(contactCursorFine.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if(!name.contains("@")) {
                        String email = contactCursorFine.getString(contactCursorFine.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        int emailType = Integer.valueOf(contactCursorFine.getString(contactCursorFine.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
                        contacts.add(new Contact(name, email, parseEmailType(emailType)));
                    }
                }
                contactCursorFine.close();
            }
            contactCursorCoarse.close();
        }
        }

    //converts eMail type into readable String only if type is meaningful (Exception: OTHER)
    public String parseEmailType(int type){
        switch (type) {
            case 1:
                return "HOME";
            case 2:
                return "WORK";
            case 4:
                return "MOBILE";

            default:
                return "";
        }
    }

    //send Email to Contact previously selected by the User
    public void sendEmail(ParkedCarLocation parkedCarLocation) {
        selectedContact = customContactListAdapter.getSelectedContact();

        if (selectedContact != null) {
            //TODO: Hier stimmt wieder wahrscheinlich etwas mit den Positions Angaben nicht
            String emailBody = "Here is my car \n \nhttp://maps.google.com?q" + parkedCarLocation.getLatitude() + "," + parkedCarLocation.getLongitude() + "&z=12";

            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                intent.setData(Uri.parse("mailto:" + selectedContact.getEmailAddress()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } catch (Exception e) {
                SignUpActivity.showErrorToast(getApplicationContext(),e.getMessage());
                System.out.println("Error:  " + e.getMessage());
            }
        } else {
            Toast noContactSelected = Toast.makeText(getApplicationContext(), R.string.no_contact_selected, Toast.LENGTH_SHORT);
            noContactSelected.show();
        }
    }
}
