package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mojaBaza.db";
//    KATEGORIJA
    public static final String DATABASE_TABLE_KATEGORIJA="Kategorija";
    public static final String KATEGORIJA_ID="_id";
    public static final String KATEGORIJA_NAZIV="naziv";
//    KNJIGA
    public static final String DATABASE_TABLE_KNJIGA="Knjiga";
    public static final String KNJIGA_ID="_id";
    public static final String KNJIGA_NAZIV="naziv";
    public static final String KNJIGA_OPIS="opis";
    public static final String KNJIGA_DATUMOBJAVLJIVANJA="datumObjavljivanja";
    public static final String KNJIGA_BROJSTRANICA="brojStranica";
    public static final String KNJIGA_IDWEBSERVIS="idWebServis";
    public static final String KNJIGA_IDKATEGORIJE="idkategorije";
//    AUTOR
    public static final String DATABASE_TABLE_AUTOR="Autor";
    public static final String AUTOR_ID="_id";
    public static final String AUTOR_IME="ime";
//    Autorstvo
    public static final String DATABASE_TABLE_AUTORSTVO="Autrostvo";
    public static final String AUTORSTVO_ID="_id";
    public static final String AUTORSTVO_IDAUTORA="idautora";
    public static final String AUTORSTVO_IDKNJIGE="idknjige";

    private static final String DATABASE_CREATE_KATEGORIJA="create table "+
            DATABASE_TABLE_KATEGORIJA + " ("+KATEGORIJA_ID+ " integer primary key autoincrement, "+
            KATEGORIJA_NAZIV + " text not null);";

    private static final String DATABASE_CREATE_KNJIGA="create table "+
            DATABASE_TABLE_KNJIGA + " ("+KNJIGA_ID + " integer primary key autoincrement, " +
            KNJIGA_NAZIV + " text not null, " +
            KNJIGA_OPIS + " text not null, "+ KNJIGA_DATUMOBJAVLJIVANJA + " text not null, " +
            KNJIGA_BROJSTRANICA + " integer not null, " +
            KNJIGA_IDWEBSERVIS + " text not null, " +
            KNJIGA_IDKATEGORIJE + " integer not null);";

    private static final String DATABASE_CREATE_AUTOR="create table " +
            DATABASE_TABLE_AUTOR + "(" + AUTOR_ID + " integer primary key autoincrement, "+
            AUTOR_IME + " text not null);";

    private static final String DATABASE_CRATE_AUTORSTVO="create table " +
            DATABASE_TABLE_AUTORSTVO + "(" + AUTORSTVO_ID + " integer primary key autoincrement, "+
            AUTORSTVO_IDAUTORA + " integer not null, " +
            AUTORSTVO_IDKNJIGE + " integer not null);";

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_KATEGORIJA);
        sqLiteDatabase.execSQL(DATABASE_CREATE_KNJIGA);
        sqLiteDatabase.execSQL(DATABASE_CREATE_AUTOR);
        sqLiteDatabase.execSQL(DATABASE_CRATE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTORSTVO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KATEGORIJA);
        // Kreiranje nove
        onCreate(sqLiteDatabase);
    }
}
