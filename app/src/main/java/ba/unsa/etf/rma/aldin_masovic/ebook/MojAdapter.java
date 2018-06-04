package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by B50-50 on 31.03.2018..
 */

public class MojAdapter extends ArrayAdapter<Knjiga> {
    private OnItemClick2 oic;
    public interface OnItemClick2 {
        public void onItemClicked2(Knjiga knjiga);
    }
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
                TextView eNaziv = (TextView) v.findViewById(R.id.eNaziv);
                TextView eAutor=(TextView) v.findViewById(R.id.eAutor);
                TextView eOpis=(TextView)v.findViewById(R.id.eOpis);
                TextView eDatumObjavljivanja=(TextView)v.findViewById(R.id.eDatumObjavljivanja);
                TextView eBrojStranica=(TextView)v.findViewById(R.id.eBrojStranica);
                ImageView eNaslovna= (ImageView)v.findViewById(R.id.eNaslovna);
                Button dPreporuci=(Button)v.findViewById(R.id.dPreporuci);
                if (eNaziv != null) {
                    eNaziv.setText("Naziv autora: "+p.getAutori().get(0).getimeiPrezime());
                    eAutor.setText("Naziv knjige: "+p.getNaziv());
                    eOpis.setText("Opis: "+p.getOpis());
                    eDatumObjavljivanja.setText("Datum: "+p.getDatumObjavljivanja());
                    eBrojStranica.setText("Broj stranica: "+p.getBrojStranica());
                    Uri uri=null;
                    try {
                         uri = Uri.parse(p.getSlika().toURI().toString());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    if(uri!=null)
                    Picasso.with(getContext()).load(uri).into(eNaslovna);
                    else
                    eNaslovna.setImageResource(R.drawable.slika);
                    //tt3.setImageBitmap(R.drawable.slika);
                }
                try {
                    oic = (MojAdapter.OnItemClick2) getContext();//getActivity();
                } catch (ClassCastException e) {
                    throw new ClassCastException("Treba implementirati Onitemclick");
                }
                final Knjiga knjiga=p;
                dPreporuci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oic.onItemClicked2(knjiga);
                    }
                });
            }

            return v;
        }

    }

