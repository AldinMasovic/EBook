package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by B50-50 on 08.04.2018..
 */

public class AdapterAutori extends ArrayAdapter<Autor> {
    public AdapterAutori(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterAutori(Context context, int resource, ArrayList<Autor> items) {
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
        Autor p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.teksticsam);

            if (tt1 != null) {
                tt1.setText(p.toString());
            }
        }

        return v;
    }
}
