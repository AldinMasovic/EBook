package ba.unsa.etf.rma.aldin_masovic.ebook;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.jar.JarEntry;

public class DohvatiNajnovije extends AsyncTask<String,Void,Void> {
    ArrayList<Knjiga> knjige;

    public interface IDohvatiNajnovijeDone{
        // bio muzicar umjesto autora
        public void onNajnovijeDone(ArrayList<Knjiga>knjige);
    }
    //    public DohvatiKnjige(IDohvatiKnjigeDone idohvati){
//        this.idohvati=idohvati;
//    }
    Knjiga rez;
    private IDohvatiNajnovijeDone roki;
    public DohvatiNajnovije(DohvatiNajnovije.IDohvatiNajnovijeDone p) {
        roki= p;
        knjige=new ArrayList<Knjiga>();
    };
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        roki.onNajnovijeDone(knjige);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    @Override
    protected Void doInBackground(String... strings){

        try {
            //String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + URLEncoder.encode(strings[0], "utf-8") + "&maxResults=5";
            String url = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + URLEncoder.encode(strings[0], "utf-8") + "&maxResults=5&orderBy=newest";
            URL urll = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection)urll.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);
//          TODO: nezz šta je ovo treba pogledati
            JSONArray items = jo.getJSONArray("items");

            for(int i = 0; i < items.length(); i++)
            {
                JSONObject knjiga = items.getJSONObject(i);

                String id;
                if(knjiga.has("id"))
                    id = knjiga.getString("id");
                else id = null;

                JSONObject volumeInfo = knjiga.getJSONObject("volumeInfo");

                String naziv;
                if(volumeInfo.has("title"))
                    naziv = volumeInfo.getString("title");
                else naziv = "unknown";


                ArrayList<Autor> autori = new ArrayList<Autor>();
                if(volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        String nazivAutora = authors.getString(j);
                        autori.add(new Autor(nazivAutora, id));
                    }
                }
                else
                    autori.add(new Autor("nepoznat autor", id));


                String opis;
                if(volumeInfo.has("description"))
                    opis = volumeInfo.getString("description");
                else
                    opis = "No value for description";

                String datumObjavljivanja;

                if(volumeInfo.has("publishedDate"))
                    datumObjavljivanja = volumeInfo.getString("publishedDate");
                else
                    datumObjavljivanja = "unknown";


                URL urlSlika;
                String slika = "https://i.imgur.com/bYtW9OM.jpg";
                if(volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    slika = imageLinks.getString("thumbnail");
                }
                urlSlika = new URL(slika);

                int brojStranica;
                if(volumeInfo.has("pageCount"))
                    brojStranica = volumeInfo.getInt("pageCount");
                else
                    brojStranica = 0;

                Knjiga k = new Knjiga(id, naziv, autori, opis, datumObjavljivanja, urlSlika, brojStranica);
                knjige.add(k);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
