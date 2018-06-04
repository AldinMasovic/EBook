package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.podaciKategorija;


public class ListaKnjigaAkt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);
        int i=getIntent().getIntExtra("pozicija",0);
        final ListView lista=(ListView)findViewById(R.id.listaKnjiga);
        final Button povratak= (Button) findViewById(R.id.dPovratak);
        //final ArrayList<String> unosi=new ArrayList<String>();
        final ArrayList<Knjiga> unosi= new ArrayList<Knjiga>();
        final MojAdapter adapter=new MojAdapter(this,R.layout.izgled_liste_knijga_dva,unosi);
        final int pozicija=i;
        //final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,
         //      unosi);

        for(int j=0;j<podaciKategorija.get(i).GetKnjige().size();j++){
            //unosi.add(podaciKategorija.get(i).GetKnjige().get(j).GetNaziv());
            unosi.add(podaciKategorija.get(i).GetKnjige().get(j));
        }
        adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);
        for(int j=0;j<podaciKategorija.get(i).GetKnjige().size();j++) {
            if (podaciKategorija.get(i).GetKnjige().get(j).getColor())
                if(lista.getChildAt(j)!=null)
                lista.getChildAt(j).setBackgroundColor(0xffaabbed);
        }
       //i popraviti izgled da nije simple item
        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ListaKnjigaAkt.this,KategorijeAkt.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                ListaKnjigaAkt.this.startActivity(myIntent);

            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //obojiti u plavu boju
                podaciKategorija.get(pozicija).GetKnjige().get(position).setColor();
                lista.getChildAt(position).setBackgroundColor(0xffaabbed);
            }
        });

    }
}
