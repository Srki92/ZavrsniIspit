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
    private EditText kvadraturom;
    private EditText numberOfRooms;
    private EditText adresa;
    private RatingBar score;


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
            a = getDatabaseHelper().getActorDao().queryForId(key);

            name = (EditText) findViewById(R.id.actor_name);
            bio = (EditText) findViewById(R.id.actor_biography);
            birth = (EditText) findViewById(R.id.actor_birth);
            rating = (RatingBar) findViewById(R.id.actor_rating);

            name.setText(a.getmName());
            bio.setText(a.getmBiography());
            birth.setText(a.getmBirth());
            rating.setRating(a.getmScore());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.actor_movies);

        try {
            List<Movie> list = getDatabaseHelper().getMovieDao().queryBuilder()
                    .where()
                    .eq(Movie.FIELD_NAME_USER, a.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie m = (Movie) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getmName() + " " + m.getmGenre() + " " + m.getmYear(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.actor_movies);

        if (listView != null) {
            ArrayAdapter<Movie> adapter = (ArrayAdapter<Movie>) listView.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<Movie> list = getDatabaseHelper().getMovieDao().queryBuilder()
                            .where()
                            .eq(Movie.FIELD_NAME_USER, a.getmId())
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
        //provera podesenja
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
                dialog.setContentView(R.layout.add_movie);

                Button add = (Button) dialog.findViewById(R.id.add_movie);
                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.movie_name);
                        EditText genre = (EditText) dialog.findViewById(R.id.movie_genre);
                        EditText year = (EditText) dialog.findViewById(R.id.movie_year);

                        Movie m = new Movie();
                        m.setmName(name.getText().toString());
                        m.setmGenre(genre.getText().toString());
                        m.setmYear(year.getText().toString());
                        m.setmUser(a);

                        try {
                            getDatabaseHelper().getMovieDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();

                        showMessage("New movie added to actor");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit_movie:

                a.setmName(name.getText().toString());
                a.setmBirth(birth.getText().toString());
                a.setmBiography(bio.getText().toString());
                a.setmScore(rating.getRating());

                try {
                    getDatabaseHelper().getActorDao().update(a);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.remove_movie:

                try {
                    getDatabaseHelper().getActorDao().delete(a);

                    showMessage("Actor Deleted");

                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public OrmLiteHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, OrmLiteHelper.class);
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
