package ba.unsa.etf.rma.aldin_masovic.ebook;

public class Kontakt {

    private String Ime;
    private String Email;

    public Kontakt(String ime, String email) {
        Ime = ime;
        Email = email;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIme() {
        return Ime;
    }

    public String getEmail() {
        return Email;
    }
}
