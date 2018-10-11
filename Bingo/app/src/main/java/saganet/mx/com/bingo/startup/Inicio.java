package saganet.mx.com.bingo.startup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;
import saganet.mx.com.bingo.Database.Controler.Sesion;
import saganet.mx.com.bingo.Database.Tablas.CasillaEO;
import saganet.mx.com.bingo.Database.Tablas.ConfigEO;
import saganet.mx.com.bingo.Database.Tablas.DatosEO;
import saganet.mx.com.bingo.Database.Tablas.HistorialEO;
import saganet.mx.com.bingo.Database.Tablas.SeccionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioAsignacionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioEO;
import saganet.mx.com.bingo.R;
import saganet.mx.com.bingo.file.SaveMedia;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.sheet.SheetOMR;
import saganet.mx.com.bingo.sync.GspMetod;
import saganet.mx.com.bingo.sync.PostMetod;
import saganet.mx.com.bingo.variables.BingoC;

public class Inicio extends AppCompatActivity {
    LoggerC log=new LoggerC(Inicio.class);
    SheetOMR sheetOMR= new SheetOMR();
    public Sesion sesion;
    History history;
    PlaceholderFragment placeholderFragment;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    private String mCurrentPhotoPath;
    private Bitmap bitmap=null;
    private TabLayout tabLayout;
    private PostMetod postMetod;
    private DrawerLayout drawerLayout;
    private GspMetod instance;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    public DatosEO datosEO;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sesion.close();
    }

    @Override
    protected void onResumeFragments() {
        log.printf("onResumeFragments");
    }

    @Override
    protected void onResume(){
        super.onResume();
        log.printf("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.printf("onPause");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Mostrar boton de regresp
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        drawerLayout = (DrawerLayout) findViewById(R.id.inicioLayout);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //log.printf("position:: "+position);
            }

            @Override
            public void onPageSelected(int position) {
                //log.printf("position:: "+position);
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_history);
                tabLayout.getTabAt(2).setIcon(R.drawable.ic_setting);
                switch (position){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_white);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history_white);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_setting_white);
                        break;
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                //log.printf("state:: "+state);
            }
        });
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_setting);

        sesion=new Sesion(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCameraCapture(view);
                /*if(placeholderFragment.getSpinnerSelectedItem(BingoC.mCasillasId, placeholderFragment.spCasilla) &&
                        placeholderFragment.getSpinnerSelectedItem(BingoC.mSeccionesId, placeholderFragment.spSecciones)) ShowCameraCapture(view);
                else Snackbar.make(view, "Faltan datos, seleccione seccion/casilla.", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
            }
        });
        getTrackingGps();
    }

    private void ShowCameraCapture(View view){
        //Pair<View , String> pair= Pair.create(view.findViewById(R.id.fab),"FAB_TRANS");
        //ActivityOptionsCompat compat;
        //Activity activity= Inicio.this;
        //compat= ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair);
        Intent i = new Intent(this, CaptureImage.class);
        i.putExtra(CaptureImage.EXTRA_ID, "-");
        //startActivityForResult(i, CaptureImage.REQUEST_CAPTURE_IMAGE,compat.toBundle());
        startActivityForResult(i, CaptureImage.REQUEST_CAPTURE_IMAGE);
        //overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile = SaveMedia.getOutputMediaFile(SaveMedia.MEDIA_TYPE_IMAGE);
            mCurrentPhotoPath= pictureFile.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        instance.StartLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        //Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
            case CaptureImage.REQUEST_CAPTURE_IMAGE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        if(BingoC.TakePictureProcessing){
                            try{
                                HistorialEO eo;
                                sheetOMR.RunOMR();
                                sesion.SVE(eo=nuevoHisorial(Integer.valueOf(BingoC.mUsuarioId),Integer.valueOf(BingoC.mSeccione),BingoC.mCasilla,BingoC.PHOTO_PATH,BingoC.PHOTO_TITLE,sheetOMR.Size(),sheetOMR.getResultValue(), sheetOMR.getResultKey(),0,0,Integer.valueOf(BingoC.mUsuarioVersion)));
                                history.AddHistory(nuevoHisorial(Integer.valueOf(BingoC.mUsuarioId),Integer.valueOf(BingoC.mSeccione),BingoC.mCasilla,BingoC.PHOTO_PATH,BingoC.PHOTO_TITLE,sheetOMR.Size(),sheetOMR.getResultValue(), sheetOMR.getResultKey(),0,0,Integer.valueOf(BingoC.mUsuarioVersion)));
                                if(getInternetConnection()) new TaskSubirDato().execute(eo);
                                BingoC.TakePictureProcessing=false;
                                svePicture(BingoC.bitmap2, BingoC.PHOTO_PATH);
                                //Snackbar.make(Inicio.this, "", Snackbar.LENGTH_LONG);
                            }catch (Exception e){
                                log.printf(e.getMessage());
                            }
                        }
                        break;
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        try {
                            if(bitmap!=null){
                                bitmap.recycle();
                                bitmap=null;
                            }
                        }catch (Exception e){        }
                        try {
                            bitmap = BitmapFactory.decodeStream(new FileInputStream(mCurrentPhotoPath), null, options);
                        } catch (FileNotFoundException e) {
                        }
                        showPreviewScren(bitmap);
                        break;
                }
            break;
        }
    }

    private void svePicture(byte[] bytes, String file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                @SuppressWarnings("MissingPermission")
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(instance.getApiClient());
                instance.setUI(lastLocation);
            } else {
                BingoC.Mensage("Es importante que active/configure el posicionamiento GPS. Además de permitir que la aplicación obtenga acceso a su ubicación.",this);
                try {
                    setMobileDataEnabled(this,true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                //Log.e(LOGTAG, "Permiso denegado");
            }
        }
        if(requestCode == PETICION_CONFIG_UBICACION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                log.printf("Permiso consedido por ti para activar la localización......");
            }
        }
    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }

    public void showPreviewScren(Bitmap res) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment editNameDialogFragment = PreviewImage.newInstance("","",res);
            editNameDialogFragment.show(fm, "fragment");
        }
        catch (Exception e){
        }
    }

    private HistorialEO nuevoHisorial(int idUsuario, int idSeccion, String idCasilla,String url, String title, int cout, String descr, String info, int del, int sync, int version){
        return new HistorialEO((sesion.getLastAutoId(new HistorialEO())),idUsuario,idSeccion,idCasilla,url,title,cout,descr, info,del,sync,version);
    }

    @Override
    public boolean onSupportNavigateUp() {
            BingoC.mSeccioneId="";
            onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    private class TaskSubirDatos extends AsyncTask<List<HistorialEO>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMetod= new PostMetod(sesion);
        }

        @Override
        protected Integer doInBackground(List<HistorialEO>... voids) {
            int status=0;
            sesion.getHistorial();
            if(BingoC.EOS.size()!=0){
                for (HistorialEO eo :voids[0]){
                    status=postMetod.Gone(
                            BingoC.URL_SINCRONIZACION,
                            new String[]{"usuario","seccion","casilla","contestados","fechafoto", "longitud","latitud","imei","filacolumna"},
                            new String[]{
                                    String.valueOf(eo.getIdUsuario()),
                                    String.valueOf(eo.getIdSeccion()),
                                    eo.getIdCasilla(),
                                    eo.getImgDescription(),
                                    eo.getFechaCaptura(),
                                    BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE,
                                    BingoC.DEVICE_IMEI,
                                    eo.getImgDInfo()
                            }
                    );
                    if(status==200){
                        sesion.UDE(new HistorialEO(eo.get_id()), new String[]{Colums.COLUMN_SYNC},new String[]{"1"});
                        delFile(eo.getImgUrl());
                    }
                }
            }else {
                status=100;
            }
            if(status==200)sesion.getHistorial();
            return status;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            if(aVoid==200) {
                history.refresh();
                Snackbar.make(drawerLayout, "Elementos subido con exito.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }if(aVoid==100) {
                Snackbar.make(drawerLayout, "No existen elementos pendientes.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }else {
                Snackbar.make(drawerLayout, "Ocurrio un error en el proceso.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    }

    private class TaskSubirDato extends AsyncTask<HistorialEO, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMetod= new PostMetod(sesion);
        }

        @Override
        protected Integer doInBackground(HistorialEO... eo) {
            int status=0;
            status=postMetod.Gone(
                    BingoC.URL_SINCRONIZACION,
                    new String[]{"usuario","seccion","casilla","contestados","fechafoto", "longitud","latitud","imei","filacolumna"},
                    new String[]{
                            String.valueOf(eo[0].getIdUsuario()),
                            String.valueOf(eo[0].getIdSeccion()),
                            eo[0].getIdCasilla(),
                            eo[0].getImgDescription(),
                            eo[0].getFechaCaptura(),
                            BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE,
                            BingoC.DEVICE_IMEI,
                            eo[0].getImgDInfo()
                    }
            );
            if(status==200){
                sesion.UDE(new HistorialEO(eo[0].get_id()), new String[]{Colums.COLUMN_SYNC},new String[]{"1"});
                delFile(eo[0].getImgUrl());
            }
            sesion.getHistorial();
            return status;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            if(aVoid==200) {
                Snackbar.make(drawerLayout, "Elemento subido con exito.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                history.refresh();
            }
            else {
                Snackbar.make(drawerLayout, "Ocurrio un error en el proceso.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    }

    private void delFile(String s){
        File file= new File(s);
        if(file.exists()){
            file.delete();
        }
    }

    private boolean getInternetConnection(){
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
            /*Wifi*/
            return true;
        } else if( mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED ){
            /*Mobile 3G*/
            return true;
        } else {
            return false;
        }
    }

    private void getTrackingGps(){
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
            /*Wifi*/
            instance= new GspMetod(this, Inicio.this,drawerLayout,true);
        } else if( mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED ){
            /*Mobile 3G*/
            instance= new GspMetod(this, Inicio.this,drawerLayout,false);
        } else {

        }
    }

    private class TaskResync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMetod= new PostMetod(sesion);
            datosEO= new DatosEO();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String msg="";
            if(postMetod.Gone(
                    SaveMedia.getOutputFile("Sync"),
                    BingoC.URL_USUARIOS,
                    new String[]{"usuario","pw","imei","longitud","latitud"},
                    new String[]{BingoC.mUsuarioNick,BingoC.mUsuarioPassword,BingoC.DEVICE_IMEI,BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE}
            )==200){
                msg=postMetod.Read(
                        new String[]{BingoC.UsuarioSeccionUpdate, BingoC.UsuarioUpdate, BingoC.SeccionUpdate, BingoC.CasillaUpdate,BingoC.DatosSync},
                        new Component[]{new UsuarioAsignacionEO(),new UsuarioEO(), new SeccionEO(), new CasillaEO(),datosEO},
                        false, PostMetod.SAFE_INSERT);
                postMetod.Read(
                        new String[]{BingoC.UsuarioSeccionUpdate, BingoC.UsuarioUpdate, BingoC.SeccionUpdate, BingoC.CasillaUpdate,BingoC.DatosSync},
                        new Component[]{new UsuarioAsignacionEO(),new UsuarioEO(), new SeccionEO(), new CasillaEO(),datosEO},
                        true, PostMetod.SAFE_UPDATE);
            }
            int paquete=datosEO.getUsuarioSeccionPaquetes();
            //Evaluar la existencia de paquetes(fragmentos)
            if (paquete>1) {
                for(int i=1; i<paquete; i++){
                    if(datosEO.getUltimoId()!=0){
                        if(postMetod.Gone(
                                SaveMedia.getOutputFile("Usuario"),
                                BingoC.URL_USUARIOS_PAQUETE,
                                new String[]{"usuario","pw","imei","longitud","latitud","ultimo_id","id_usuario"},
                                new String[]{BingoC.mUsuarioNick,BingoC.mUsuarioPassword,BingoC.DEVICE_IMEI,BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE, String.valueOf(datosEO.getUltimoId()), String.valueOf(datosEO.get_id())}
                        )==200){
                            msg=postMetod.Read(
                                    new String[]{BingoC.UsuarioSeccionUpdate,BingoC.DatosSync},
                                    new Component[]{new UsuarioAsignacionEO(),datosEO},
                                    false, PostMetod.SAFE_INSERT
                            );
                            postMetod.Read(
                                    new String[]{BingoC.UsuarioSeccionUpdate,BingoC.DatosSync},
                                    new Component[]{new UsuarioAsignacionEO(),datosEO},
                                    true, PostMetod.SAFE_UPDATE
                            );
                        }
                    }
                }
                //Llegamos hasta el ultimo elemento de la sincronizacion
                if(datosEO.getUltimoId()==0){
                    datosEO.setUsuarioSeccionPaquetes(paquete);
                    sesion.UDE(datosEO);
                }
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            Snackbar.make(drawerLayout, aVoid, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.ic_animated_download);
            //item.setIcon(drawable);
            //drawable.start();
            new AlertDialog.Builder(Inicio.this)
                    .setTitle("Espere...")
                    .setMessage("¿Sincronizar todos los elementos pendientes (Necesario conexión a internet.)?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(getInternetConnection()){
                                if(BingoC.EOS.size()!=0){
                                    Snackbar.make(drawerLayout, "Se estan subiendo los datos, espere por favor.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    new TaskSubirDatos().execute(BingoC.EOS);
                                }
                                else Snackbar.make(drawerLayout, "No existen elementos pendientes.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                            else {
                                Snackbar.make(drawerLayout, "Sin conexión a internet.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            }).setIcon(R.drawable.ic_help).show();
            return true;
        }else if(id == R.id.action_sync){
            new AlertDialog.Builder(Inicio.this)
                    .setTitle("Espere...")
                    .setMessage("¿Actualizar datos directo del server (Necesario conexión a internet.)?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(getInternetConnection()){
                                Snackbar.make(drawerLayout, "Descarga de datos iniciado.", Snackbar.LENGTH_SHORT).show();
                                new TaskResync().execute();
                            }
                            else {
                                Snackbar.make(drawerLayout, "Sin conexión a internet.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            }).setIcon(R.drawable.ic_help).show();
            return true;
        }
        else if(id == R.id.action_mensages){
            try {
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment editNameDialogFragment = Mensages.newInstance(BingoC.mUsuarioId,"");
                editNameDialogFragment.show(fm, "mensages");
            }
            catch (Exception e){}
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        LoggerC log=new LoggerC(PlaceholderFragment.class);
        public AppCompatSpinner spSecciones,spCasilla;
        private Sesion sesion;
        private int seccionSeleccionada=0,casillaSeleccionada=0;
        private SwipeRefreshLayout refreshLayout;
        private PostMetod postMetod;
        private View rootView;
        private DatosEO datosEO;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public void Adata(Sesion sesion) {
            this.sesion=sesion;
        }
        public PlaceholderFragment() {}

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Sesion sesion) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.Adata(sesion);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private class TaskSincronizacion extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                postMetod= new PostMetod(sesion);
                datosEO= new DatosEO();
            }

            @Override
            protected String doInBackground(Void... voids) {
                datosEO=sesion.obtenerEstados(BingoC.mUsuarioId);
                String msg="Usted esta al día.";
                int paquete=datosEO.getUsuarioSeccionPaquetes();
                //Evaluar la existencia de paquetes(fragmentos)
                if (paquete>1) {
                    for(int i=1; i<paquete; i++){
                        if(datosEO.getUltimoId()!=0){
                            postMetod.Gone(
                                    SaveMedia.getOutputFile("Usuario"),
                                    BingoC.URL_USUARIOS_PAQUETE,
                                    new String[]{"usuario","pw","imei","longitud","latitud","ultimo_id","id_usuario"},
                                    new String[]{BingoC.mUsuarioNick,BingoC.mUsuarioPassword,BingoC.DEVICE_IMEI,BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE, String.valueOf(datosEO.getUltimoId()), String.valueOf(datosEO.get_id())}
                            );
                            msg=postMetod.Read(
                                    new String[]{BingoC.UsuarioSeccionUpdate,BingoC.DatosSync},
                                    new Component[]{new UsuarioAsignacionEO(),datosEO},
                                    false, PostMetod.SAFE_INSERT
                            );
                            postMetod.Read(
                                    new String[]{BingoC.UsuarioSeccionUpdate,BingoC.DatosSync},
                                    new Component[]{new UsuarioAsignacionEO(),datosEO},
                                    true, PostMetod.SAFE_UPDATE
                            );
                        }
                    }
                    //Llegamos hasta el ultimo elemento de la sincronizacion
                    if(datosEO.getUltimoId()==0){
                        datosEO.setUsuarioSeccionPaquetes(paquete);
                        sesion.UDE(datosEO);
                    }
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                Snackbar.make(rootView, aVoid, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                // Parar la animación del indicador
                if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
            }
        }

        private boolean getInternetConnection(){
            final ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if( wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
            /*Wifi*/
                return true;
            } else if( mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED ){
            /*Mobile 3G*/
                return true;
            } else {
                return false;
            }
        }

        private void setCameraTrackingValues(ConfigEO values){
            BingoC.engine.setDpResolution(values.getResolicionDp());
            BingoC.engine.setMaxRadio(values.getRadMax());
            BingoC.engine.setMinRadio(values.getRadMin());
            BingoC.engine.setMinDistancia(values.getDistMin());
            BingoC.engine.setThresholdEdge(values.getThresEdge());
            BingoC.engine.setThresholdCenter(values.getThresCenter());
            BingoC.engine.setMinArea(300000);
            BingoC.engine.setMaxArea(360000);
            if(values.getResolicionDp()!=BingoC.resolicionDp ||
                    values.getRadMax()!=BingoC.radMax ||
                    values.getRadMin()!=BingoC.radMin ||
                    values.getDistMin()!=BingoC.distMin ||
                    values.getThresEdge()!=BingoC.thresEdge ||
                    values.getThresCenter()!=BingoC.thresCenter
                    ){
                BingoC.LoadNewConfig=true;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView=inflater.inflate(R.layout.fragment_inicio, container, false);
            //sesion= new Sesion(getActivity());
            sesion.obtenerSeccion();
            sesion.obtenerCasillas("",BingoC.mSeccioneId);
            ConfigEO eo=sesion.getConfiguracion();
            setCameraTrackingValues(eo);
                    spSecciones = (AppCompatSpinner ) rootView.findViewById(R.id.spSeccion);
                    spCasilla = (AppCompatSpinner ) rootView.findViewById(R.id.spCasilla);
                    refreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshCamera);
                    refreshLayout.setOnRefreshListener(
                            new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    if(getInternetConnection()){
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Espere...")
                                                .setMessage("¿Actualizar datos directo del server (Necesario conexión a internet.)?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                            new TaskSincronizacion().execute();
                                                    }
                                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(refreshLayout.isRefreshing())refreshLayout.setRefreshing(false);
                                                // do nothing
                                            }
                                        }).setIcon(R.drawable.ic_help).show();
                                    }
                                    else {
                                        if(refreshLayout.isRefreshing())refreshLayout.setRefreshing(false);
                                        Snackbar.make(rootView, "Sin conexión a internet.", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                    llenarComoBox1(BingoC.mSecciones, spSecciones);
                    llenarComoBox2(BingoC.mCasillas, spCasilla);
                    //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.genero, android.R.layout.simple_spinner_dropdown_item);
                    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    //spSecciones.setAdapter(adapter);
                    //spCasilla.setAdapter(adapter);
            return rootView;
        }

        public boolean getSpinnerSelectedItem(List<String> mSecciones,AppCompatSpinner spSecciones){
            String s;
            s= mSecciones.get(spSecciones.getSelectedItemPosition());
            if(s.equals("0")){
               return false;
            }
            return true;
        }

        private void llenarComoBox1(List<String> mSecciones, AppCompatSpinner spSecciones) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, mSecciones);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spSecciones.setAdapter(spinnerArrayAdapter);
            spSecciones.setSelection(0);
            spSecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          if (seccionSeleccionada == position) {
                                                              log.printf("seleccionaste el valor 0");
                                                              return; //do nothing
                                                          } else {
                                                              sesion.obtenerCasillas("",BingoC.mSeccionesId.get(position));
                                                              BingoC.mSeccioneId= BingoC.mSeccionesId.get(position);
                                                              BingoC.mSeccione= BingoC.mSecciones.get(position);
                                                              spCasilla.setAdapter(null);
                                                              llenarComoBox2(BingoC.mCasillas, spCasilla);
                                                          }
                                                          seccionSeleccionada = position;
                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  });
        }

        private void llenarComoBox2(List<String> mSecciones, AppCompatSpinner spSecciones) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, mSecciones);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spSecciones.setAdapter(spinnerArrayAdapter);spSecciones.setSelection(0);
            spSecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (casillaSeleccionada == position) {
                        return; //do nothing
                    } else {
                        BingoC.mCasillaId=BingoC.mCasillasId.get(position);
                        BingoC.mCasilla=BingoC.mCasillas.get(position);
                    }
                    casillaSeleccionada = position;
                    //write your code here
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return placeholderFragment=PlaceholderFragment.newInstance(0, sesion);
                case 1:
                    history=new History();
                    history.Adata(sesion);
                    return history;
                case 2:
                    //configuracion= new Configuracion();
                    return Configuracion.newInstance("",sesion);
                    //return configuracion;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    //Captura
                    return "";
                case 1:
                    //Historial
                    return "";
                case 2:
                    //Configuración
                    return "";
            }
            return null;
        }
    }
}
