package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTORSTVO_IDAUTORA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTORSTVO_IDKNJIGE;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTOR_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KATEGORIJA_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KATEGORIJA_NAZIV;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_BROJSTRANICA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_DATUMOBJAVLJIVANJA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_IDKATEGORIJE;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_IDWEBSERVIS;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_OPIS;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.autori;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.bazaDBOpenHelper;
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
    Button dDodajOnline;
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
        dDodajOnline=(Button)view.findViewById(R.id.dDodajOnline);
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
        else if(getArguments().getBoolean("Proslo")) {
            Knjiga book = getArguments().getParcelable("knjiga");
            String odabir = getArguments().getString("kategorija");

            long dodanaknjiga = dodajKnjigu(book);
            if (dodanaknjiga != -1){
                for (int i = 0; i < podaciKategorija.size(); i++) {
                    if (podaciKategorija.get(i).ToString().equals(odabir)) {
                        podaciKategorija.get(i).GetKnjige().add(book);
                        break;
                    }
                }

            boolean ubacen = false;
            for (int i = 0; i < autori.size(); i++) {
                for (int j = 0; j < book.getAutori().size(); j++) {
                    if (book.getAutori().get(j).getimeiPrezime().equals(autori.get(i).getimeiPrezime())) {
                        autori.get(i).getKnjige().add(book.getNaziv());
                        ubacen = true;
                        break;
                    }
                }
            }

            if (!ubacen) {
                for (int q = 0; q < book.getAutori().size(); q++) {
                    autori.add(new Autor(book.getAutori().get(q).getimeiPrezime()));
                    autori.get(autori.size() - 1).getKnjige().add(book.getNaziv());
                }
            }
            kategorije = podaciKategorija;
            listaKategorija = new ArrayList<String>();
            for (int i = 0; i < kategorije.size(); i++) {
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
        else {
            Toast.makeText(getActivity(), "Knjiga postoji u bazi!", Toast.LENGTH_SHORT).show();
                kategorije = podaciKategorija;
                listaKategorija = new ArrayList<String>();
                for (int i = 0; i < kategorije.size(); i++) {
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
//                poziv za bazu
                long uspjelo=dodajKategoriju(unosteksta.getText().toString());
                podaciKategorija.get(podaciKategorija.size()-1).setID(uspjelo);
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

        dDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle argumenti = new Bundle();
                argumenti.putParcelableArrayList("kategorije", podaciKategorija);
                FragmentOnline fd = new FragmentOnline();
                fd.setArguments(argumenti);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.ListeFragment, fd)
                            //.addToBackStack(null)
                            .commit();
            }
        });
    }

    private long dodajKategoriju(String naziv){
        ContentValues novi = new ContentValues();
        novi.put(KATEGORIJA_NAZIV,naziv);
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        db.insert(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,null,novi);
        long x=dajIDKategorije(naziv);
        return x;
    }
    public static long dajIDKategorije(String naziv){
        String[] koloneRezulat = new String[]{ KATEGORIJA_ID,KATEGORIJA_NAZIV};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return -1;
        while(cursor.moveToNext()){
            String naz=cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV));
            if(naziv.equals(naz)){
                int x=cursor.getInt(cursor.getColumnIndex(KATEGORIJA_ID));
                return x;
            }

        }
        return -1;
    }
    private long dodajKnjigu(Knjiga knjiga){
        //provjera postoji li u bazi
        long idKnjigePorvjera=dajIDKnjige(knjiga.getNaziv());
        if(idKnjigePorvjera!=-1)return -1;
        //zavrsena provjera
        ContentValues novi = new ContentValues();
        novi.put(KNJIGA_NAZIV,knjiga.getNaziv());
        novi.put(KNJIGA_OPIS,knjiga.getOpis());
        novi.put(KNJIGA_DATUMOBJAVLJIVANJA,knjiga.getDatumObjavljivanja());
        novi.put(KNJIGA_BROJSTRANICA,knjiga.getBrojStranica());
        novi.put(KNJIGA_IDWEBSERVIS,knjiga.getSlika().toString());
        novi.put(KNJIGA_IDKATEGORIJE,knjiga.getIdKategorije());
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        db.insert(BazaOpenHelper.DATABASE_TABLE_KNJIGA,null,novi);
        for(int i=0;i<knjiga.getAutori().size();i++){
            long DodanAutor=dodajAutora(knjiga.getAutori().get(i));
            //if(DodanAutor!=-1){
            dodajAutorstvo(knjiga,knjiga.getAutori().get(i));
            //}
        }
        long x=dajIDKnjige(knjiga.getNaziv());
        return x;
    }
    private long dodajAutora(Autor autor){
        if(provjeriPostojiLiAutorUBazi(autor))return -1;
        ContentValues novi = new ContentValues();
        novi.put(AUTOR_IME,autor.getimeiPrezime());
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTOR,null,novi);
        long x=dajIDAutora(autor.getimeiPrezime());
        return x;
    }
    private boolean provjeriPostojiLiAutorUBazi(Autor autor){
        String[] koloneRezulat = new String[]{ AUTOR_IME};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return false;
        while(cursor.moveToNext()){
            String poredi=cursor.getString(cursor.getColumnIndex(AUTOR_IME));
            if(poredi.equals(autor.getimeiPrezime()))return true;
        }
        return false;
    }
    private long dodajAutorstvo(Knjiga knjiga,Autor autor){
        long idAutora=dajIDAutora(autor.getimeiPrezime());
        long idKnjige=dajIDKnjige(knjiga.getNaziv());

        ContentValues novi = new ContentValues();
        novi.put(AUTORSTVO_IDAUTORA,idAutora);
        novi.put(AUTORSTVO_IDKNJIGE,idKnjige);
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO,null,novi);
        return 0;
    }
    public static long dajIDAutora(String naziv) {
        String[] koloneRezulat = new String[]{AUTOR_ID,AUTOR_IME};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if (cursor.getCount() <= 0) return -1;
        while (cursor.moveToNext()) {
            String rezultNaziv=cursor.getString(cursor.getColumnIndex(AUTOR_IME));
            if(naziv.equals(rezultNaziv)) {
                int x = cursor.getInt(cursor.getColumnIndex(AUTOR_ID));
                return x;
            }
        }
        return -1;
    }
    public static long dajIDKnjige(String naziv) {
        String[] koloneRezulat = new String[]{KNJIGA_ID,KNJIGA_NAZIV};
        String where =null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if (cursor.getCount() <= 0) return -1;
        while (cursor.moveToNext()) {
            String rezultatNaziv=cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV));
            if(naziv.equals(rezultatNaziv)){
                int x = cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));
                return x;
            }
        }
        return -1;
    }

}
