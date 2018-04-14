package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by B50-50 on 08.04.2018..
 */

public class Autor implements Parcelable
{
    private ArrayList<Knjiga> knjige;
    private String naziv;
    public Autor(String naziv){
        this.naziv=naziv;
        knjige=new ArrayList<Knjiga>();
    }
    public int getBroj(){return knjige.size();}
    public String getNaziv(){return naziv;}
    public void setNaziv(String aa){naziv=aa;}
    public ArrayList<Knjiga> getKnjige(){return knjige;}
    @Override
    public String toString(){
        return knjige.size()+":  "+naziv;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    protected Autor(Parcel in){
        naziv=in.readString();
        //knjige=in.readArrayList();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        //dest.writeArray(knjige);

    }
    public static final Creator<Autor> CREATOR = new Creator<Autor>() {
        @Override
        public Autor createFromParcel(Parcel in){
            return new  Autor(in);
        }
        @Override
        public Autor[] newArray(int size){
            return new Autor[size];
        }
    };
}
