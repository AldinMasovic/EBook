package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

//import static ba.unsa.etf.rma.aldin_masovic.ebook.DodavanjeKnjigeAkt.xxx;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.OnItemClick {
    public static ArrayList<Kategorija> podaciKategorija = new ArrayList<Kategorija>();
    final ArrayList<String> unosi = new ArrayList<String>();
    public static ArrayList<Autor> autori = new ArrayList<Autor>();
    public static Boolean siri=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_liste);
        siri=false;
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
        arguments.putInt("pozicija",pos);
        EditText dd=(EditText) findViewById(R.id.tekstPretraga);
        if(dd==null)return;
        if(dd.getVisibility()==View.VISIBLE)
            arguments.putParcelable("kategorija", podaciKategorija.get(pos));
        else
            arguments.putParcelable("autori",autori.get(pos));
        KnjigeFragment fd = new KnjigeFragment();
        fd.setArguments(arguments);
        if(siri)
            getFragmentManager().beginTransaction().replace(R.id.ListeFragment2, fd).commit();
        else
            getFragmentManager().beginTransaction().replace(R.id.ListeFragment, fd).commit();
    }

}
