package com.v5;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Login implements OfferAdapter.FirebaseDataListener {


    private FloatingActionButton Add, Info;
    private EditText mEditName;
    private EditText mEditDesc;
    private EditText mEditPrice;
    private EditText mEditLocation;
    private TextView ViewOffer, ViewInfo;
    private RecyclerView mRecyclerView;
    private OfferAdapter mAdapter;
    private ArrayList<Offer> offerList;
    private FirebaseAuth auth;
    private Button btnDatePicker, btnTimePicker;
    private EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;



    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        mRecyclerView = findViewById( R.id.recycler_view );
        mRecyclerView.setHasFixedSize( true );
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );


        FirebaseApp.initializeApp( this );

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference( "offer" );
        mDatabaseReference.child( "data_offer" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                offerList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = mDataSnapshot.getValue( Offer.class );
                    offer.setKey( mDataSnapshot.getKey() );
                    offerList.add( offer );
                }
                //set adapter RecyclerView
                mAdapter = new OfferAdapter( MainActivity.this, offerList );
                mRecyclerView.setAdapter( mAdapter );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText( MainActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG ).show();
            }

        } );

        //FAB (FloatingActionButton)
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        Add = findViewById( R.id.floating_button_add );
        if (currentUser.getEmail().equals( "admin@nylivspa.com" )) {
            Add.show();
            Add.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogAddOffer();

                }

            } );

        } else
            Add.hide();


        Info = findViewById( R.id.floating_button_info );

        Info.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogInfo();

            }

        } );

    }

    private void dialogInfo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Services we offer");
        View view = getLayoutInflater().inflate(R.layout.info, null);
        ViewInfo = view.findViewById( R.id.info);
        ViewInfo.setText( "-Wellness/sport-\n"+"\n"+
                "Relaxing and stress relieving the upper layers of muscle tissue,\n" +
                "good for blood circulation and detoxifying.\n"+ "\n"+
                "-Hot stone massage-\n" + "\n"+
                " Imagine 45 degrees hot lava stones specially shaped to caress your\n" +
                " body from head to toe in a relaxing and stress free way combined with\n" +
                " nourishing and moisturizing properties of vegetable oils.\n"+ "\n"+
                "-Coconut oil massage-\n" + "\n"+
                "The benefits of coconut oil are even greater in the cold weather,\n"+
                "combined with moisturising properties as well as amaizing hydratation,\n"+
                "the coconut oil is one of the few natural oils that are as good for the \n"+
                "body inside and outside.\n"+ "\n"+
                "-Hot chocolate butter massage-\n"+ "\n"+
                "If you would like to have smooth silky skin and want to\n"+
                "treat yourself with a relaxing full body massage and facial,\n"+
                "then you should try our new chocolate treat.45 minutes\n"+
                "includes full body massage, 75 minutes includes 55 min massage and 20 min facial"
                );
        builder.setView(view);



        //button save
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {


            }
        });

        Dialog dialog = builder.create();
        dialog.show();


    }


    //dialog ADD offer
    private void dialogAddOffer() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add an offer");
        View view = getLayoutInflater().inflate(R.layout.add_offer, null);

        btnDatePicker = view.findViewById(R.id.btn_date);
        txtDate = view.findViewById( R.id.in_date);
        btnTimePicker = view.findViewById(R.id.btn_time);
        txtTime = view.findViewById( R.id.in_time);
        mEditName = view.findViewById(R.id.offer_name);
        mEditDesc = view.findViewById(R.id.offer_desc);
        mEditPrice = view.findViewById(R.id.offer_price);
        mEditLocation = view.findViewById(R.id.offer_location);

        builder.setView(view);

            btnDatePicker.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogCalendar();

                }

            } );

        btnTimePicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTime();

            }

        } );



        //button save
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String offerName = mEditName.getText().toString();
                String offerDesc = mEditDesc.getText().toString();
                String offerDate = txtDate.getText().toString();
                String offerTime = txtTime.getText().toString();
                String offerPrice = mEditPrice.getText().toString();
                String offerLocation = mEditLocation.getText().toString();

                    submitOffer(new Offer(offerName, offerDesc, offerDate, offerTime, offerPrice, offerLocation));

            }
        });


        //button cancel
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();



    }

    private void dialogCalendar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calendar");
        View view = getLayoutInflater().inflate(R.layout.calendar, null);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

    private void dialogTime() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calendar");
        View view = getLayoutInflater().inflate(R.layout.time, null);


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    @Override
    public void onDataClick(final Offer offer, int position) {
        //action click
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "What do you want to do? " );

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser.getEmail().equals( "admin@nylivspa.com" )) {

            builder.setPositiveButton( "UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialogUpdateOffer( offer );
                }
            } );
            builder.setNegativeButton( "DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    deleteOffer( offer );
                }
            } );
            builder.setNeutralButton( "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            } );

        }

         else
             {

            builder.setNegativeButton( "BOOK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialogBookOffer( offer );
                }
            } );

        }
        Dialog dialog = builder.create();
        dialog.show();

    }


    //dialog UPDATE offer
    private void dialogUpdateOffer(final Offer offer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Offer");
        View view = getLayoutInflater().inflate(R.layout.edit_offer, null);

        btnDatePicker = view.findViewById(R.id.btn_date);
        txtDate = view.findViewById( R.id.in_date);
        btnTimePicker = view.findViewById(R.id.btn_time);
        txtTime = view.findViewById( R.id.in_time);
        mEditName = view.findViewById(R.id.offer_name);
        mEditDesc = view.findViewById(R.id.offer_desc);
        mEditPrice = view.findViewById(R.id.offer_price);
        mEditLocation = view.findViewById(R.id.offer_location);

        txtDate.setText(offer.getDate());
        txtTime.setText( offer.getTime() );
        mEditName.setText( offer.getName() );
        mEditDesc.setText(offer.getDesc());
        mEditPrice.setText(offer.getPrice());
        mEditLocation.setText(offer.getLocation());
        builder.setView(view);

        btnDatePicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCalendar();

            }

        } );

        btnTimePicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTime();

            }

        } );



        if (offer != null) {
            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    offer.setName(mEditName.getText().toString());
                    offer.setDesc(mEditDesc.getText().toString());
                    offer.setDate(txtDate.getText().toString());
                    offer.setTime(txtTime.getText().toString());
                    offer.setPrice(mEditPrice.getText().toString());
                    offer.setLocation(mEditLocation.getText().toString());
                    updateOffer(offer);
                }
            });
        }
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }


    //dialog BOOK offer
    private void dialogBookOffer(final Offer offer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Book Offer");
        View view = getLayoutInflater().inflate(R.layout.booking, null);


        ViewOffer = view.findViewById(R.id.offer_offer);

        ViewOffer.setText(
                "\n"+"\n"+"\n"+
                "Thank you for booking the offer:"+"\n"+"\n"+offer.getName()+" in "+offer.getLocation()+
                        "  location"+"\n"+"on the "+offer.getDate()+" at "+offer.getTime()+"\n"+"The price is "+offer.getPrice()+"kr"+"."+"\n"+"\n"+"\n"+
                        "You can pay now or by cash at the amenity."+"\n"+"\n"+"\n"+"\n"+"\n"+
                        "Rebooking or Cancellation by sms at: 71597953. Enjoy your therapy!" );

        builder.setView(view);


        if (offer != null) {
            builder.setPositiveButton("CONFIRM AND PAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(MainActivity.this, PayPal.class));
                    //deleteOffer( offer );


                }
            });
        }
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }



    // submit offer

    private void submitOffer(Offer offer) {
        mDatabaseReference.child("data_offer").push().setValue(offer).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Data has been saved!", Toast.LENGTH_LONG).show();

            }
        });

    }

     //update/edit offer

    private void updateOffer(Offer offer) {
        mDatabaseReference.child("data_offer").child(offer.getKey()).setValue(offer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Offer has been updated!", Toast.LENGTH_LONG).show();
            }
        });
    }


    // delete offer
    private void deleteOffer(Offer offer) {
        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();
        if (mDatabaseReference != null) {
            mDatabaseReference.child("data_offer").child(offer.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {


                @Override
                public void onSuccess(Void mVoid) {
                    if (currentUser.getEmail().equals( "admin@nylivspa.com" ))
                    {
                        Toast.makeText(MainActivity.this, "Offer has been deleted!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Offer has been booked!", Toast.LENGTH_LONG).show();}

                    }
            });
        }
    }
}
