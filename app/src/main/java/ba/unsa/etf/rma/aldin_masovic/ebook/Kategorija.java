package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by B50-50 on 24.03.2018..
 */

public class Kategorija implements Parcelable{
    private ArrayList<Knjiga> knjige;
    private String naziv;
    private long Id;
    public Kategorija(ArrayList<Knjiga> a,String name){
        knjige=a;
        naziv=name;
    }
    public long  getID(){return Id;}
    public void setID(long id){this.Id=id;}
    public Kategorija(){}
    public Kategorija(String name){
        knjige=new ArrayList<Knjiga>();
        naziv=name;
    }
    public String getNaziv(){return naziv;}
    public String ToString(){
        return naziv;
    }
    public ArrayList<Knjiga> GetKnjige(){return knjige;}
    public void setKnjige(ArrayList<Knjiga> books){knjige=books;}
    @Override
    public int describeContents() {
        return 0;
    }
    protected Kategorija(Parcel in){
        naziv=in.readString();
        //knjige=in.readArrayList();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(naziv);
            //dest.writeArray(knjige);

    }

    public static final Creator<Kategorija> CREATOR = new Creator<Kategorija>() {
        @Override
        public Kategorija createFromParcel(Parcel in){
            return new  Kategorija(in);
        }
        @Override
        public Kategorija[] newArray(int size){
            return new Kategorija[size];
        }
    };
}
