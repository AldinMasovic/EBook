package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class KnjigePoznanika extends IntentService {

    public static final int STATUS_START=0;
    public static final int STATUS_FINISH= 1;
    public static final int STATUS_ERROR=2;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "ba.unsa.etf.rma.aldin_masovic.ebook.action.FOO";
    private static final String ACTION_BAZ = "ba.unsa.etf.rma.aldin_masovic.ebook.action.BAZ";
    ResultReceiver receiver;
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ba.unsa.etf.rma.aldin_masovic.ebook.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ba.unsa.etf.rma.aldin_masovic.ebook.extra.PARAM2";
    private ArrayList<Knjiga>knjige;
    private static  String idKorisnika="";

    public KnjigePoznanika() {
        super(null);
    }
    public KnjigePoznanika(String name){
        super(name);
        // Sav posao koji treba da obavi konstruktor treba da se


    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startBackgroundTask​(​intent​,​ startId​);

    //    onHandleIntent(intent);
        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        // Akcije koje se trebaju obaviti pri kreiranju servisa
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
    /*public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, KnjigePoznanika.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    /*public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, KnjigePoznanika.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(intent.hasExtra("idKorisnika")){
                idKorisnika=intent.getStringExtra("idKorisnika");
            }
            if(intent.hasExtra("receiver")){
                receiver=intent.getParcelableExtra("receiver");
            }

            Bundle bundle=new Bundle();
            receiver.send(STATUS_START,Bundle.EMPTY);

            /*final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
            try{
                String url = "https://www.googleapis.com/books/v1/users/" + URLEncoder.encode(idKorisnika, "utf-8")
                        + "/bookshelves";
                URL urll = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection)urll.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);
//                TODO:ovo ne treba ovdje nego u create
                knjige=new ArrayList<Knjiga>();
                JSONObject jo = new JSONObject(rezultat);

                JSONArray items = jo.getJSONArray("items");


                for(int i = 0; i < items.length(); i++) {
                    JSONObject knjiga1 = items.getJSONObject(i);

                    String selfLink;
                    if (knjiga1.has("selfLink")) {
                        selfLink = knjiga1.getString("selfLink");
                    } else selfLink = null;
                    selfLink += "/volumes";
                    URL url2 = new URL(selfLink);
                    HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                    InputStream in2 = new BufferedInputStream(urlConnection2.getInputStream());
                    String rezultat2 = convertStreamToString(in2);
                    String access;
                    if (knjiga1.has("access")) {
                        access = knjiga1.getString("access");
                    } else access = "";
                    if (!access.equals("PUBLIC")) continue;


                    JSONObject object23 = new JSONObject(rezultat2);
                    JSONArray stvari = object23.getJSONArray("items");

                    for (int j = 0; j < stvari.length(); j++) {
                        JSONObject knjiga = stvari.getJSONObject(j);

                        String id;
                        if(knjiga.has("id"))
                            id = knjiga.getString("id");
                        else id = "";

                        JSONObject volumeInfo = knjiga.getJSONObject("volumeInfo");

                        String naziv;
                        if(volumeInfo.has("title"))
                            naziv = volumeInfo.getString("title");
                        else naziv = "unknown";


                        ArrayList<Autor> autori = new ArrayList<Autor>();
                        if(volumeInfo.has("authors")) {
                            JSONArray authors = volumeInfo.getJSONArray("authors");
                            for (int k = 0; k < authors.length(); k++) {
                                String nazivAutora = authors.getString(k);
                                autori.add(new Autor(nazivAutora, id));
                            }
                        }
                        else
                            autori.add(new Autor("nepoznat autor", id));


                        String opis;
                        if(volumeInfo.has("description"))
                            opis = volumeInfo.getString("description");
                        else
                            opis = "Nema opisa";

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

                }
                bundle.putParcelableArrayList("knjige",knjige);
                receiver.send(STATUS_FINISH,bundle);


        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
                e.printStackTrace();
            }




        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
/*    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    /*private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
