package saganet.mx.com.bingo.startup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import saganet.mx.com.bingo.Database.Controler.Sesion;
import saganet.mx.com.bingo.R;
import saganet.mx.com.bingo.variables.BingoC;

public class Mensages extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Sesion sesion;

    // TODO: Rename and change types of parameters
    private String mUsuario;
    private String mParam2;
    private AppCompatTextView[] registro = new AppCompatTextView[12];

    public Mensages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mensages.
     */
    // TODO: Rename and change types and number of parameters
    public static Mensages newInstance(String param1, String param2) {
        Mensages fragment = new Mensages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsuario = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mensages, container, false);
        // Title text
        getDialog().setTitle(Html.fromHtml("<font color='#FFFFFF'>Estados Sincronización</font>"));
        getDialog().getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabEstados);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view,"Hola mundo", Snackbar.LENGTH_SHORT).show();
                dismiss();
            }
        });
        // Title divider
        final View titleDivider = getDialog().findViewById(getResources().getIdentifier("titleDivider", "id", "android"));
        if (titleDivider != null) titleDivider.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sesion = new Sesion(getActivity());
        registro[0]=(AppCompatTextView)view.findViewById(R.id.msgcontent2);
        registro[1]=(AppCompatTextView)view.findViewById(R.id.msgcontent4);
        registro[2]=(AppCompatTextView)view.findViewById(R.id.msgcontent6);
        registro[3]=(AppCompatTextView)view.findViewById(R.id.msgcontent8);
        registro[4]=(AppCompatTextView)view.findViewById(R.id.msgcontent10);
        registro[5]=(AppCompatTextView)view.findViewById(R.id.msgcontent12);
        registro[6]=(AppCompatTextView)view.findViewById(R.id.msgcontent14);
        registro[7]=(AppCompatTextView)view.findViewById(R.id.msgcontent16);
        registro[8]=(AppCompatTextView)view.findViewById(R.id.msgcontent18);
        registro[9]=(AppCompatTextView)view.findViewById(R.id.msgcontent20);
        registro[10]=(AppCompatTextView)view.findViewById(R.id.msgcontent22);
        registro[11]=(AppCompatTextView)view.findViewById(R.id.msgcontent24);

        new TaskUsuarios().execute();
        // Inflate the layout for this fragment
        return view;
    }

    private class TaskUsuarios extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {super.onPreExecute();}

        @Override
        protected Void doInBackground(Void... voids) {
            sesion.obtenerEstados(mUsuario);return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int msg=-1;
            registro[0].setText(String.valueOf(BingoC.CONTAR_SECCIONES_ESPERADOS));
            registro[1].setText(String.valueOf(BingoC.CONTAR_SECCIONES_RECIVIDOS));
            registro[2].setText(String.valueOf(msg=(BingoC.CONTAR_SECCIONES_ESPERADOS-BingoC.CONTAR_SECCIONES_RECIVIDOS)));
            registro[3].setText(msg==0?"Datos completos.":"Faltan registros.");

            registro[4].setText(String.valueOf(BingoC.CONTAR_CASILLAS_ESPERADOS));
            registro[5].setText(String.valueOf(BingoC.CONTAR_CASILLAS_RECIVIDOS));
            registro[6].setText(String.valueOf(msg=(BingoC.CONTAR_CASILLAS_ESPERADOS-BingoC.CONTAR_CASILLAS_RECIVIDOS)));
            registro[7].setText(msg==0?"Datos completos.":"Faltan registros.");

            registro[8].setText(String.valueOf(BingoC.CONTAR_USUARIOS_ASIGNACION_ESPERADOS));
            registro[9].setText(String.valueOf(BingoC.CONTAR_USUARIOS_ASIGNACION_RECIVIDOS));
            registro[10].setText(String.valueOf(msg=(BingoC.CONTAR_USUARIOS_ASIGNACION_ESPERADOS-BingoC.CONTAR_USUARIOS_ASIGNACION_RECIVIDOS)));
            registro[11].setText(msg==0?"Datos completos.":"Faltan registros.");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
