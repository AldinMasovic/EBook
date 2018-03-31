package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.aldin_masovic.ebook.DodavanjeKnjigeAkt.xxx;

public class KategorijeAkt extends AppCompatActivity {
    public static ArrayList<Kategorija> podaciKategorija=new ArrayList<Kategorija>();
    final ArrayList<String> unosi=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);
        final EditText unosteksta = (EditText) findViewById(R.id.tekstPretraga);
        final Button button_pretraga = (Button) findViewById(R.id.dPretraga);
        final Button button_kategorija = (Button) findViewById(R.id.dDodajKategoriju);
        final Button button_knjiga = (Button) findViewById(R.id.dDodajKnjigu);
        final ListView lista=(ListView)findViewById(R.id.listaKategorija);
        //final ArrayList<String> unosi=new ArrayList<String>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,unosi);
        lista.setAdapter(adapter);
        for(int i=0;i<podaciKategorija.size();i++){
            unosi.add(podaciKategorija.get(i).ToString());
            //adapter.add(podaciKategorija.get(i).ToString());
        }
        adapter.notifyDataSetChanged();
        //for(int k=0;k<unosi.size();k++)adapter.add(unosi.get(k));
        //adapter.notifyDataSetChanged();
        //List<Kategorija> podaciKategorija;
        //public static ArrayList<Kategorija> podaciKategorija=new ArrayList<Kategorija>();
        /*podaciKategorija.add(new Kategorija("Drama"));
        podaciKategorija.add(new Kategorija("Komedija"));
        unosi.add(podaciKategorija.get(0).ToString());
        unosi.add(podaciKategorija.get(1).ToString());*/
        adapter.notifyDataSetChanged();
        button_kategorija.setEnabled(false);
        Boolean jeste=getIntent().getBooleanExtra("Proslo",false);
        if(jeste){
            String kategory=getIntent().getStringExtra("spiner");
            String ime=getIntent().getStringExtra("ime");
            String naziv=getIntent().getStringExtra("naziv");
            //////////////////////////////////////////////alberteee hehe
            Knjiga UbaciKnjigu=new Knjiga(0,ime,naziv,xxx);
            for(int i=0;i<podaciKategorija.size();i++){
                if(podaciKategorija.get(i).ToString().equals(kategory)) {
                    podaciKategorija.get(i).GetKnjige().add(UbaciKnjigu);
                    break;
                }
            }
        }
        button_knjiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                myIntent.putStringArrayListExtra("kategorije",unosi);
                KategorijeAkt.this.startActivity(myIntent);
            }
        });
        button_kategorija.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(unosteksta.getText().toString().length()==0){
                return;
            }
            podaciKategorija.add(new Kategorija(unosteksta.getText().toString()));
            unosi.add(unosteksta.getText().toString());
            adapter.add(unosteksta.getText().toString());
            adapter.notifyDataSetChanged();
            ///
            adapter.getFilter().filter(unosteksta.getText().toString());
            ////
            button_kategorija.setEnabled(false);
            }
        });
        button_pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(null);
                if(unosteksta.getText().toString().length()==0){
                    return;
                }
                adapter.getFilter().filter(unosteksta.getText().toString());
                adapter.notifyDataSetChanged();
                if(adapter.isEmpty()){
                    button_kategorija.setEnabled(true);
                }
                else{
                    button_kategorija.setEnabled(false);
                }
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                myIntent.putExtra("pozicija",position);
                KategorijeAkt.this.startActivity(myIntent);
            }
        });

    }
}
