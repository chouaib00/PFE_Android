package com.isi.pfe.bank_app.Fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.morphingbutton.MorphingButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Request_Money extends Fragment {

    MaterialEditText amount;
    MorphingButton generateQR;
    TextView account_number,balance;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tabs_requestMoney));
        init();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView =  inflater.inflate(R.layout.fragment_request_money, container, false);
        inflate(myInflatedView);
        return myInflatedView;
    }

    //TODO:Init
    public void init() {
        initCard();
        makeListeners();
    }
    public void inflate(View myInflatedView){
        amount = (MaterialEditText) myInflatedView.findViewById(R.id.requestMoney_Amount);
        generateQR = (MorphingButton) myInflatedView.findViewById(R.id.requestMoney_Generate);
        account_number = (TextView) myInflatedView.findViewById(R.id.requestMoney_accountCard_NumAccount);
        balance = (TextView) myInflatedView.findViewById(R.id.requestMoney_accountCard_Balance);
    }
    public void initCard() {
        account_number.setText(Account_Management.getAccountNumberString(Account_Management.getAccountNumberString(getContext())));
        balance.setText(Account_Management.balance);
    }

    //TODO:Listeners
    public void makeListeners() {
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().length()>0) {
                    double reqAmount = Double.parseDouble(amount.getText().toString());
                    if (reqAmount < 1) {
                        amount.setError(getString(R.string.error_minRequestAmount));
                        morphToFailure(generateQR);
                    } else {
                        amount.setError(null);
                        String[] text2Qr = new String[]{String.valueOf(Account_Management.IBAN), amount.getText().toString(), "Transfer"};
                        String s = constructJSON(text2Qr);
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.QR_CODE, 400, 400);
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            File pictureFile = getOutputMediaFile();
                            storeImage(bitmap,pictureFile);
                            showDialog(bitmap,pictureFile);
                        } catch (WriterException e) {
                            e.printStackTrace();
                            morphToFailure(generateQR);
                        }
                        morphToSuccess(generateQR);
                    }
                }else {
                    amount.setError(getString(R.string.error_minRequestAmount));
                    morphToFailure(generateQR);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        morphToSquare(generateQR);
                    }
                }, 3000);
            }
        });

    }
    public void showDialog(final Bitmap image,final File pictureFile) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertDialog= inflater.inflate(R.layout.qrcode_dialog, null);
        TextView text = (TextView) alertDialog.findViewById(R.id.qrcode_textView);
        String storeTxt = String.format(getString(R.string.requestMoney_stored), pictureFile.toString());
        text.setText(storeTxt);
        ImageView imageView= (ImageView) alertDialog
                .findViewById(R.id.qrcode_imageView);
        imageView.setImageBitmap(image);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700,800);
//        imageView.setLayoutParams(layoutParams);
//        imageView.setImageBitmap(image);

        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());

        alertadd.setView(alertDialog);
        alertadd.setPositiveButton(getString(R.string.share), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri bmpUri = Uri.parse(pictureFile.getAbsolutePath());
                getContext().grantUriPermission(getActivity().getPackageName(),bmpUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                emailIntent1.setType("image/png");
                startActivity(emailIntent1);
            }
        });
        alertadd.setNegativeButton(getString(R.string.cancel), null);
        alertadd.show();
    }

    //TODO:Functions
    private void storeImage(Bitmap image, File pictureFile) {
        if (pictureFile == null) {
            Log.d(getString(R.string.error), getString(R.string.error_storagePermission));// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(getString(R.string.error), getString(R.string.error_fileNotFound) + e.getMessage());
        } catch (IOException e) {
            Log.d(getString(R.string.error), getString(R.string.error_fileAccess) + e.getMessage());
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, pictureFile.getName());
        values.put(MediaStore.Images.Media.DESCRIPTION,"QR Code");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, pictureFile.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, pictureFile.getName().toLowerCase(Locale.US));
        values.put("_data", pictureFile.getAbsolutePath());

        ContentResolver cr = getContext().getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/DCIM/BFI");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        mediaFile.setReadable(true, false);
        return mediaFile;
    }
    public static String constructJSON(String[] data) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("IBAN",data[0]);
            obj.put("Amount",data[1]);
            obj.put("Type",data[2]);
        } catch (JSONException e) {
        }
        return obj.toString();
    }
    //Send button
    private void morphToSquare(final MorphingButton btnMorph) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(RelativeLayout.LayoutParams.MATCH_PARENT)
                .height(RelativeLayout.LayoutParams.WRAP_CONTENT)
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(getString(R.string.requestMoney_generate));
        btnMorph.morph(square);
    }
    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }
    private void morphToFailure(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(RelativeLayout.LayoutParams.WRAP_CONTENT)
                .height(RelativeLayout.LayoutParams.WRAP_CONTENT)
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(android.R.drawable.stat_notify_error);
        btnMorph.morph(circle);
    }
    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }
    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

}
