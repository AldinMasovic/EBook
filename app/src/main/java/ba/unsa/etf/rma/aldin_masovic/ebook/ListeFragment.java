package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;

import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.autori;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.podaciKategorija;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.siri;

/**
 * Created by B50-50 on 08.04.2018..
 */
public class ListeFragment extends Fragment {
    private OnItemClick oic;
    private ArrayList<Kategorija> kategorije;
    private ArrayList<Autor> autori2;
    ListView lv;
    EditText unosteksta;
    Button button_pretraga;
    Button button_kategorija;
    Button button_knjiga;
    Button dKategorije;
    Button dAutori;
    ArrayList<String> listaKategorija;
    ArrayList<String>listaAutora;



    public interface OnItemClick {
        public void onItemClicked(int pos);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.lista_kategorija_akt_2,container,false);
        lv = (ListView) view.findViewById(R.id.listaKategorija);
        unosteksta=(EditText)view.findViewById(R.id.tekstPretraga);
        button_pretraga=(Button)view.findViewById(R.id.dPretraga);
        button_kategorija=(Button)view.findViewById(R.id.dDodajKategoriju);
        button_knjiga=(Button)view.findViewById(R.id.dDodajKnjigu);
        dKategorije=(Button)view.findViewById(R.id.dKategorije);
        dAutori=(Button)view.findViewById(R.id.dAutori);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(getArguments().containsKey("ListaKategorija")) {
            //kategorije = getArguments().getParcelableArrayList("ListaKategorija");
            kategorije=podaciKategorija;
            listaKategorija=new ArrayList<String>();
            for(int i=0;i<kategorije.size();i++){
                listaKategorija.add(kategorije.get(i).ToString());
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listaKategorija);
            lv.setAdapter(adapter);
            button_kategorija.setEnabled(false);
            try {
                oic = (OnItemClick) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                    oic.onItemClicked(position);
                }
            });
        }
        else if(getArguments().containsKey("ListaAutora")){
            //autori=getArguments().getParcelableArrayList("ListaAutora");
            listaAutora=new ArrayList<String>();
            for(int i=0;i<autori.size();i++){
                listaAutora.add(autori.get(i).toString());
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listaAutora);
            lv.setAdapter(adapter);
            try {
                oic = (OnItemClick) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                    oic.onItemClicked(position);
                }
            });
            unosteksta.setVisibility(View.GONE);
            button_kategorija.setVisibility(View.GONE);
            button_pretraga.setVisibility(View.GONE);

        }
        else if(getArguments().getBoolean("Proslo")){
            Knjiga book=getArguments().getParcelable("knjiga");
            String odabir=getArguments().getString("kategorija");
            for(int i=0;i<podaciKategorija.size();i++){
                if(podaciKategorija.get(i).ToString().equals(odabir)){
                    podaciKategorija.get(i).GetKnjige().add(book);
                    break;
                }
            }
            boolean ubacen=false;
            for(int i=0;i<autori.size();i++){
                if(autori.get(i).getNaziv().equals(book.GetIme())){
                    autori.get(i).getKnjige().add(book);
                    ubacen=true;
                    break;
                }
            }
            if(!ubacen){autori.add(new Autor(book.GetIme()));autori.get(autori.size()-1).getKnjige().add(book);}
            kategorije=podaciKategorija;
             listaKategorija=new ArrayList<String>();
            for(int i=0;i<kategorije.size();i++){
                listaKategorija.add(kategorije.get(i).ToString());
            }

            final ArrayAdapter<String> adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listaKategorija);
            lv.setAdapter(adapter1);
            button_kategorija.setEnabled(false);
            try {
                oic = (OnItemClick) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                    oic.onItemClicked(position);
                }
            });

        }
        else if(!getArguments().getBoolean("Proslo")){
            //if(kategorije==null)return;
            kategorije=podaciKategorija;
            listaKategorija=new ArrayList<String>();
            for(int i=0;i<kategorije.size();i++){
                listaKategorija.add(kategorije.get(i).ToString());
            }

            final ArrayAdapter<String> adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listaKategorija);
            lv.setAdapter(adapter1);
            button_kategorija.setEnabled(false);
            try {
                oic = (OnItemClick) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
            }
            if(siri){

            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                    oic.onItemClicked(position);
                }
            });
        }
        button_kategorija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                podaciKategorija.add(new Kategorija(unosteksta.getText().toString()));
                kategorije=podaciKategorija;
                listaKategorija=new ArrayList<String>();
                for(int i=0;i<kategorije.size();i++){
                    listaKategorija.add(kategorije.get(i).ToString());
                }

                final ArrayAdapter<String> adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listaKategorija);
                lv.setAdapter(adapter1);
                unosteksta.setText("");
                button_kategorija.setEnabled(false);
            }
        });
        button_pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(unosteksta.getText().toString().length()==0)return;
                kategorije=podaciKategorija;
                listaKategorija=new ArrayList<String>();
                for(int i=0;i<kategorije.size();i++){
                    listaKategorija.add(kategorije.get(i).ToString());
                }

                final ArrayAdapter<String> adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listaKategorija);
                lv.setAdapter(adapter1);
                adapter1.getFilter().filter(unosteksta.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        Log.d("hh", count+"");
                        if(adapter1.isEmpty()){
                            button_kategorija.setEnabled(true);
                        }
                        else{
                            button_kategorija.setEnabled(false);
                        }
                    }
                });
            }
        });
        button_knjiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle argumenti = new Bundle();
                argumenti.putParcelableArrayList("kategorije", podaciKategorija);
                DodavanjeKnjigeFragment fd = new DodavanjeKnjigeFragment();
                fd.setArguments(argumenti);
                if(siri){
                getFragmentManager().beginTransaction()
                        .replace(R.id.ListeFragment2, fd)
                        //.addToBackStack(null)
                        .commit();}
                else
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();

            }
        });
        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaAutora=new ArrayList<String>();
                for(int i=0;i<autori.size();i++){
                    listaAutora.add(autori.get(i).toString());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listaAutora);
                lv.setAdapter(adapter);
                try {
                    oic = (OnItemClick) getActivity();
                } catch (ClassCastException e) {
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                        oic.onItemClicked(position);
                    }
                });

                button_pretraga.setVisibility(View.GONE);
                button_kategorija.setVisibility(View.GONE);
                unosteksta.setVisibility(View.GONE);
            }
        });
        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaKategorija=new ArrayList<String>();
                for(int i=0;i<podaciKategorija.size();i++){
                    listaKategorija.add(podaciKategorija.get(i).ToString());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listaKategorija);
                lv.setAdapter(adapter);
                try {
                    oic = (OnItemClick) getActivity();
                } catch (ClassCastException e) {
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati Onitemclick");
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent​, View view, int position, long id) {
                        oic.onItemClicked(position);
                    }
                });
                button_pretraga.setVisibility(View.VISIBLE);
                button_kategorija.setVisibility(View.VISIBLE);
                unosteksta.setVisibility(View.VISIBLE);
            }
        });
    }

}
