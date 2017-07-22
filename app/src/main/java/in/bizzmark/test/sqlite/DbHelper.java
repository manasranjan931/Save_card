package in.bizzmark.test.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Admin on 7/20/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ALL_CARD_DETAILS";
    public static final int VERSION = 1;

    public static final String TABLE_NAME = "CARD_DETAILS";

    public static final String SL_NUMBER_COL_1 = "SL_NUMBER";
    public static final String  CARD_NUMBER_COL_2 = "CARD_NUMBER";
    public static final String  EXPIRY_DATE_COL_3 = "EXPIRY_DATE";
    public static final String  CVV_COL_4 = "CVV";
    public static final String  CARD_HOLDER_NAME_COL_5 = "CARD_HOLDER_NAME";

    Context context;
    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
        public void onCreate(SQLiteDatabase db) {
        try {
            String query = "create table " + TABLE_NAME + " ( "+SL_NUMBER_COL_1+" INTEGER AUTO INCREMENT, "+CARD_NUMBER_COL_2+" TEXT, "+EXPIRY_DATE_COL_3+" TEXT, "+CVV_COL_4+" TEXT primary key, "+CARD_HOLDER_NAME_COL_5+" TEXT)";
            db.execSQL(query);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
