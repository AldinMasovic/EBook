package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileDescriptor;

//import static ba.unsa.etf.rma.aldin_masovic.ebook.DodavanjeKnjigeAkt.xxx;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.siri;

/**
 * Created by B50-50 on 08.04.2018..
 */

public class DodavanjeKnjigeFragment extends Fragment {
    private ArrayList<Kategorija> kategorije;
    private Autor autor;
    public static Bitmap xxx=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dodavanje_knjige,container,false);
/*
        View iv= inflater.inflate(R.layout.dodavanje_knjige, container, false);
        if(getArguments()!=null && getArguments().containsKey("kategorija")){
            kategorije=getArguments().getParcelableArrayList("kategorija");
            final TextView ime = (TextView) iv.findViewById(R.id.imeAutora);
            final TextView naziv = (TextView) iv.findViewById(R.id.nazivKnjige);
            final Spinner odabir=(Spinner)iv.findViewById(R.id.sKategorijaKnjige);
            ArrayList<String>spinerString=new ArrayList<String>();
            for(int i=0;i<kategorije.size();i++){
                spinerString.add(kategorije.get(i).ToString());}
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(null,android.R.layout.simple_dropdown_item_1line,spinerString);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            odabir.setAdapter(adapter1);
//Postavljanje i ostalih vrijednosti na isti način
            //ime.setText("mrki");
            //autor.setText("joke");
            //ime.setText(knjige.get(0).GetIme());
            //autor.setText(knjige.get(0).GetNaziv());
        }
        return iv;*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("kategorije")) {
            kategorije = getArguments().getParcelableArrayList("kategorije");
            if(kategorije==null){
                kategorije=new ArrayList<Kategorija>();
            }
            final TextView ime = (TextView) getView().findViewById(R.id.imeAutora);
            final TextView naziv = (TextView) getView().findViewById(R.id.nazivKnjige);
            final Spinner odabir=(Spinner)getView().findViewById(R.id.sKategorijaKnjige);
            final Button dUpisiKnjigu=(Button) getView().findViewById(R.id.dUpisiKnjigu);
            final Button dPonisti=(Button) getView().findViewById(R.id.dPonisti);
            final Button dNadjiSliku=(Button)getView().findViewById(R.id.dNadjiSliku);
            final ArrayList<String>spinerString=new ArrayList<String>();
            for(int i=0;i<kategorije.size();i++){
                spinerString.add(kategorije.get(i).ToString());}
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getView().getContext(),android.R.layout.simple_dropdown_item_1line,spinerString);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            odabir.setAdapter(adapter1);


            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=ime.getText().toString();
                    String nazif=naziv.getText().toString();

                    Bundle argumenti = new Bundle();
                    //argumenti.putParcelableArrayList("knjiga", podaciKategorija);
                    argumenti.putBoolean("Proslo",true);
                    argumenti.putParcelable("knjiga",new Knjiga(name,nazif,xxx));
                    xxx=null;
                    //dodati xxx=null
                    argumenti.putString("kategorija",odabir.getSelectedItem().toString());
                    ListeFragment fd = new ListeFragment();
                    fd.setArguments(argumenti);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();
                }
            });
            dPonisti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle argumenti = new Bundle();
                    //argumenti.putParcelableArrayList("knjiga", podaciKategorija);
                    argumenti.putBoolean("Proslo",false);
                    ListeFragment fd = new ListeFragment();
                    fd.setArguments(argumenti);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();
                    if(siri){
                        KnjigeFragment fu=new KnjigeFragment();
                        getFragmentManager().beginTransaction().replace(R.id.ListeFragment2,fu).commit();
                    }
                }
            });
            dNadjiSliku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent (Intent.ACTION_GET_CONTENT);
                    Uri data = Uri.fromFile(Environment.getExternalStorageDirectory());
                    String type = "image/*";
                    intent.setDataAndType(data, type);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        //   //startActivityForResult(intent, 0);
                        startActivityForResult(Intent.createChooser(intent, "Odaberite fotografiju"), 1);
                    }
                }
            });


        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if(data == null)
                return;

            final TextView nazivKnjige = (TextView) getView().findViewById(R.id.nazivKnjige);
            String nazivSlike = nazivKnjige.getText().toString();
            if(nazivSlike.length() == 0)
                nazivSlike = "...";
            FileOutputStream outputStream;
            outputStream = getContext().openFileOutput(nazivSlike, Context.MODE_PRIVATE);
            getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            ImageView slika = (ImageView)getView().findViewById(R.id.naslovnaStr);
            Bitmap bm = null;
            while(bm == null)
                bm = BitmapFactory.decodeStream(getContext().openFileInput(nazivSlike));
            slika.setImageBitmap(bm);
            //slika.setRotation(90);
            xxx=bm;

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }




    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if(data == null)
                return;

            final TextView nazivKnjige = (TextView) getView().findViewById(R.id.nazivKnjige);
            String nazivSlike = nazivKnjige.getText().toString();
            if(nazivSlike.length() == 0)
                nazivSlike = "...";
            FileOutputStream outputStream;
            outputStream = openFileOutput(nazivSlike, Context.MODE_PRIVATE);
            getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            ImageView slika = (ImageView)getView().findViewById(R.id.naslovnaStr);
            Bitmap bm = null;
            while(bm == null)
                bm = BitmapFactory.decodeStream(openFileInput(nazivSlike));
            slika.setImageBitmap(bm);
            xxx=bm;

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/
}

//TODO: poziv ovog na button klik
// new SearchArtist((SearchArtist.OnMuzicarSearchDone)
//FragmentLista.this).execute(ime_koje_trazim);