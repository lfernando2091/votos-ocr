package saganet.mx.com.bingo.startup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.io.File;

import saganet.mx.com.bingo.R;

public class HistorialView extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private Intent intent;
    private ImageView imageView;
    private AppCompatTextView resultado, contestado, ruta, casillad, secciond;
    private CollapsingToolbarLayout mCollapseingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.initial);
        resultado=(AppCompatTextView) findViewById(R.id.lblResultado);
        contestado=(AppCompatTextView) findViewById(R.id.lblContestados);
        casillad=(AppCompatTextView) findViewById(R.id.lblCasilla);
        secciond=(AppCompatTextView) findViewById(R.id.lblSeccion);
        ruta=(AppCompatTextView) findViewById(R.id.lblRuta);
        mCollapseingView=(CollapsingToolbarLayout)  findViewById(R.id.toolbar_layout);
        intent = getIntent();
        String url = intent.getStringExtra(History.EXTRA_URL);
        String res = intent.getStringExtra(History.EXTRA_RES);
        String contes = intent.getStringExtra(History.EXTRA_CONTES);
        String casilla = intent.getStringExtra(History.EXTRA_CAS);
        String seccion = intent.getStringExtra(History.EXTRA_SEC);

        resultado.setText(res + " elemento(s)");
        contestado.setText(contes);
        casillad.setText(casilla);
        secciond.setText(seccion);
        ruta.setText(url);

        File imgFile = new  File(url);
        mCollapseingView.setTitle(imgFile.getName());
        if (imgFile.exists()) {
            //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap thumbnail= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 800, 600);
            imageView.setImageBitmap(thumbnail);
        }else {
            imageView.setImageResource(R.drawable.a);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
            onBackPressed();
        return true;
    }
}
