package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by B50-50 on 08.04.2018..
 */

public class Autor implements Parcelable
{
    private ArrayList<Knjiga> knjige1;
    private ArrayList<String>knjige;
    private String imeiPrezime;
    public Autor(String imeiPrezime){
        this.imeiPrezime=imeiPrezime;
        knjige1=new ArrayList<Knjiga>();
        knjige=new ArrayList<String>();
    }
    public Autor(String ime,String id){
        imeiPrezime=ime;
        knjige=new ArrayList<String>();
        knjige.add(id);
//        TODO: dodati knjigu pod ovim id-em u listu knjiga hmm
    }
    public ArrayList<Knjiga> getKnjige1(){return knjige1;}
    public void setKnjige1(ArrayList<Knjiga> books){
        knjige1=books;
        knjige=new ArrayList<String>();
        for(int i=0;i<books.size();i++){
            knjige.add(books.get(i).getNaziv());
        }
    }
    public int getBroj(){return knjige1.size();}
    public String getimeiPrezime(){return imeiPrezime;}
    public void setimeiPrezime(String aa){imeiPrezime=aa;}
    public ArrayList<String> getKnjige(){return knjige;}
    public void setKnjige(ArrayList<String>knjige){this.knjige=knjige;}
    public ArrayList<Knjiga> getknjige1(){return knjige1;}
    public void dodajKnjigu(String id){
        for(int i=0;i<knjige.size();i++){
            if(knjige.get(i).equals(id))return;
        }
        knjige.add(id);
    }
    @Override
    public String toString(){
        return knjige.size()+":  "+imeiPrezime;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    protected Autor(Parcel in){
        imeiPrezime=in.readString();
        knjige=in.readArrayList(String.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeiPrezime);
        dest.writeList(knjige);
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
