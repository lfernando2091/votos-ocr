package saganet.mx.com.bingo.startup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;
import saganet.mx.com.bingo.Database.Controler.Sesion;
import saganet.mx.com.bingo.Database.Tablas.CasillaEO;
import saganet.mx.com.bingo.Database.Tablas.HistorialEO;
import saganet.mx.com.bingo.Database.Tablas.SeccionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioAsignacionEO;
import saganet.mx.com.bingo.Database.Tablas.UsuarioEO;
import saganet.mx.com.bingo.R;
import saganet.mx.com.bingo.file.SaveMedia;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.sync.PostMetod;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 22/03/2017.
 */

public class History extends Fragment {
    private Sesion sesion;
    private RecyclerView recyclerView;
    public  ContentAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    //private AppCompatTextView textView;
    public static final String TRANSITION_IMAGE = "AvatarImage";
    public static final String TRANSITION_TITLE= "TitleCard";
    public static final String TRANSITION_DESC= "DescCard";

    public static final String EXTRA_URL = "image";
    public static final String EXTRA_RES = "resultado";
    public static final String EXTRA_CONTES = "contestado";
    public static final String EXTRA_SEC = "seccion";
    public static final String EXTRA_CAS = "casilla";
    @Override
    public void onDestroy() {
        super.onDestroy();
        sesion.close();
    }

    public void Adata(Sesion sesion) {
        this.sesion = sesion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       refreshLayout = (SwipeRefreshLayout) inflater.inflate(
                R.layout.recycler_view, container, false);
        //View root= inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = (RecyclerView)refreshLayout.findViewById(R.id.my_recycler_views);
       //textView=(AppCompatTextView)refreshLayout.findViewById(R.id.noinfo);
       //Load();
       // Obtener el refreshLayout
       //refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefresh);
        // Iniciar la tarea asíncrona al revelar el indicador
       refreshLayout.setOnRefreshListener(
               new SwipeRefreshLayout.OnRefreshListener() {
                   @Override
                   public void onRefresh() {
                       new TaskSincronizacion().execute();
                   }
               }
       );
       new TaskSincronizacion().execute();
       return refreshLayout;
    }

    public void reload(){
        new TaskSincronizacion().execute();
    }

    public class TaskSincronizacion extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Load();
            return "";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
            refresh();
        }
    }

    public void refresh(){
        adapter = new ContentAdapter(BingoC.EOS,History.this,getActivity());
        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    public void AddHistory(HistorialEO historialEO){
        adapter.addCard(historialEO);
    }

    private void Load(){
        //sesion= new Sesion(getActivity());
        sesion.getHistorial();
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

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static  final int LENGTH = 20;
        LoggerC log=new LoggerC(ContentAdapter.class);
        private History context;
        private FragmentActivity typeg;
        private List<HistorialEO> historialEOs;
        private PostMetod postMetod;
        private View view;
        private View itemViewlocal;
        private int itemViewposition;
        private String imgURLLocal;

        public void animateCircularReveal(View view){
            int cX=0, cY=0, sRadius=0, eRadius=Math.max(view.getWidth(),view.getHeight());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view,cX,cY,sRadius,eRadius);
                view.setVisibility(View.VISIBLE);
                animator.start();
            }
        }

        public void animateCircularSupress(final View view, final int lposition){
            int cX=view.getWidth(), cY=view.getHeight(), sRadius=view.getWidth(), eRadius=0;
            Animator animator = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                animator = ViewAnimationUtils.createCircularReveal(view,cX,cY,sRadius,eRadius);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                        historialEOs.remove(lposition);
                        notifyItemRemoved(lposition);
                    }
                });
                animator.start();
            }else {
                view.setVisibility(View.INVISIBLE);
                historialEOs.remove(lposition);
                notifyItemRemoved(lposition);
            }
        }

        public ContentAdapter(List<HistorialEO> historialEOs, History context, FragmentActivity typeg) {
            this.historialEOs=historialEOs;this.context=context;this.typeg=typeg;
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder viewHolder){
            super.onViewAttachedToWindow(viewHolder);
            animateCircularReveal(viewHolder.itemView);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder viewHolder){
            super.onViewDetachedFromWindow(viewHolder);
            viewHolder.itemView.clearAnimation();
        }

        public void addCard(HistorialEO historialEO) {
            BingoC.EOS.add(historialEO);
            ((History)context).doSmoothScroll(getItemCount());
            notifyItemInserted(getItemCount());
        }

        public void deleteCard(View view, int list_position) {
            animateCircularSupress(view, list_position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View v = li.inflate(R.layout.item_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
                try {
                    holder.name.setText(""+historialEOs.get(position).getImgCout() + " elemento(s).");
                    holder.description.setText(historialEOs.get(position).getImgTitle());
                    try {
                        File imgFile = new  File(historialEOs.get(position).getImgUrl());
                        if (imgFile.exists()) {
                            //---Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            //Bitmap thumbnail= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 400, 200);
                            //holder.picture.setImageBitmap(thumbnail);
                            Glide.with(getActivity()).load(imgFile.getAbsolutePath()).thumbnail(0.1f).into(holder.picture);
                        }else {
                            holder.picture.setImageResource(R.drawable.a);
                        }
                    }catch (Exception e){
                        holder.picture.setImageResource(R.drawable.a);
                    }
                }catch (Exception en){
                }
        }

        @Override
        public int getItemCount() {
            if(historialEOs.size()<LENGTH){
                //textView.setVisibility(View.INVISIBLE);
                //recyclerView.setVisibility(View.VISIBLE);
                return  historialEOs.size();
            }
            //if(historialEOs.size()==0){return 1;}
            return LENGTH;
        }

        private class TaskSubirDatos extends AsyncTask<HistorialEO, Void, Integer> {
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
                }
                return status;
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                if(aVoid==200) {
                    Snackbar.make(view, "Elemento subido con exito.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    delFile(imgURLLocal);
                    animateCircularSupress(itemViewlocal,itemViewposition);
                    new TaskSincronizacion().execute();
                }
                else {
                    Snackbar.make(view, "Ocurrio un error en el proceso.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return historialEOs.get(position).get_id();
        }

        //@Override
        //public int getItemViewType(int position) {}

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView picture;
            private TextView name;
            private TextView description;
            public ViewHolder(View v) {
                super(v);
                view=v;
                picture = (ImageView) v.findViewById(R.id.card_image);
                name = (TextView) v.findViewById(R.id.card_title);
                description = (TextView) v.findViewById(R.id.card_text);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showItem();
                    }
                });

                Button button = (Button)v.findViewById(R.id.action_button);
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                       //Snackbar.make(v, "Ver imagen", Snackbar.LENGTH_LONG).show();
                        showItem();
                    }
                });

                ImageButton favoriteImageButton = (ImageButton) v.findViewById(R.id.upload_button);
                favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        itemViewlocal=itemView;
                        itemViewposition= getAdapterPosition();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Espere...")
                                .setMessage("¿Sincronizar solo este elemento?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(getInternetConnection()){
                                            //animateCircularSupress(itemView, getAdapterPosition());
                                            int requestCode = getAdapterPosition();
                                            imgURLLocal=historialEOs.get(requestCode).getImgUrl();
                                            new TaskSubirDatos().execute(historialEOs.get(requestCode));
                                            //sesion.UDE(new HistorialEO(historialEOs.get(requestCode).get_id()), new String[]{Colums.COLUMN_SYNC},new String[]{"1"});
                                            //Snackbar.make(v, "Subido", Snackbar.LENGTH_LONG).show();
                                        }
                                        else {
                                            Snackbar.make(view, "Sin conexión a internet.", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_help).show();
                    }
                });

                ImageButton shareImageButton = (ImageButton) v.findViewById(R.id.delete_button);
                shareImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {
                        final int requestCode = getAdapterPosition();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Espere...")
                                .setMessage("¿Eliminar este registro ahora?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        sesion.UDE(new HistorialEO(historialEOs.get(requestCode).get_id()), new String[]{Colums.COLUMN_DELETED},new String[]{"1"});
                                        delFile(historialEOs.get(requestCode).getImgUrl());
                                        Snackbar.make(v, "Eliminado", Snackbar.LENGTH_LONG).show();
                                        animateCircularSupress(itemView, getAdapterPosition());
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_help).show();
                    }
                });
            }

            private void showItem(){
                Pair<View, String> p1 = Pair.create((View) picture, History.TRANSITION_IMAGE);
                Pair<View, String> p2 = Pair.create((View) name, History.TRANSITION_TITLE);
                Pair<View, String> p3 = Pair.create((View) description, History.TRANSITION_DESC);

                ActivityOptionsCompat options;
                Activity act = (FragmentActivity) typeg;
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, p1, p2, p3);

                int requestCode = getAdapterPosition();
                String url = historialEOs.get(requestCode).getImgUrl();
                String res = String.valueOf(historialEOs.get(requestCode).getImgCout());
                String contes = String.valueOf(historialEOs.get(requestCode).getImgDescription());
                String casilla = String.valueOf(historialEOs.get(requestCode).getIdCasilla());
                String seccion = String.valueOf(historialEOs.get(requestCode).getIdSeccion());
                //TODO ASIGNAR SECCION Y CASILLA
                //int color = historialEOs.get(requestCode).getColorResource();

                Intent transitionIntent = new Intent(typeg, HistorialView.class);
                transitionIntent.putExtra(History.EXTRA_URL, url);
                transitionIntent.putExtra(History.EXTRA_RES, res);
                transitionIntent.putExtra(History.EXTRA_CONTES, contes);
                transitionIntent.putExtra(History.EXTRA_CAS, casilla);
                transitionIntent.putExtra(History.EXTRA_SEC, seccion);
                //transitionIntent.putExtra(SampleMaterialActivity.EXTRA_INITIAL, Character.toString(name.charAt(0)));
                //transitionIntent.putExtra(SampleMaterialActivity.EXTRA_COLOR, color);
                //transitionIntent.putExtra(SampleMaterialActivity.EXTRA_UPDATE, false);
                //transitionIntent.putExtra(SampleMaterialActivity.EXTRA_DELETE, false);
                ((FragmentActivity) typeg).startActivityForResult(transitionIntent, requestCode, options.toBundle());




                //Context context = v.getContext();
                //Intent intent = new Intent(context, HistorialView.class);
                //intent.putExtra(HistorialView.EXTRA_POSITION, getAdapterPosition());
                //context.startActivity(intent);
            }

        }

        private void delFile(String s){
            File file= new File(s);
            if(file.exists()){
                file.delete();
            }
        }
    }
}

