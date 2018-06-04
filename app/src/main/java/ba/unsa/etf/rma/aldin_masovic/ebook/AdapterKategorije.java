package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by B50-50 on 08.04.2018..
 */
 public class AdapterKategorije extends ArrayAdapter<Kategorija> {

        public AdapterKategorije(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public AdapterKategorije(Context context, int resource, ArrayList<Kategorija> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.jedan_text, null);
            }
            Kategorija p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.teksticsam);

                if (tt1 != null) {
                    tt1.setText(p.ToString());
                }
            }

            return v;
        }

    }
