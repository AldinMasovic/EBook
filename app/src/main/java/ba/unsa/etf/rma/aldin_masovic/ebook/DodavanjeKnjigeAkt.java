package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import static android.R.attr.data;

public class DodavanjeKnjigeAkt extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    public static Bitmap xxx=null;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if(data == null)
                return;

            final TextView nazivKnjige = (TextView) findViewById(R.id.nazivKnjige);
            String nazivSlike = nazivKnjige.getText().toString();
            if(nazivSlike.length() == 0)
                nazivSlike = "...";
            FileOutputStream outputStream;
            outputStream = openFileOutput(nazivSlike, Context.MODE_PRIVATE);
            getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            ImageView slika = (ImageView)findViewById(R.id.naslovnaStr);
            Bitmap bm = null;
            while(bm == null)
                bm = BitmapFactory.decodeStream(openFileInput(nazivSlike));
            slika.setImageBitmap(bm);
            xxx=bm;

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        final Spinner odabir=(Spinner)findViewById(R.id.sKategorijaKnjige);
        ArrayList<String> kategorije=getIntent().getStringArrayListExtra("kategorije");
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kategorije);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        odabir.setAdapter(adapter1);
        final ImageView slika=(ImageView)findViewById(R.id.naslovnaStr);
        final Button unesiDugme= (Button)findViewById(R.id.dUpisiKnjigu);
        final Button ponistiDugme= (Button)findViewById(R.id.dPonisti);
        final EditText imeAutora= (EditText)findViewById(R.id.imeAutora);
        final EditText naziv= (EditText)findViewById(R.id.nazivKnjige) ;
        final Button dslika= (Button)findViewById(R.id.dNadjiSliku);

        unesiDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(DodavanjeKnjigeAkt.this, KategorijeAkt.class);
                myIntent.putExtra("Proslo",true);
                myIntent.putExtra("ime",imeAutora.getText().toString());
                myIntent.putExtra("naziv",naziv.getText().toString());
                myIntent.putExtra("spiner",odabir.getSelectedItem().toString());

                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                DodavanjeKnjigeAkt.this.startActivity(myIntent);
            }
        });
        ponistiDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(DodavanjeKnjigeAkt.this, KategorijeAkt.class);
                myIntent.putExtra("Proslo",false);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                DodavanjeKnjigeAkt.this.startActivity(myIntent);
            }
        });
        dslika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (Intent.ACTION_GET_CONTENT);
               // startActivity(intent);
                Uri data = Uri.fromFile(Environment.getExternalStorageDirectory());
                String type = "image/*";
                intent.setDataAndType(data, type);


                if (intent.resolveActivity(getPackageManager()) != null) {
                    //   //startActivityForResult(intent, 0);
                    startActivityForResult(Intent.createChooser(intent, "Odaberite fotografiju"), 1);
                }


                /*
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                */
               /* private Bitmap getBitmapFromUri(Uri uri) throws IOException {
                    ParcelFileDescriptor parcelFileDescriptor =
                            getContentResolver().openFileDescriptor(uri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    return image;
                }
                */
            }
        });


    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.naslovnaStr);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        */


    //}



}

