package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by B50-50 on 24.03.2018..
 */

public class Knjiga implements Parcelable {
    private String id;
    private String naziv;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;

    private boolean obojen;
    private Bitmap xxx;
    private long idKategorije;
    private int idIntKnjige;
    public long  getIdKategorije(){return idKategorije;}
    public void setIdKategorije(long id){idKategorije=id;}
    public int getIdIntKnjige(){return idIntKnjige;}
    public void setIdIntKnjige(int id){idIntKnjige=id;}


    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja,
                  URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
    }

    //kategorija
    public String getId() {
        return id;
    }

    public void setId(String ID) {
        id = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String NAZIV) {
        naziv = NAZIV;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public Knjiga(String name, String mess, Bitmap heh) {
        autori = new ArrayList<>();
        Autor autor = new Autor(name);
        autori.add(autor);
        naziv = mess;
        obojen = false;
        xxx = heh;
    }

    public Knjiga(Knjiga x) {
        autori = x.getAutori();
        naziv = x.getNaziv();
        obojen = x.getColor();
        xxx = x.GetBit();
    }

    public Bitmap GetBit() {
        return xxx;
    }

    public void SetBit(Bitmap heh) {
        xxx = heh;
    }
//    public String GetIme(){return imeAutora;}
//    public void SetIme(String name){imeAutora=name;}


    public void setColor() {
        obojen = true;
    }

    public boolean getColor() {
        return obojen;
    }

    protected Knjiga(Parcel in) {
        id = in.readString();
        naziv = in.readString();
        if (in.readByte() == 0x01) {
            autori = new ArrayList<Autor>();
            in.readList(autori, Autor.class.getClassLoader());
        } else {
            autori = null;
        }
        opis = in.readString();
        datumObjavljivanja = in.readString();
        slika = (URL) in.readValue(URL.class.getClassLoader());
        brojStranica = in.readInt();
        obojen = in.readByte() != 0x00;
        xxx = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        if (autori == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(autori);
        }
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeValue(slika);
        dest.writeInt(brojStranica);
        dest.writeByte((byte) (obojen ? 0x01 : 0x00));
        dest.writeValue(xxx);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Knjiga> CREATOR = new Parcelable.Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };
}
