package saganet.mx.com.bingo.startup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;

import saganet.mx.com.bingo.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreviewImage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewImage extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mButtonCancel;
    private Button mButtonOk;
    private AppCompatImageView image;
    private static Bitmap bitmap;

    public static final String RESULT_OK="OK";
    public static final String RESULT_CANCELED="CANCELED";
    public static final String RESULT_TYPE="PreviewImageResult";
    public static final int REQUEST_INPUT_ID = 2;

    private OnFragmentInteractionListener mListener;
    private static CaptureImage captureImage;

    public PreviewImage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviewImage.
     */
    // TODO: Rename and change types and number of parameters
    public static PreviewImage newInstance(String param1, String param2, Bitmap res, AppCompatActivity captureImage1) {
        PreviewImage fragment = new PreviewImage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        bitmap=res;
        captureImage=(CaptureImage)captureImage1;
        fragment.setArguments(args);
        return fragment;
    }
    public static PreviewImage newInstance(String param1, String param2, Bitmap res) {
        PreviewImage fragment = new PreviewImage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        bitmap=res;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_preview_image, container, false);
        getDialog().setTitle(Html.fromHtml("<font color='#FFFFFF'>Resultado</font>"));
        getDialog().getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        // Title divider
        final View titleDivider = getDialog().findViewById(getResources().getIdentifier("titleDivider", "id", "android"));
        if (titleDivider != null) titleDivider.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image= (AppCompatImageView) view.findViewById(R.id.imageResult);
        image.setImageBitmap(bitmap);
        mButtonCancel = (Button) view.findViewById(R.id.b_cancel_c);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        mButtonOk = (Button) view.findViewById(R.id.b_ok_c);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_TYPE, RESULT_OK);
                captureImage.onActivityResult(getTargetRequestCode(), REQUEST_INPUT_ID, intent);
                captureImage.StopC();
                getDialog().dismiss();
                //showFinishAsk(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private void showFinishAsk(boolean q){
        //getActivity().setResult(q? Activity.RESULT_OK: Activity.RESULT_CANCELED);
        Intent intent = new Intent();
        intent.putExtra(RESULT_TYPE, q? RESULT_OK: RESULT_CANCELED);
        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_INPUT_ID, intent);
        getDialog().dismiss();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
