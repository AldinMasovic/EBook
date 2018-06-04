package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.facebook.stetho.Stetho;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTORSTVO_IDAUTORA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTORSTVO_IDKNJIGE;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTOR_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KATEGORIJA_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KATEGORIJA_NAZIV;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_BROJSTRANICA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_DATUMOBJAVLJIVANJA;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_ID;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_IDKATEGORIJE;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_IDWEBSERVIS;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KNJIGA_OPIS;

//import static ba.unsa.etf.rma.aldin_masovic.ebook.DodavanjeKnjigeAkt.xxx;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.OnItemClick,
MojAdapter.OnItemClick2{
    public static ArrayList<Kategorija> podaciKategorija = new ArrayList<Kategorija>();
    final ArrayList<String> unosi = new ArrayList<String>();
    public static ArrayList<Autor> autori = new ArrayList<Autor>();
    public static Boolean siri=false;
    public static BazaOpenHelper bazaDBOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        bazaDBOpenHelper=new BazaOpenHelper(this,DATABASE_NAME,null,1);
        setContentView(R.layout.fragment_liste);
        siri=false;
        try {
            podaciKategorija = dajSveKategorije();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        autori=dajSveAutore();
        final FragmentManager fm = getFragmentManager();
        FrameLayout ldetalji = (FrameLayout)findViewById(R.id.ListeFragment2);
        //ListeFragment f1 = (ListeFragment) fm.findFragmentById(R.id.ListeFragment);
        if (ldetalji != null) {
            siri=true;
            KnjigeFragment fu;
            fu=(KnjigeFragment)fm.findFragmentById(R.id.ListeFragment2);
            if(fu==null){
                fu=new KnjigeFragment();
                Bundle argumenti = new Bundle();
                argumenti.putInt("pozicija",0);
                argumenti.putString("kategorija","heh");
                //argumenti.putParcelableArrayList("ListaKategorija", podaciKategorija);
                fu.setArguments(argumenti);
                fm.beginTransaction().replace(R.id.ListeFragment2,fu).commit();
            }
/*
            f1 = new ListeFragment();
            Bundle argumenti = new Bundle();
            argumenti.putParcelableArrayList("ListaKategorija", podaciKategorija);
            f1.setArguments(argumenti);
            fm.beginTransaction().replace(R.id.ListeFragment, f1).commit();
*/        }
        Fragment fl=(Fragment)fm.findFragmentById(R.id.ListeFragment);
        if(fl==null || fl instanceof KnjigeFragment){
            fl=new ListeFragment();
            Bundle argumenti = new Bundle();
            argumenti.putParcelableArrayList("ListaKategorija", podaciKategorija);
            fl.setArguments(argumenti);
            fm.beginTransaction().replace(R.id.ListeFragment,fl).commit();
        }
        else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @Override
    public void onItemClicked(int pos) {
        Bundle arguments = new Bundle();
        arguments.putInt("pozicija", pos);
        EditText dd = (EditText) findViewById(R.id.tekstPretraga);
        if (dd == null) return;
        if (dd.getVisibility() == View.VISIBLE){
            arguments.putParcelable("kategorija", podaciKategorija.get(pos));
            long idKat = ListeFragment.dajIDKategorije(podaciKategorija.get(pos).getNaziv());
            try {
                ArrayList<Knjiga> pri = knjigeKategorije(idKat);
                podaciKategorija.get(pos).setKnjige(pri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        else{
            arguments.putParcelable("autori",autori.get(pos));
            long idAut=ListeFragment.dajIDAutora(autori.get(pos).getimeiPrezime());
            ArrayList<Knjiga> knjigeAutora=knjigeAutora(idAut);
            autori.get(pos).setKnjige1(knjigeAutora);
        }

        KnjigeFragment fd = new KnjigeFragment();
        fd.setArguments(arguments);
        if(siri)
            getFragmentManager().beginTransaction().replace(R.id.ListeFragment2, fd).commit();
        else
            getFragmentManager().beginTransaction().replace(R.id.ListeFragment, fd).commit();
    }

    @Override
    public void onItemClicked2(Knjiga knjiga) {
        Bundle argumenti = new Bundle();
        argumenti.putParcelable("knjiga",knjiga);
        FragmentPreporuci fd = new FragmentPreporuci();
        fd.setArguments(argumenti);

        getFragmentManager().beginTransaction()
                .replace(R.id.ListeFragment, fd)
                .addToBackStack(null)
                .commit();
    }
    private ArrayList<Kategorija> dajSveKategorije() throws MalformedURLException {
        ArrayList<Kategorija>povratniTip=new ArrayList<Kategorija>();
        String[] koloneRezulat = new String[]{ KATEGORIJA_ID, KATEGORIJA_NAZIV};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return povratniTip;
        while(cursor.moveToNext()){
            String x=cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV));
            povratniTip.add(new Kategorija(x));
        }
        return povratniTip;
    }
    public  ArrayList<Knjiga> knjigeKategorije(long idKategorije) throws MalformedURLException {
        String[] koloneRezultat1={KNJIGA_IDKATEGORIJE,KNJIGA_NAZIV,KNJIGA_OPIS,
                KNJIGA_DATUMOBJAVLJIVANJA,KNJIGA_BROJSTRANICA,KNJIGA_IDWEBSERVIS,KNJIGA_ID};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        ArrayList<Knjiga>povrateKnjige=new ArrayList<Knjiga>();
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor1=db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA,
                koloneRezultat1,where,whereArgs,groupBy,having,order);
        if(cursor1.getCount()<=0)
            return povrateKnjige;
        else{
            while(cursor1.moveToNext()){
                long id=cursor1.getInt(cursor1.getColumnIndex(KNJIGA_IDKATEGORIJE));
                if(id==idKategorije){
                    int idKnjige=cursor1.getInt(cursor1.getColumnIndex(KNJIGA_ID));
                    String naziv=cursor1.getString(cursor1.getColumnIndex(KNJIGA_NAZIV));
                    String opis=cursor1.getString(cursor1.getColumnIndex(KNJIGA_OPIS));
                    String datum=cursor1.getString(cursor1.getColumnIndex(KNJIGA_DATUMOBJAVLJIVANJA));
                    int br=cursor1.getInt(cursor1.getColumnIndex(KNJIGA_BROJSTRANICA));
                    String web=cursor1.getString(cursor1.getColumnIndex(KNJIGA_IDWEBSERVIS));
                    //TODO: string to url web
                    URL slikica=new URL(web);
                    //String idiii=""+idKnjige;
                    //TODO: traziti autore
                    ArrayList<Autor> autors=poveziKnjiguIAutore(idKnjige);
                    //autors.add(new Autor("ime i prezime"));
                    Knjiga book=new Knjiga("id",naziv,autors,opis,datum,slikica,br);
                    book.setIdKategorije(id);
                    book.setIdIntKnjige(idKnjige);
                    povrateKnjige.add(book);

                    }
            }
        }
        return povrateKnjige;
    }
    private ArrayList<Autor> poveziKnjiguIAutore(long idknjiga){
        String[] koloneRezulat = new String[]{ AUTORSTVO_IDKNJIGE,AUTORSTVO_IDAUTORA};
        //long IDKNJIGE=ListeFragment.dajIDKnjige(knjiga.getNaziv());
        ArrayList<Autor> autors=new ArrayList<Autor>();

        String where =AUTORSTVO_IDKNJIGE + "="+idknjiga;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0){
            autors.add(new Autor("nepoznat autor"));
            return autors;
        }
        while(cursor.moveToNext()){
            long idAutora=cursor.getInt(cursor.getColumnIndex(AUTORSTVO_IDAUTORA));
            Autor autoric=dajAutoraIzBaze(idAutora);
            autors.add(autoric);
        }
        return autors;
    }
    private Autor dajAutoraIzBaze(long id){
        String[] koloneRezulat = new String[]{ AUTOR_ID,AUTOR_IME};
        String where =null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return new Autor("nepoznat autor");
        while(cursor.moveToNext()){
            long idIzBaze=cursor.getInt(cursor.getColumnIndex(AUTOR_ID));
            if(idIzBaze==id){
                String naziv=cursor.getString(cursor.getColumnIndex(AUTOR_IME));
                return new Autor(naziv);
            }
        }
        return new Autor("nepoznat autor");
    }
    public static ArrayList<Autor> dajSveAutore(){
        String[] koloneRezulat = new String[]{ AUTOR_ID,AUTOR_IME};
        String where =null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        ArrayList<Autor>autors=new ArrayList<Autor>();
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return autors;
        while(cursor.moveToNext()){
                long idIzBaze=cursor.getInt(cursor.getColumnIndex(AUTOR_ID));
                String naziv=cursor.getString(cursor.getColumnIndex(AUTOR_IME));
                autors.add(new Autor(naziv));
            }
            return autors;
        }
    private ArrayList<Knjiga> knjigeAutora(long idAutora){
        String[] koloneRezultat1={AUTORSTVO_IDAUTORA,AUTORSTVO_IDKNJIGE};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        ArrayList<Knjiga>povrateKnjige=new ArrayList<Knjiga>();
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor1=db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO,
                koloneRezultat1,where,whereArgs,groupBy,having,order);
        if(cursor1.getCount()<=0)
            return povrateKnjige;
        while (cursor1.moveToNext()){
            long idIzBazeAutra=cursor1.getInt(cursor1.getColumnIndex(AUTORSTVO_IDAUTORA));
            if(idAutora==idIzBazeAutra){
                long idIzBazeKnjiga=cursor1.getInt(cursor1.getColumnIndex(AUTORSTVO_IDKNJIGE));
                Knjiga x=dajKnjigu(idIzBazeKnjiga);
                povrateKnjige.add(x);
            }
        }
        return povrateKnjige;
    }
    private Knjiga dajKnjigu(long idKnjige){
        String[] koloneRezultat1={KNJIGA_IDKATEGORIJE,KNJIGA_NAZIV,KNJIGA_OPIS,
                KNJIGA_DATUMOBJAVLJIVANJA,KNJIGA_BROJSTRANICA,KNJIGA_IDWEBSERVIS,KNJIGA_ID};
        String where =null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = bazaDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA,
                koloneRezultat1, where,
                whereArgs, groupBy, having, order);
        if(cursor.getCount()<=0)return null;
//        TODO: provjeri ovo null
        while(cursor.moveToNext()){
            long idIzBaze=cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));
            if(idIzBaze==idKnjige) {
                String naziv = cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV));
                String opis = cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS));
                String datum = cursor.getString(cursor.getColumnIndex(KNJIGA_DATUMOBJAVLJIVANJA));
                int br = cursor.getInt(cursor.getColumnIndex(KNJIGA_BROJSTRANICA));
                String web = cursor.getString(cursor.getColumnIndex(KNJIGA_IDWEBSERVIS));
                URL slikica = null;
                try {
                    slikica = new URL(web);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //TODO: traziti autore
                ArrayList<Autor> autors = poveziKnjiguIAutore(idKnjige);
                Knjiga book = new Knjiga("id", naziv, autors, opis, datum, slikica, br);
                //book.setIdKategorije(id);
                int aaa = (int) idKnjige;
                book.setIdIntKnjige(aaa);
                return book;
            }
            }
        return null;
        }

}
