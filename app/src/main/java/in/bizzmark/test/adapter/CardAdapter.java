package in.bizzmark.test.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.bizzmark.test.MainActivity;
import in.bizzmark.test.R;
import in.bizzmark.test.bo.CardBO;

/**
 * Created by Admin on 7/20/2017.
 */

public class CardAdapter extends ArrayAdapter<CardBO> {

    MainActivity activity;
    ArrayList<CardBO> cardList;

    public CardAdapter(MainActivity activity, int resource, ArrayList<CardBO> cardList) {
        super(activity, resource, cardList);
        this.activity = activity;
        this.cardList = cardList;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.card_items, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        CardBO cardBO = cardList.get(position);
        holder.tvCardNumber.setText(cardBO.getCardNumber());
        holder.tvCardExpry.setText(cardBO.getExpiryDate());

        convertView.setOnClickListener(onClickListener(position));

        return convertView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setOnItemClickListener(getItem(position), v);
            }
        };
    }

    private class ViewHolder {
        private TextView tvCardNumber, tvCardExpry;

        public ViewHolder(View v) {

            tvCardNumber = (TextView) v.findViewById(R.id.tv_card_number);
            tvCardExpry = (TextView) v.findViewById(R.id.tv_expry_date);
        }
    }
}
