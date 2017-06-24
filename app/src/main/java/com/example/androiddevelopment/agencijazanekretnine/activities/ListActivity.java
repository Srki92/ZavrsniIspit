package com.example.androiddevelopment.agencijazanekretnine.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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
import com.example.androiddevelopment.agencijazanekretnine.dialogs.AboutDialog;
import com.example.androiddevelopment.agencijazanekretnine.preferences.Preferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 24.6.17..
 */

public class ListActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;

    public static String NEKRETNINA_KEY = "NEKRETNINA_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.glumci_list);

        try {
            List<Nekretnina> list = getDatabaseHelper().getNekretninarDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Nekretnina p = (Nekretnina) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra(NEKRETNINA_KEY, p.getnId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.glumci_list);

        if (listView != null) {
            ArrayAdapter<Nekretnina> adapter = (ArrayAdapter<Nekretnina>) listView.getAdapter();

            if (adapter != null)
            {
                try {
                    adapter.clear();
                    List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryForAll();

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
        NotificationCompat.Builder mBuilder = new  NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Zavrsni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_new_actor:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_actor_layout);

                Button add = (Button) dialog.findViewById(R.id.add_actor);
                add.setOnClickListener(new View.OnClickListener() {
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

                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast) {
                                Toast.makeText(ListActivity.this, "Added new actor", Toast.LENGTH_SHORT).show();
                            }

                            if (status) {
                                showStatusMesage("Added new Actor");

                                refresh();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.about:
                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;

            case R.id.preferences:
                startActivity(new Intent(ListActivity.this, Preferences.class));
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
