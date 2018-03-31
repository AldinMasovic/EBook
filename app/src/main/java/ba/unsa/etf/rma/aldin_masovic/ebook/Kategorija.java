package ba.unsa.etf.rma.aldin_masovic.ebook;

import java.util.ArrayList;

/**
 * Created by B50-50 on 24.03.2018..
 */

public class Kategorija {
    private ArrayList<Knjiga> knjige;
    private String naziv;
    public Kategorija(ArrayList<Knjiga> a,String name){
        knjige=a;
        naziv=name;
    }
    public Kategorija(String name){
        knjige=new ArrayList<Knjiga>();
        naziv=name;
    }
    public String ToString(){
        return naziv;
    }
    public ArrayList<Knjiga> GetKnjige(){return knjige;}

}
