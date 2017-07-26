package in.bizzmark.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.bizzmark.test.adapter.CardAdapter;
import in.bizzmark.test.bo.CardBO;
import in.bizzmark.test.sqlite.DbHelper;
import in.bizzmark.test.wifi_direct.WiFiDirectActivity;

import static android.R.id.icon;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    TextView tvSavedCard;
    ListView listView;

    DbHelper dbHelper;
    SQLiteDatabase database;
    ArrayList<CardBO> cardList;

    CardBO cardBO;
    String cardNumber, expiry, cvv, name;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find-All-Ids
        findViewByAllIds();
    }

    // Find-All-Ids
    private void findViewByAllIds() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tvSavedCard = (TextView) findViewById(R.id.tv_saved_card);

        listView = (ListView) findViewById(R.id.listView);
        animation = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        listView.startAnimation(animation);
        // Get card details from SQLite-DB
        getCardDetailsFromSQLiteDB();
    }

    // Get card details from SQLite-DB
    private void getCardDetailsFromSQLiteDB() {
        cardList = new ArrayList<>();
        if (!cardList.isEmpty() || cardList != null) {
            tvSavedCard.setVisibility(View.VISIBLE);
        }else {
            tvSavedCard.setVisibility(View.GONE);
        }

        try {
            dbHelper = new DbHelper(this);
            database = dbHelper.getWritableDatabase();
            if (database != null) {
                String query = "SELECT * FROM CARD_DETAILS";
                Cursor cursor = database.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        cardNumber = cursor.getString(cursor.getColumnIndex("CARD_NUMBER"));
                        expiry = cursor.getString(cursor.getColumnIndex("EXPIRY_DATE"));
                        cvv = cursor.getString(cursor.getColumnIndex("CVV"));
                        name = cursor.getString(cursor.getColumnIndex("CARD_HOLDER_NAME"));


                        cardBO = new CardBO();
                        cardBO.setCardNumber(cardNumber);
                        cardBO.setExpiryDate(expiry);
                        cardBO.setCvv(cvv);
                        cardBO.setName(name);

                        cardList.add(cardBO);

                    } while (cursor.moveToNext());
                }
                cursor.close();
                database.close();

                CardAdapter adapter = new CardAdapter(this, R.layout.card_items, cardList);
                listView.setAdapter(adapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        //cardList.get(position);
                        return false;
                    }
                });
            }
        }catch (NullPointerException n){
            n.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get card details from SQLite-DB
        getCardDetailsFromSQLiteDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Get card details from SQLite-DB
        getCardDetailsFromSQLiteDB();
    }

    @Override
    public void onClick(View v) {
        if (v == fab){
            startActivity(new Intent(this, EnterCardDetailsActivity.class));
        }
    }

    public void setOnItemClickListener(CardBO item, View view) {
        //Toast.makeText(this, ""+item.getCardNumber()+item.getExpiryDate()+item.getCvv()+item.getName(), Toast.LENGTH_SHORT).show();
        try {

            CardBO cardBO1 = new CardBO();
            cardBO1.setCardNumber(item.getCardNumber());
            cardBO1.setExpiryDate(item.getExpiryDate());
            cardBO1.setCvv(item.getCvv());
            cardBO1.setName(item.getName());
            cardBO1.setStatus("success");

            // Create Object To Gson
            Gson gson = new Gson();
            String json = gson.toJson(cardBO1);
            Intent i = new Intent(this, WiFiDirectActivity.class);
           // i.putExtra("card_details", jsonEarn);
            startActivity(i);

            // save json object into sharedPreferences
            SharedPreferences.Editor editor = this.getSharedPreferences("CARD_DETAILS", Context.MODE_PRIVATE).edit();
            editor.putString("card_details", json);
            editor.commit();

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
