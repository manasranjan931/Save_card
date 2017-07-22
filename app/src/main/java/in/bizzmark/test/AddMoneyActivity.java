package in.bizzmark.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Admin on 7/17/2017.
 */

public class AddMoneyActivity  extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBackArrow;
    EditText etAmount;
    Button btnAddMoney;

    String amount;
    int amnt = 0;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        // Find-All-Ids
        findViewByAllIds();
    }


    // Find-All-Ids
    private void findViewByAllIds() {
        ivBackArrow = (ImageView) findViewById(R.id.iv_add_back);
        ivBackArrow.setOnClickListener(this);

        etAmount = (EditText) findViewById(R.id.et_amount);
        btnAddMoney = (Button) findViewById(R.id.btn_add_money);
        btnAddMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBackArrow){
            finish();
        }else if (v == btnAddMoney){
            addMoney();
        }
    }

    private void addMoney() {
        try {
            if (!etAmount.equals("")) {
                amount = etAmount.getText().toString().trim();
                amnt = Integer.parseInt(amount);
                if (amnt > 10000 || amnt == 00000) {
                    Toast.makeText(this, "Please enter an amount between 1 to 10,000", Toast.LENGTH_SHORT).show();
                }else {
                    String money = "" + amnt;
                    startActivity(new Intent(this, EnterCardDetailsActivity.class).putExtra("money", money));
                }
            }
        }catch (NumberFormatException n){
            n.printStackTrace();
        }
    }
}
