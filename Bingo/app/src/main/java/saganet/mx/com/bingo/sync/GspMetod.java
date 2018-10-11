package saganet.mx.com.bingo.sync;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 20/04/2017.
 */

class NetworkBase {
    LoggerC log=new LoggerC(NetworkBase.class);
    private LocationManager locationManager;
    private String provider;
    private AppCompatActivity activity;
    private Context context;
    private static final int PETICION_RED_LOCALIZACION = 301;
    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            BingoC.DEVICE_LATITUDE=String.valueOf(location.getLatitude());
            BingoC.DEVICE_LONGITUDE=String.valueOf(location.getLongitude());
            log.printf("DEVICE_LATITUDE:: " + BingoC.DEVICE_LATITUDE);
            log.printf("DEVICE_LONGITUDE:: " + BingoC.DEVICE_LONGITUDE);
            //DataUpload.VALUE_LATITUDE_POSITION = String.valueOf(location.getLongitude());
            //DataUpload.VALUE_LONGITUDE_POSITION = String.valueOf(location.getLatitude());
            //Log.v("LATITUDE_POSITION",DataUpload.VALUE_LATITUDE_POSITION);
            //Log.v("LONGITUDE_POSITION",DataUpload.VALUE_LONGITUDE_POSITION);
            //Toast.makeText(MenuEncuestasContent.this, "LATITUDE_POSITION "+ DataUpload.VALUE_LATITUDE_POSITION , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void BestLocationUnEnabled() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListenerBest);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Encender localización")
                .setMessage("Tu configuración de posicionamiento esta 'Apagado'.\nPor favor encienda la localización " +
                        "para poder continuar")
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public NetworkBase(Context context,final AppCompatActivity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        this.activity = activity;
        this.context=context;
        BestLocationEnabled();
    }

    private void BestLocationEnabled() {
        if (!checkLocation()) return;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_RED_LOCALIZACION);

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 60 * 1000, 10, locationListenerBest);
            //Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
        }
    }
}

class WifiBase implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener  {
    LoggerC log=new LoggerC(WifiBase.class);
    private LocationRequest locRequest;
    public GoogleApiClient apiClient;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private AppCompatActivity activity;
    private Context context;
    private View view;

    public WifiBase(Context context, final AppCompatActivity activity, View view) {
        apiClient= new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        this.activity=activity;
        this.context=context;
        this.view=view;
        locRequest = new LocationRequest();
        locRequest.setInterval(60 * 1000);//Actualizar cada 60s
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locRequest).build();
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(apiClient, locSettingsRequest);
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            //Log.i(LOGTAG, "Se requiere actuación del usuario");
                            log.printf("Necesario peticion del usuario.");
                            status.startResolutionForResult(activity, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            //btnActualizar.setChecked(false);
                            //Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        //btnActualizar.setChecked(false);
                        break;
                }
            }
        });
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.
            //Log.i(LOGTAG, "Inicio de recepción de ubicaciones");
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest,this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(activity.getCurrentFocus(), "Es indispensable proporcinarnos su ubicación, con el fin de realizar estadisticas.", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
                            }
                        });
            }else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
            }
        } else {
            log.printf("tracking iniciado");
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            updateUI(lastLocation);
        }
    }

    public void updateUI(Location loc) {
        if (loc != null) {
            BingoC.DEVICE_LATITUDE= String.valueOf(loc.getLatitude());
            BingoC.DEVICE_LONGITUDE= String.valueOf(loc.getLongitude());
            log.printf("DEVICE_LATITUDE:: " + BingoC.DEVICE_LATITUDE);
            log.printf("DEVICE_LONGITUDE:: " + BingoC.DEVICE_LONGITUDE);
            //Log.v("VALUE_LATITUDE_POS",DataUpload.VALUE_LATITUDE_POSITION);
            //Log.v("VALUE_LONGITUDE_POS",DataUpload.VALUE_LONGITUDE_POSITION);
        } else {
            //DataUpload.VALUE_LATITUDE_POSITION = "0.0";
            //DataUpload.VALUE_LONGITUDE_POSITION = "0.0";
            //Log.v("VALUE_LATITUDE_POS",DataUpload.VALUE_LATITUDE_POSITION);
            //Log.v("VALUE_LONGITUDE_POS",DataUpload.VALUE_LONGITUDE_POSITION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        BingoC.Mensage("La conexion a su posicionamiento se suspendio por "+ (i==1? "servicio desconectado":i==2?"conexión perdida":"razones desconocidas")+".",context);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        BingoC.Mensage("Es importante que active/configure el posicionamiento GPS. Además de permitir que la aplicación obtenga acceso a su ubicación.",context);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);
    }

}

public class GspMetod{
    private WifiBase base;

    private NetworkBase networkBase;

    public GspMetod(Context context, final AppCompatActivity activity, View view, boolean b){
            if(b){
                base= new WifiBase(context,activity, view);
            }else {
                networkBase=new NetworkBase(context,activity);
            }
        }

    public GoogleApiClient getApiClient() {
        return base.apiClient;
    }

    public void setUI(Location loc){
        base.updateUI(loc);
    }

    public void StartLocationUpdates(){
        base.startLocationUpdates();
    }
}
