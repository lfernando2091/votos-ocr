package saganet.mx.com.bingo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import saganet.mx.com.bingo.Database.Controler.Component;
import saganet.mx.com.bingo.Database.Controler.Sesion;
import saganet.mx.com.bingo.Database.Tablas.CasillaEO;
import saganet.mx.com.bingo.Database.Tablas.DatosEO;
import saganet.mx.com.bingo.Database.Tablas.SeccionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioAsignacionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioEO;
import saganet.mx.com.bingo.file.SaveMedia;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.startup.Inicio;
import saganet.mx.com.bingo.sync.PostMetod;
import saganet.mx.com.bingo.variables.BingoC;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    private Sesion sesion;
    private LoggerC log = new LoggerC(LoginActivity.class);
    private String SecurePassword;
    private PostMetod postMetod;
    private DatosEO datosEO;
    private static final int PETICION_RED_LOCALIZACION = 401;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //--private UserLoginTask mAuthTask = null;
    private boolean autoLogin;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressBar progressBar;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginFormView = findViewById(R.id.login_form);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.logingprocess);
        progressBar.setVisibility(View.GONE);
        //populateAutoComplete();
        sesion = new Sesion(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Snackbar.make(mLoginFormView, "Es indispensable proporcinarnos su ubicación, con el fin de realizar estadisticas.", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_RED_LOCALIZACION);
                            }
                        });
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_RED_LOCALIZACION);
            }
        }
        else {
            try {
                Location lastlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                BingoC.DEVICE_LATITUDE= String.valueOf(lastlocation.getLatitude());
                BingoC.DEVICE_LONGITUDE= String.valueOf(lastlocation.getLongitude());
            }catch (Exception e){}
        }
        //BingoC.DEVICE_IMEI = telephonyManager.getDeviceId();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    MostrarInicio(mLoginFormView);
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //--attemptLogin();
                MostrarInicio(view);
            }
        });
        new TaskUsuarios().execute();
    }

    private void MostrarInicio(final View view){
        IrInicio();
        /*if (!mEmailView.getText().toString().equals("") && !mPasswordView.getText().toString().equals("")) {
            SecurePassword=getMD5(mPasswordView.getText().toString());
            if(BingoC.mUsuariosNick.size()!=0 && BingoC.mUsuariosNick.contains(mEmailView.getText().toString())){
                if(BingoC.mUsuariosPassword.contains(SecurePassword)){
                    String realPassword= BingoC.mUsuariosPassword.get(BingoC.mUsuariosNick.indexOf(mEmailView.getText().toString()));
                    if(SecurePassword.equals(realPassword)){
                        int UsuarioIndex=BingoC.mUsuariosNick.indexOf(mEmailView.getText().toString());
                        BingoC.mUsuarioPassword=SecurePassword;
                        BingoC.mUsuarioNick=mEmailView.getText().toString();
                        BingoC.mUsuarioId=BingoC.mUsuariosId.get(UsuarioIndex);
                        BingoC.mUsuarioVersion=BingoC.mUsuariosVersiones.get(UsuarioIndex);
                        mPasswordView.setText("");
                        IrInicio();
                    }else {
                        Snackbar.make(view, "La contraseña no es correcta.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        mPasswordView.setError("La contraseña no es correcta.");mPasswordView.requestFocus();
                    }
                }
                else {
                    Snackbar.make(view, "La contraseña no es la correcta.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mPasswordView.setError("La contraseña no es la correcta.");mPasswordView.requestFocus();
                }
            }
            else if(!autoLogin){
                //Snackbar.make(view, "Usuario no registrado.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                BingoC.mUsuarioNick=mEmailView.getText().toString();*/
                /*Iniciar el proceso de sincronizacion*/
                /*new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Actualizar")
                        .setMessage("¿Sincronizar información del nuevo usuario? (Necesario conexión a internet.)")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(getInternetConnection()){
                                    //Continuar con la sincronizacion en segundo plano
                                    new TaskSincronizacion().execute();
                                } else {
                                    Snackbar.make(view, "Sin conexión a internet.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_help).show();
            }
        }
        else {
            Snackbar.make(view, "Campos vacios ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            if(mPasswordView.getText().toString().equals("")){
                mPasswordView.setError("Password necesario.");
                mPasswordView.requestFocus();
            }
            if(mEmailView.getText().toString().equals("")){
                mEmailView.setError("Usuario necesario.");
                mEmailView.requestFocus();
            }
        }*/
    }

    private void IrInicio(){
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        //saveAll(0,new Component[]{nuevoUsuario(0, "THMOG", "34RYUD","LUIS FERNANDO HERNANDEZ MENDEZ","",""), nuevoSeccion(0, "ZXY001"), nuevoCasilla(0, "ABC001")});
    }

    private String getMD5(String s) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
            return String.valueOf(new BigInteger(1, m.digest()).toString(16));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
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

    /**
    -------------------------------------------------------
     **/

    private class TaskUsuarios extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {super.onPreExecute();}

        @Override
        protected Void doInBackground(Void... voids) {
            sesion.getUser();return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(autoLogin){
                MostrarInicio(mLoginFormView);
                autoLogin=false;
            }
        }
    }

    private class TaskSincronizacion extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMetod= new PostMetod(sesion);
            datosEO= new DatosEO();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String msg="";
            if(postMetod.Gone(
                    SaveMedia.getOutputFile("Usuario"),
                    BingoC.URL_USUARIOS,
                    new String[]{"usuario","pw","imei","longitud","latitud"},
                    new String[]{BingoC.mUsuarioNick,SecurePassword,BingoC.DEVICE_IMEI,BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE}
                )==200){
                msg=postMetod.Read(
                        new String[]{BingoC.UsuarioSeccionUpdate, BingoC.UsuarioUpdate, BingoC.SeccionUpdate, BingoC.CasillaUpdate,BingoC.DatosSync},
                        new Component[]{new UsuarioAsignacionEO(),new UsuarioEO(), new SeccionEO(), new CasillaEO(),datosEO},
                        true, PostMetod.SAFE_INSERT
                );
            }
            int paquete=datosEO.getUsuarioSeccionPaquetes();
            //Evaluar la existencia de paquetes(fragmentos)
            if (paquete>1) {
                for(int i=1; i<paquete; i++){
                    if(datosEO.getUltimoId()!=0){
                        datosEO.setUsuarioSeccionPaquetes(i);
                        if(postMetod.Gone(
                                SaveMedia.getOutputFile("Usuario"),
                                BingoC.URL_USUARIOS_PAQUETE,
                                new String[]{"usuario","pw","imei","longitud","latitud","ultimo_id","id_usuario"},
                                new String[]{BingoC.mUsuarioNick,SecurePassword,BingoC.DEVICE_IMEI,BingoC.DEVICE_LONGITUDE,BingoC.DEVICE_LATITUDE, String.valueOf(datosEO.getUltimoId()), String.valueOf(datosEO.get_id())}
                            )==200) {
                            msg=postMetod.Read(
                                    new String[]{BingoC.UsuarioSeccionUpdate},
                                    new Component[]{new UsuarioAsignacionEO()},
                                    false, PostMetod.SAFE_INSERT
                            );
                            postMetod.Read(
                                    new String[]{BingoC.DatosSync},
                                    new Component[]{datosEO},
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
            Snackbar.make(mLoginFormView, aVoid, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            autoLogin=true;
            progressBar.setVisibility(View.GONE);
            new TaskUsuarios().execute();
        }
    }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PETICION_RED_LOCALIZACION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    @SuppressWarnings("MissingPermission")
                    Location lastlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    BingoC.DEVICE_LATITUDE= String.valueOf(lastlocation.getLatitude());
                    BingoC.DEVICE_LONGITUDE= String.valueOf(lastlocation.getLongitude());
                }catch (Exception e){}
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //if (mAuthTask != null) return;


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            showProgress(false);
        }
    }
}

