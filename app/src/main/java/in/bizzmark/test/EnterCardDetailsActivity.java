package in.bizzmark.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import in.bizzmark.test.sqlite.DbHelper;

/**
 * Created by Admin on 7/17/2017.
 */

public class EnterCardDetailsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageView ivBackArrow;
    EditText etCardNumber, etExpiryDate, etCVV, etCardHolderName;
    CheckBox cbSaveThisCard;
    Button btnAddAmount;

    String card_number, expiry_date, cvv ,name;

    DbHelper dbHelper;
    SQLiteDatabase db;

    NotificationManager notificationManager ;
    NotificationCompat.Builder builder;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_card_details);

        // Find-All-Ids
        findViewByAllIds();

    }

    // Find-All-Ids
    private void findViewByAllIds() {
        ivBackArrow = (ImageView) findViewById(R.id.iv_card_details_back);
        ivBackArrow.setOnClickListener(this);

        etCardNumber = (EditText) findViewById(R.id.et_card_number);
        etExpiryDate = (EditText) findViewById(R.id.et_expiry_date);
        etCVV = (EditText) findViewById(R.id.et_cvv);
        etCardHolderName = (EditText) findViewById(R.id.et_card_holder_name);

        cbSaveThisCard = (CheckBox) findViewById(R.id.cb_save_card);
        cbSaveThisCard.setOnCheckedChangeListener(this);

        btnAddAmount = (Button) findViewById(R.id.btn_add_amount);
        btnAddAmount.setOnClickListener(this);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBackArrow){
            finish();
        }else if (v == btnAddAmount){
            // Add-Amount
            addCardDetails();
        }
    }

    // Add-Amount
    private void addCardDetails() {
        card_number = etCardNumber.getText().toString().trim();
        expiry_date = etExpiryDate.getText().toString().trim();
        cvv = etCVV.getText().toString().trim();
        name = etCardHolderName.getText().toString().trim();

        try {
            if (!TextUtils.isEmpty(card_number)) {
                if (card_number.length() == 16) {
                    if (!TextUtils.isEmpty(expiry_date)) {
                        if (!TextUtils.isEmpty(cvv)) {
                            if (cvv.length() == 3) {
                                if (!TextUtils.isEmpty(name)) {
                                    if (cbSaveThisCard.isChecked()) {
                                        try {
                                            dbHelper = new DbHelper(this);
                                            db = dbHelper.getWritableDatabase();
                                            ContentValues cv = new ContentValues();
                                            cv.put(DbHelper.CARD_NUMBER_COL_2, card_number);
                                            cv.put(DbHelper.EXPIRY_DATE_COL_3, expiry_date);
                                            cv.put(DbHelper.CVV_COL_4, cvv);
                                            cv.put(DbHelper.CARD_HOLDER_NAME_COL_5, name);

                                            long result = db.insert(DbHelper.TABLE_NAME, null, cv);
                                            if (result != -1) {
                                                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                                // Send-a-notification
                                                getNotification(card_number, expiry_date, cvv);
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLiteException s) {
                                            s.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(this, "Click on save this card", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                etCVV.setError("CVV should be 3 digits");
                            }
                        }
                    }
                }else {
                    etCardNumber.setError("Card number should be 16 digits");
                }
            }
        }catch (NullPointerException n){
            n.printStackTrace();
        }
    }

    // Send-a-notification
    private void getNotification(String card_number, String expiry_date, String cvv) {
        try {
            builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setContentTitle("You saved");
            builder.setContentText("Card number: " + card_number + "\n" + "Expiry date :" + expiry_date + "\n" + "CVV " + cvv);
            builder.setAutoCancel(true);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);

            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            //builder.setDefaults();
            vibrator.vibrate(1);

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(100, builder.build());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            // do-after-checked
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String cardNumberValidate = "[0-9]+[ ]+[0-9]+[0-9]+";
       // etCardNumber.setO
    }
}
