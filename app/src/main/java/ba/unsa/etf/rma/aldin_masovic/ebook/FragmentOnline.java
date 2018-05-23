package ba.unsa.etf.rma.aldin_masovic.ebook;

//import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toast;

import java.util.ArrayList;

//import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.podaciKategorija;

public class FragmentOnline extends Fragment
        implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone,
        MojResultReceiver.Receiver{

    Spinner sKategorije;
    EditText tekstUpit;
    Spinner sRezultat;
    Button dRun;
    Button dAdd;
    Button dPovratak;
    private ArrayList<Kategorija> kategorije;
    private ArrayList<Knjiga>knjige;
    private boolean treba=false;
    //Receiver mReciever;
    MojResultReceiver mReciever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online, container, false);
        sKategorije = (Spinner) view.findViewById(R.id.sKategorije);
        tekstUpit = (EditText) view.findViewById(R.id.tekstUpit);
        sRezultat = (Spinner) view.findViewById(R.id.sRezultat);
        dRun = (Button) view.findViewById(R.id.dRun);
        dAdd = (Button) view.findViewById(R.id.dAdd);
        dPovratak = (Button) view.findViewById(R.id.dPovratak);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey("kategorije")) {
            kategorije = getArguments().getParcelableArrayList("kategorije");
            if (kategorije == null) {
                kategorije = new ArrayList<Kategorija>();
            }
            final ArrayList<String> spinerString = new ArrayList<String>();
            for (int i = 0; i < kategorije.size(); i++) {
                spinerString.add(kategorije.get(i).ToString());
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, spinerString);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sKategorije.setAdapter(adapter1);


//            TODO: izmjeniti ovo i na kraju izbrisati obavezno

//        if(getArguments().containsKey("ListaKategorija")) {
//            //kategorije = getArguments().getParcelableArrayList("ListaKategorija");
//            kategorije=podaciKategorija;
//            listaKategorija=new ArrayList<String>();
//            for(int i=0;i<kategorije.size();i++){
//                listaKategorija.add(kategorije.get(i).ToString());
//            }
//            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listaKategorija);
//            lv.setAdapter(adapter);
//            button_kategorija.setEnabled(false);
//            try {
//                oic = (ListeFragment.OnItemClick) getActivity();
//            } catch (ClassCastException e) {
//                throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
//            }
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
//                    oic.onItemClicked(position);
//                }
//            });
//        }

            dPovratak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                TODO: trebalo bi da radi
                    Bundle argumenti = new Bundle();
                    ListeFragment fd = new ListeFragment();
                    fd.setArguments(argumenti);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();
                }

            });

            dAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                TODO: dodati knjigu koja je označena na spineru sRezultat u kategoriju označenu u spineru sKategorije
                    Bundle argumenti = new Bundle();
                    argumenti.putBoolean("Proslo",true);
                    int odabirKnjige=sRezultat.getSelectedItemPosition();
                    argumenti.putParcelable("knjiga",knjige.get(odabirKnjige));
                    argumenti.putString("kategorija",sKategorije.getSelectedItem().toString());
                    ListeFragment fd = new ListeFragment();
                    fd.setArguments(argumenti);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();

                }
            });
            dRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                TODO: Ako je u tekstUpit upisana jedna riječ pozovite DohvatiKnjige sa datom riječi

                    String editText=tekstUpit.getText().toString();
                    String [] viseKnjiga=editText.split(";");
                    String [] zaAutore=editText.split("autor:");
                    String [] zaKorisnike=editText.split("korisnik:");
                    treba=true;
                    if(zaAutore.length==2){

                        new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)
                                    FragmentOnline.this).execute(zaAutore[1]);
                    }
                    else if(zaKorisnike.length==2){
                            Intent intent=new Intent(Intent.ACTION_SYNC,null,getActivity(),KnjigePoznanika.class);

                            mReciever=new MojResultReceiver(new Handler());
                            mReciever.setReceiver(FragmentOnline.this);

                            intent.putExtra("idKorisnika",zaKorisnike[1]);
                            intent.putExtra("receiver",mReciever);

                            getActivity().startService(intent);
                    }
                    else if(viseKnjiga.length==1){
                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)
                                FragmentOnline.this).execute(tekstUpit.getText().toString());
                    }
                    else{
                        for(int i=0;i<viseKnjiga.length;i++){
                            new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)
                                    FragmentOnline.this).execute(viseKnjiga[i]);
                        }
                    }

                }

            });


        }

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> knjige1) {
        if(treba) {
            knjige = new ArrayList<Knjiga>();
        }
        treba=false;
        knjige.addAll(knjige1);

        final ArrayList<String> spinerString = new ArrayList<String>();
        for (int i = 0; i < knjige.size(); i++) {
            spinerString.add(knjige.get(i).getNaziv());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, spinerString);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapter1);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige1) {
        this.knjige=knjige1;
        if (knjige== null) {
            knjige= new ArrayList<Knjiga>();
        }
        final ArrayList<String> spinerString = new ArrayList<String>();
        for (int i = 0; i < knjige.size(); i++) {
            spinerString.add(knjige.get(i).getNaziv());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, spinerString);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapter1);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case KnjigePoznanika.STATUS_START:
                Toast.makeText(getActivity(),"Pokrenuto je",Toast.LENGTH_SHORT).show();
                break;
            case KnjigePoznanika.STATUS_ERROR:
                String error=resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                break;
            case KnjigePoznanika.STATUS_FINISH:
                knjige=resultData.getParcelableArrayList("knjige");
                //popuni spiner
                final ArrayList<String> spinerString = new ArrayList<String>();
                for (int i = 0; i < knjige.size(); i++) {
                    spinerString.add(knjige.get(i).getNaziv());
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, spinerString);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sRezultat.setAdapter(adapter1);


                break;
        }
    }
}