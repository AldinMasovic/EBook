package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.graphics.Bitmap;

/**
 * Created by B50-50 on 24.03.2018..
 */

public class Knjiga {
    private int slika;
    private String imeAutora;
    private String naziv;
    private boolean obojen;
    private Bitmap xxx;
    //kategorija
    public Knjiga(int pic,String name,String mess,Bitmap heh){
        slika=pic;
        imeAutora=name;
        naziv=mess;
        obojen=false;
        xxx=heh;
    }
    public Bitmap GetBit(){return xxx;}
    public void SetBit(Bitmap heh){xxx=heh;}
    public int GetSlika(){return slika;}
    public void SetSlika(int pic){slika=pic;}
    public String GetIme(){return imeAutora;}
    public void SetIme(String name){imeAutora=name;}
    public String GetNaziv(){return naziv;}
    public void SetNaziv(String mess){naziv=mess;}
    public void setColor(){obojen=true;}
    public boolean getColor(){return obojen;}
}
