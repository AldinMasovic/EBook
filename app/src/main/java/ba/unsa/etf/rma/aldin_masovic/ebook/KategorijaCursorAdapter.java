package ba.unsa.etf.rma.aldin_masovic.ebook;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import static ba.unsa.etf.rma.aldin_masovic.ebook.BazaOpenHelper.KATEGORIJA_NAZIV;

public class KategorijaCursorAdapter extends ResourceCursorAdapter {


    public KategorijaCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView kategorija=(TextView)view.findViewById(R.id.teksticsam);
        kategorija.setText(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)));
    }
}
