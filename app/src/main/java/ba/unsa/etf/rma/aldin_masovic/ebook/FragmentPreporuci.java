package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class FragmentPreporuci extends Fragment {

    private Knjiga knjiga=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View iv= inflater.inflate(R.layout.fragment_preporuci, container, false);
        return iv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey("knjiga")){
            knjiga=getArguments().getParcelable("knjiga");
        }
        ArrayList<Kontakt> kontakti=new ArrayList<Kontakt>();
        kontakti=getContacts();
        ArrayList<String>mailovi=new ArrayList<String>();
        for(int i=0;i<kontakti.size();i++){
            mailovi.add(kontakti.get(i).getEmail());
        }
        //pronalazak widgeta

        final TextView eNaziv=(TextView)getView().findViewById(R.id.eNaziv);
        final TextView eAutor=(TextView) getView().findViewById(R.id.eAutor);
        TextView eOpis=(TextView)getView().findViewById(R.id.eOpis);
        TextView eDatumObjavljivanja=(TextView)getView().findViewById(R.id.eDatumObjavljivanja);
        TextView eBrojStranica=(TextView)getView().findViewById(R.id.eBrojStranica);
        ImageView eNaslovna= (ImageView)getView().findViewById(R.id.eNaslovna);
        Button dPosalji=(Button)getView().findViewById(R.id.dPosalji);
        final Spinner sKontakti=(Spinner)getView().findViewById(R.id.sKontakti);

        //postavljanje vrijednosti

        eNaziv.setText(knjiga.getNaziv());
        eAutor.setText(knjiga.getAutori().get(0).getimeiPrezime());
        eOpis.setText(knjiga.getOpis());
        eDatumObjavljivanja.setText("Datum objavljivanja: "+knjiga.getDatumObjavljivanja());
        eBrojStranica.setText("Broj stranica: "+knjiga.getBrojStranica());

        //postavljanje slike

        Uri uri=null;
        try {
            uri = Uri.parse(knjiga.getSlika().toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(uri!=null)
            Picasso.with(getContext()).load(uri).into(eNaslovna);
        else
            eNaslovna.setImageResource(R.drawable.slika);

        //zavrseno postavljanje slike

        //postavljanje spinera kontakta

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mailovi);
        sKontakti.setAdapter(adapter);

        //zavrseno postavljanje spinera kontakta

        final ArrayList<Kontakt> finalKontakti = kontakti;
        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] TO={sKontakti.getSelectedItem().toString()};
                String [] CC={""};
                int index=sKontakti.getSelectedItemPosition();
                String text="Zdravo "+ finalKontakti.get(index).getIme()+",\nPročitaj knjigu "
                        +eNaziv.getText().toString()+" od "+eAutor.getText().toString()+"!";
                Intent emailIntent=new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
                emailIntent.putExtra(Intent.EXTRA_CC,CC);

                //spiner seleted

                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Preporuka knjige");//"Email subjekta");
                emailIntent.putExtra(Intent.EXTRA_TEXT,text);
                try{
                    startActivity(Intent.createChooser(emailIntent,"Send mail..."));
                    //finish();

                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getActivity(),"Kupi novi mobitel :D",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private ArrayList<Kontakt> getContacts() {

        ContentResolver cr = getActivity().getContentResolver();

        Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI, new String[] { ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                null,null,ContactsContract.Contacts.DISPLAY_NAME);

        ArrayList<Kontakt>contact=new ArrayList<Kontakt>();
        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {

                String mimeType = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE));

                if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    String mail = (cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                    String ime = (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    contact.add(new Kontakt(ime, mail));
                }
            }
        }

        cur.close();

        return contact;
    }
}
