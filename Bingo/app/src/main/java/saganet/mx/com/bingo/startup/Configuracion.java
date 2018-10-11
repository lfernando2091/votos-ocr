package saganet.mx.com.bingo.startup;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import saganet.mx.com.bingo.Database.Controler.Sesion;
import saganet.mx.com.bingo.Database.Tablas.ConfigEO;
import saganet.mx.com.bingo.R;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.logger.LoggerTypeC;
import saganet.mx.com.bingo.photo.CameraViewBase;
import saganet.mx.com.bingo.sheet.SheetConfig;
import saganet.mx.com.bingo.sheet.SheetMapC;
import saganet.mx.com.bingo.variables.BingoC;
import saganet.mx.com.bingo.xdata.omr;
import saganet.mx.com.bingo.xdata.omrEngine;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Configuracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Configuracion extends Fragment {
    LoggerC log=new LoggerC(Configuracion.class);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Sesion sesion;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextInputEditText setDpResolution, setMaxRadio, setMinRadio, setMinDistancia, setThresholdEdge, setThresholdCenter;
    private ImageView imageView,imageView2;
    private View linearLayout;
    private SwitchCompat switchCompat, switchLog, switchCutSize;

    //private omrEngine settingomr2=new omrEngine();
    private omr engineomr=new omr();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Configuracion.
     */
    // TODO: Rename and change types and number of parameters
    public static Configuracion newInstance(String param1, Sesion sesion) {
        Configuracion fragment = new Configuracion();
        fragment.Adata(sesion);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void Adata(Sesion sesion) {
        this.sesion=sesion;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //sesion.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=null;
        //sesion= new Sesion(getActivity());
        ConfigEO eo=sesion.getConfiguracion();
        setCameraTrackingValues(eo);
        rootView=inflater.inflate(R.layout.fragment_configuracion, container, false);
        linearLayout= (View) rootView.findViewById(R.id.configView);

        setDpResolution=(TextInputEditText) rootView.findViewById(R.id.campo1);
        setMaxRadio=(TextInputEditText) rootView.findViewById(R.id.campo2);
        setMinRadio=(TextInputEditText) rootView.findViewById(R.id.campo3);
        setMinDistancia=(TextInputEditText) rootView.findViewById(R.id.campo4);
        setThresholdEdge=(TextInputEditText) rootView.findViewById(R.id.campo5);
        setThresholdCenter=(TextInputEditText) rootView.findViewById(R.id.campo6);
        switchCompat= (SwitchCompat) rootView.findViewById(R.id.switchButton);
        switchCompat.setChecked(BingoC.segPlano);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    BingoC.SegundoPlano=true;
                    save(BingoC.SegundoPlano,switchLog.isChecked(),BingoC.ApplyCutSize);
                    Snackbar.make(buttonView,"Los procesos se ejecutaran en segundo plano.",Snackbar.LENGTH_SHORT).show();
                }else {
                    BingoC.SegundoPlano=false;
                    save(BingoC.SegundoPlano,switchLog.isChecked(),BingoC.ApplyCutSize);
                }
            }
        });
        switchLog= (SwitchCompat) rootView.findViewById(R.id.switchLog);
        switchLog.setChecked(BingoC.impLog);
        switchLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LoggerTypeC.LoggerActive= LoggerTypeC.eLoggerTypeActive.Active;
                    save(BingoC.SegundoPlano,isChecked,BingoC.ApplyCutSize);
                    Snackbar.make(buttonView,"Se imprimiran datos en el Log.",Snackbar.LENGTH_SHORT).show();
                }else {
                    LoggerTypeC.LoggerActive= LoggerTypeC.eLoggerTypeActive.Inactive;
                    save(BingoC.SegundoPlano,isChecked,BingoC.ApplyCutSize);
                }
            }
        });

        switchCutSize= (SwitchCompat) rootView.findViewById(R.id.switchCutSize);
        switchCutSize.setChecked(BingoC.recImg);
        switchCutSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    BingoC.ApplyCutSize=true;
                    save(BingoC.SegundoPlano,switchLog.isChecked(),BingoC.ApplyCutSize);
                    Snackbar.make(buttonView,"Cada imagen tomada se recortara.",Snackbar.LENGTH_LONG).show();
                }else {
                    BingoC.ApplyCutSize=false;
                    save(BingoC.SegundoPlano,switchLog.isChecked(),BingoC.ApplyCutSize);
                }
            }
        });

        setDpResolution.setText("" + BingoC.engine.getDpResolution());
        setMaxRadio.setText("" + BingoC.engine.getMaxRadio());
        setMinRadio.setText("" + BingoC.engine.getMinRadio());
        setMinDistancia.setText("" + BingoC.engine.getMinDistancia());
        setThresholdEdge.setText("" + BingoC.engine.getThresholdEdge());
        setThresholdCenter.setText("" + BingoC.engine.getThresholdCenter());

        imageView=(ImageView) rootView.findViewById(R.id.imageAnalize);
        imageView2=(ImageView) rootView.findViewById(R.id.imageAnalize2);

        Button analize = (Button) rootView.findViewById(R.id.btnAnalize);
        analize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(!Validate()){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Espere...")
                            .setIcon(R.drawable.ic_help)
                            .setMessage("¿Establecer esta nueva configuración global?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Snackbar.make(view,"Nueva configuración establecida.",Snackbar.LENGTH_SHORT).show();
                                    BingoC.engine.setDpResolution(Double.valueOf(setDpResolution.getText().toString()));
                                    BingoC.engine.setMaxRadio(Integer.valueOf(setMaxRadio.getText().toString()));
                                    BingoC.engine.setMinRadio(Integer.valueOf(setMinRadio.getText().toString()));
                                    BingoC.engine.setMinDistancia(Double.valueOf(setMinDistancia.getText().toString()));
                                    BingoC.engine.setThresholdEdge(Double.valueOf(setThresholdEdge.getText().toString()));
                                    BingoC.engine.setThresholdCenter(Double.valueOf(setThresholdCenter.getText().toString()));
                                    sesion.UDE(new ConfigEO(1,
                                            BingoC.engine.getDpResolution(),
                                            BingoC.engine.getMaxRadio(),
                                            BingoC.engine.getMinRadio(),
                                            BingoC.engine.getMinDistancia(),
                                            BingoC.engine.getThresholdEdge(),
                                            BingoC.engine.getThresholdCenter(),
                                            BingoC.SegundoPlano,switchLog.isChecked(),BingoC.ApplyCutSize));
                                    BingoC.engine.setCutSize(false);
                                    BingoC.LoadNewConfig=true;
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                            .setIcon(R.drawable.ic_help)
                            .show();
                }
                //settingomr2.setCutSize(true);
                //settingomr2.setColorFound(Negro);
                //imageView2.setImageBitmap(null);
                //imageView2.setImageBitmap(engineomr.getColorCircleTracking(BingoC.bitmap2,settingomr2));
                //imageView.setImageBitmap(null);
                //imageView.setImageBitmap(engineomr.getColorCircleTracking(BingoC.bitmap,BingoC.engine));
            }
        });

        Button imagen = (Button) rootView.findViewById(R.id.btnImage);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Espere...")
                            .setMessage("¿Reiniciar la configuracion a valores por defecto?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Snackbar.make(view,"Valores por defecto establecidos.",Snackbar.LENGTH_SHORT).show();
                                    sesion.UDE(new ConfigEO(1,
                                            BingoC.resolicionDp,
                                            BingoC.radMax,
                                            BingoC.radMin,
                                            BingoC.distMin,
                                            BingoC.thresEdge,
                                            BingoC.thresCenter,
                                            BingoC.segPlano,BingoC.impLog,BingoC.recImg));
                                    setDpResolution.setText("" + BingoC.resolicionDp);
                                    setMaxRadio.setText("" + BingoC.radMax);
                                    setMinRadio.setText("" + BingoC.radMin);
                                    setMinDistancia.setText("" + BingoC.distMin);
                                    setThresholdEdge.setText("" + BingoC.thresEdge);
                                    setThresholdCenter.setText("" + BingoC.thresCenter);
                                    switchCompat.setChecked(BingoC.segPlano);
                                    switchLog.setChecked(BingoC.impLog);
                                    switchCutSize.setChecked(BingoC.recImg);
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                            .setIcon(R.drawable.ic_help)
                            .show();
                }catch (Exception e){

                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void save(boolean SegundoPlano,boolean log, boolean ApplyCutSize){
        sesion.UDE(new ConfigEO(1,
                BingoC.engine.getDpResolution(),
                BingoC.engine.getMaxRadio(),
                BingoC.engine.getMinRadio(),
                BingoC.engine.getMinDistancia(),
                BingoC.engine.getThresholdEdge(),
                BingoC.engine.getThresholdCenter(),
                SegundoPlano,log,ApplyCutSize));
    }

    private boolean Validate(){
        boolean auth=false;
        String nombre= setDpResolution.getText().toString();
        String paterno= setMaxRadio.getText().toString();
        String materno= setMinRadio.getText().toString();
        String calle= setMinDistancia.getText().toString();
        String colonia= setThresholdEdge.getText().toString();
        String numext= setThresholdCenter.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            setDpResolution.setError("Obligatorio");auth=true;
        }if(TextUtils.isEmpty(paterno)){
            setMaxRadio.setError("Obligatorio");auth=true;
        }if(TextUtils.isEmpty(materno)){
            setMinRadio.setError("Obligatorio");auth=true;
        }if(TextUtils.isEmpty(calle)){
            setMinDistancia.setError("Obligatorio");auth=true;
        }if(TextUtils.isEmpty(colonia)){
            setThresholdEdge.setError("Obligatorio");auth=true;
        }if(TextUtils.isEmpty(numext)){
            setThresholdCenter.setError("Obligatorio");auth=true;
        }
            return auth;
    }
}