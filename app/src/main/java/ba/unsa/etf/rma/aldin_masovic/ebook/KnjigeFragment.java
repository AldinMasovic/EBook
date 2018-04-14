package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.autori;
import static ba.unsa.etf.rma.aldin_masovic.ebook.KategorijeAkt.podaciKategorija;

/**
 * Created by B50-50 on 08.04.2018..
 */

public class KnjigeFragment extends Fragment {
    private ArrayList<Knjiga> knjige;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View iv= inflater.inflate(R.layout.fragment_za_knjige, container, false);
        if(getArguments()!=null && getArguments().containsKey("pozicija")){
            final int pos=getArguments().getInt("pozicija");
            if(getArguments().containsKey("kategorija"))
                knjige=podaciKategorija.get(pos).GetKnjige();
            else if(getArguments().containsKey("autori"))
                knjige=autori.get(pos).getKnjige();
            //knjige=getArguments().getParcelableArrayList("kategorija");
            Button dPovratak=(Button)iv.findViewById(R.id.dPovratak);
            final ListView listaKnjiga=(ListView)iv.findViewById(R.id.listaKnjiga);
            final MojAdapter adapter = new MojAdapter(getActivity(), R.layout.izgled_liste_knijga_dva, knjige);
            listaKnjiga.setAdapter(adapter);

            dPovratak.setOnClickListener(new View.OnClickListener() {
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
                }
            });
            listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //obojiti u plavu boju
                    //podaciKategorija.get(pos).GetKnjige().get(position).setColor();
                    knjige.get(position).setColor();
                    listaKnjiga.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.plava));
                }
            });
            for(int i=0;i<knjige.size();i++){
                if(knjige.get(i).getColor()){
                    if(listaKnjiga.getChildAt(i)==null){continue;}
                    listaKnjiga.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.plava));
                }
            }

        }
        return iv;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments()!=null && getArguments().containsKey("pozicija")) {
            int pos=getArguments().getInt("pozicija");
            if(getArguments().containsKey("kategorija"))
                knjige=podaciKategorija.get(pos).GetKnjige();
            else if(getArguments().containsKey("autori"))
                knjige=autori.get(pos).getKnjige();
            //knjige=getArguments().getParcelableArrayList("kategorija");
            Button dPovratak=(Button)getView().findViewById(R.id.dPovratak);
            ListView listaKnjiga=(ListView)getView().findViewById(R.id.listaKnjiga);
            final MojAdapter adapter = new MojAdapter(getActivity(), R.layout.izgled_liste_knijga_dva, knjige);
            listaKnjiga.setAdapter(adapter);
        }
        else if(podaciKategorija.size()!=0){
            knjige=podaciKategorija.get(0).GetKnjige();
            ListView listaKnjiga=(ListView)getView().findViewById(R.id.listaKnjiga);
            final MojAdapter adapter = new MojAdapter(getActivity(), R.layout.izgled_liste_knijga_dva, knjige);
            listaKnjiga.setAdapter(adapter);
        }
    }
}
