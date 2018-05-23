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
 * Created by B50-50 on 31.03.2018..
 */

public class MojAdapter extends ArrayAdapter<Knjiga> {

        public MojAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public MojAdapter(Context context, int resource, ArrayList<Knjiga> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.izgled_liste_knijga_dva, null);
            }
            Knjiga p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.eNaziv);
                TextView tt2=(TextView) v.findViewById(R.id.eAutor);
                ImageView tt3= (ImageView)v.findViewById(R.id.eNaslovna);
                if (tt1 != null) {
                    tt1.setText(p.getAutori().get(0).getimeiPrezime());
                    tt2.setText(p.getNaziv());
                    tt3.setImageResource(R.drawable.slika);
                    //tt3.setImageBitmap(R.drawable.slika);
                }
            }

            return v;
        }

    }

