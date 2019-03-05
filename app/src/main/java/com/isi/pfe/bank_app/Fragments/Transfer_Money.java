package com.isi.pfe.bank_app.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.isi.pfe.bank_app.Activities.AccountActivity;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.ProgressGenerator;
import com.isi.pfe.bank_app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class Transfer_Money extends Fragment {
    MaterialEditText inputAmount,inputIBAN;
    LinearProgressButton Send;
    FloatingActionButton Scan;
    String IBAN="",amount="",type="Transfer";
    TextView account_number,balance;
    public static boolean finish = false,success=false;
    Toolbar toolbar;
    private boolean isToolbar = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tabs_transferMoney));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isToolbar = bundle.getBoolean("toolbar", false);
        }
        if(isToolbar) {
            toolbar.setVisibility(View.VISIBLE);
            System.out.println("test");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        else
            toolbar.setVisibility(View.GONE);
        init();
        makeListeners();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_transfer_money, container, false);
        inflate(myInflatedView);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbartransfer);
        toolbar.setVisibility(View.INVISIBLE);
        return myInflatedView;
    }

    //TODO:Init
    public void init(){
        inputIBAN.setHelperTextAlwaysShown(true);
        initCard();
    }
    public void inflate(View myInflatedView) {
        inputAmount = (MaterialEditText) myInflatedView.findViewById(R.id.transferMoney_Amount);
        inputIBAN = (MaterialEditText) myInflatedView.findViewById(R.id.transferMoney_IBAN);
        Send = (LinearProgressButton) myInflatedView.findViewById(R.id.transferMoney_Send);
        Scan = (FloatingActionButton) myInflatedView.findViewById(R.id.transferMoney_FAB);
        account_number = (TextView) myInflatedView.findViewById(R.id.transferMoney_accountCard_NumAccount);
        balance = (TextView) myInflatedView.findViewById(R.id.transferMoney_accountCard_Balance);
    }
    public void initCard() {
        account_number.setText(Account_Management.getAccountNumberString(Account_Management.getAccountNumberString(getContext())));
        balance.setText(Account_Management.balance);
    }
    public void resetInputs(){
        inputAmount.setError(null);
        inputIBAN.setError(null);
      //  inputIBAN.setHelperText(inputAmount.getHelperText());
    }

    //TODO:Listeners
    public void makeListeners() {
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:IBAN 34 Chars
                boolean invalidIBAN = inputIBAN.getText().length()<5;
                boolean invalidAmount = inputAmount.getText().toString().length() == 0;
                if(invalidIBAN)
                    inputIBAN.setError(getString(R.string.transfer_invalidIBAN));
                if(invalidAmount)
                    inputAmount.setError(getString(R.string.transfer_invalidAmount));
                if((!invalidAmount) && (!invalidIBAN))
                    if (Double.parseDouble(inputAmount.getText().toString()) <= 0) {
                        inputAmount.setError(getString(R.string.transfer_minAmount));
                    }else
                        showDialog();
            }
        });
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeScan();
            }
        });
    }
    public void showDialog(){
        //icon : android.R.drawable.ic_dialog_alert
        String text = String.format(getString(R.string.transferConfirmation_msg), inputAmount.getText(), IBAN);
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.transferConfirmation_title))
                .content(text)
                .iconRes(android.R.drawable.ic_dialog_alert)
                .positiveText(getString(R.string.agree))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        simulateProgress1(Send,0);
                        invokeTransferService();
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                    }
                })
                .negativeText(getString(R.string.cancel))
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .show();
    }

    //TODO:Service
    public void invokeTransferService() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_number", Account_Management.getAccountNumberString(getContext()));
        params.put("IBAN", inputIBAN.getText().toString());
        params.put("amount", inputAmount.getText().toString());
        params.put("category", type);
        client.get(getString(R.string.serverIP)+"/WebServices/transfer/doTransfer",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    System.err.println("res "+response);
                    if (obj.getBoolean("status")) {
                        if(obj.getString("info").equals("Success")) {
                            success = true;
                        }else {
                            success = false;
                            if(obj.getString("info").equals("Invalid IBAN"))
                                inputIBAN.setError(getString(R.string.transfer_invalidIBAN));
                            if(obj.getString("info").equals("Insufficient Funds"))
                                inputAmount.setError(getString(R.string.transfer_insFunds));
                        }
                        finish = true;
                    } else {
                        success=false;
                        finish = true;
                        Toast.makeText(getContext(), getString(R.string.error_falseAccount), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (statusCode == 404) {
                    Toast.makeText(getContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //TODO:Functions
    public void getJSON(String result){
       try {
           JSONObject obj = new JSONObject(result);
           IBAN = obj.getString("IBAN");
           amount = obj.getString("Amount");
           type = obj.getString("Type");
       } catch (JSONException e) {
           Toast.makeText(getContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();
       }
       inputIBAN.setText(IBAN);
       inputAmount.setText(amount);
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
                .text(getString(R.string.send));
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
    private void simulateProgress1( final LinearProgressButton button,int iteration) {
        if(!finish) {
            if(iteration<10)
                simulateProgress1(button,++iteration);
            else {
                System.err.println("Error");
            }
        } else {
            int progressColor = R.color.material_blue_grey_50;
            int color = R.color.mb_gray;
            int progressCornerRadius = 4;
            int width = 200;
            int height = 8;
            int duration = 500;

            ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
                @Override
                public void onComplete() {
                    if (success)
                        morphToSuccess(button);
                    else
                        morphToFailure(button);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.unblockTouch();
                            morphToSquare(button);
                            resetInputs();
                        }
                    }, 5000);
                }
            });
            button.blockTouch();
            button.morphToProgress(color, progressColor, progressCornerRadius, width, height, duration);
            generator.start(button);
        }
    }
    //QR Code
    public void makeScan() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity()) {

            @Override
            protected void startActivityForResult(Intent intent, int code) {
                Transfer_Money.this.startActivityForResult(intent, code);
            }
        };
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(getString(R.string.transfer_scanqr));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null)
                Toast.makeText(getContext(), getString(R.string.transfer_scanqrcancel), Toast.LENGTH_LONG).show();
            else
                getJSON(result.getContents());
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

}
