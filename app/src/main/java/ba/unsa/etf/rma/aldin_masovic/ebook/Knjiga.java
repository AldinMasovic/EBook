package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by B50-50 on 24.03.2018..
 */

public class Knjiga implements Parcelable {
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
    public Knjiga(Knjiga x){
        this.slika=x.GetSlika();
        imeAutora=x.GetIme();
        naziv=x.GetNaziv();
        obojen=x.getColor();
        xxx=x.GetBit();
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

    @Override
    public int describeContents() {
        return 0;
    }
    protected Knjiga(Parcel in){
        naziv=in.readString();
        imeAutora=in.readString();
        //obojen=in.readInt();
        //
        //knjige=in.readArrayList();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(imeAutora);
        //dest.writeArray(knjige);

    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in){
            return new  Knjiga(in);
        }
        @Override
        public Knjiga[] newArray(int size){
            return new Knjiga[size];
        }
    };
}
