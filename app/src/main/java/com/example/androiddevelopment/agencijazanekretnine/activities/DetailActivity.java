package com.example.androiddevelopment.agencijazanekretnine.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.androiddevelopment.agencijazanekretnine.db.ORMLightHelper;
import com.example.androiddevelopment.agencijazanekretnine.db.model.Nekretnina;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 24.6.17..
 */

public class DetailActivity  extends AppCompatActivity{


    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Nekretnina n;

    private EditText identifikator;
    private EditText name;
    private EditText description;
    private EditText pictures;
    private EditText phoneNumber;
    private EditText kvadratura;
    private EditText numberOfRooms;
    private EditText adresa;
    private RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(ListActivity.NEKRETNINA_KEY);

        try {
            n = getDatabaseHelper().getNekretninaDao().queryForId(key);

            name = (EditText) findViewById(R.id.nekretnina_name);
            identifikator = (EditText) findViewById(R.id.nekretnina_identifikator);
            description = (EditText) findViewById(R.id.nekretnina_description);
            pictures = (EditText) findViewById(R.id.nekretnina_pictures);
            rating = (RatingBar) findViewById(R.id.nekretnina_rating);
            phoneNumber = (EditText) findViewById(R.id.nekretnina_phoneNumber);
            kvadratura = (EditText) findViewById(R.id.nekretnina_kvadratura);
            numberOfRooms = (EditText) findViewById(R.id.nekretnina_numberOfRooms);
            adresa = (EditText) findViewById(R.id.nekretnina_adresa);




            name.setText(n.getnName());
            description.setText(n.getnDescription());
            identifikator.setText(n.getnId());
            rating.setRating(n.getnScore());
            pictures.setText(n.getnPictures());
            phoneNumber.setText(n.getnPhoneNumber());
            adresa.setText(n.getnAdresa());
            kvadratura.setText(n.getnKvadratura());
            numberOfRooms.setText(n.getnNumberOfRooms());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.nekretnina);

        try {
            List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryBuilder()
                    .where()
                    .eq(Nekretnina.FIELD_NAME, n.getnId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Nekretnina n = (Nekretnina) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, n.getnName() + " " + n.getnAdresa() + " " + n.getnDescription() + " " + n.getnKvadratura() + " " + n.getnNumberOfRooms() + " " + n.getnPhoneNumber() + " " + n.getnId(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.actor_movies);

        if (listView != null) {
            ArrayAdapter<Nekretnina> adapter = (ArrayAdapter<Nekretnina>) listView.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryBuilder()
                            .where()
                            .eq(Nekretnina.FIELD_NAME, n.getnId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message) {
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Zavrsni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);

        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){

        boolean toast = prefs.getBoolean(NOTIF_STATUS, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_movie:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_nekretnina);

                Button add = (Button) dialog.findViewById(R.id.add_movie);
                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.nekretnina_name);
                        EditText adresa = (EditText) dialog.findViewById(R.id.nekretnina_adresa);
                        EditText description = (EditText) dialog.findViewById(R.id.nekretnina_description);
                        EditText kvadratura = (EditText) dialog.findViewById(R.id.nekretnina_kvadratura);
                        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.nekretnina_rating);
                        EditText id = (EditText) dialog.findViewById(R.id.nekretnina_identifikator);
                        EditText pictures = (EditText) dialog.findViewById(R.id.nekretnina_pictures);
                        EditText phoneNumber = (EditText) dialog.findViewById(R.id.nekretnina_phoneNumber);
                        EditText numberOfRooms = (EditText) dialog.findViewById(R.id.nekretnina_numberOfRooms);


                        float rating = ratingBar.getRating();

                        Nekretnina n = new Nekretnina();
                        n.setnName(name.getText().toString());
                        n.setnAdresa(adresa.getText().toString());
                        n.setnDescription(description.getText().toString());
                        n.setnKvadratura(kvadratura.getText().toString());
                        // n.setnId(id.getText().toString());
                        n.setnPictures(pictures.getText().toString());
                        n.setnPhoneNumber(phoneNumber.getText().toString());
                        n.setnNumberOfRooms(numberOfRooms.getText().toString());



                        try {
                            getDatabaseHelper().getNekretninaDao().create(n);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();

                        showMessage("New nekretnina added to nekretnine");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit_nekretnina:

                Nekretnina n = new Nekretnina();
                n.setnName(name.getText().toString());
                n.setnAdresa(adresa.getText().toString());
                n.setnDescription(description.getText().toString());
                n.setnKvadratura(kvadratura.getText().toString());
                // n.setnId(id.getText().toString());
                n.setnPictures(pictures.getText().toString());
                n.setnPhoneNumber(phoneNumber.getText().toString());
                n.setnNumberOfRooms(numberOfRooms.getText().toString());

                try {
                    getDatabaseHelper().getNekretninaDao().update(n);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.remove_nekretnina:

                try {
                    getDatabaseHelper().getNekretninaDao().delete(n);

                    showMessage("Nekretnina Deleted");

                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }


}
