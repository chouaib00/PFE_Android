package com.isi.pfe.bank_app.Classes;

import android.content.Context;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isi.pfe.bank_app.Fragments.my_stats;
import com.isi.pfe.bank_app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


public class stats {
    private static int retry=0;
    private static Context context;
    private static String ip;
    private static MaterialDialog dialog;

//    static float[][] t_in ={{120,150,500,420},{10,450,100,620},{720,150,980,120},{540,950,50,20},
//            {1020,190,740,1420},{1600,10,500,40},{2620,1500,400,720},{190,350,320,720},
//            {1300,350,510,420},{920,160,1500,220},{130,150,540,420},{190,550,500,440}};
//    static float[][] t_out ={{10,1500,50,120},{30,100,70,40},{120,950,50,420},{10,50,1000,1020},
//            {120,150,50,40},{120,10,1000,420},{720,150,500,700},{1200,10,10,0},
//            {30,0,0,420},{0,0,0,420},{10,1500,500,420},{2120,1050,0,420}};
    private static float[][] t_in= new float[][]{};
    private static float[][] t_out= new float[][]{};

    public stats(Context contextt,String ipp) {
        context = contextt;
        ip = ipp;
        dialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.loading))
                .content(context.getString(R.string.wait))
                .progress(true, 0)
                .cancelable(true)
                .show();
        dialog.hide();
        InvokeStatService();
    }

    public static void refreshData() {
        my_stats.usable= false;
        if (t_in.length != my_stats.NUMBER_MONTHS) my_stats.usable= false;
        InvokeStatService();
    }

    public static float getWeekStat_In(int month,int week) {
        return t_in[month][week];
    }
    public static float getWeekStat_Out(int month,int week) {
        return t_out[month][week];
    }
    public static float getMonthStat(int month) {
        float w1,w2,w3,w4,res=0;
        int div=0;
        w1 = getWeekStat_In(month,0) - getWeekStat_Out(month,0);
        w2 = getWeekStat_In(month,1)- getWeekStat_Out(month,1);
        w3 = getWeekStat_In(month,2)- getWeekStat_Out(month,2);
        w4 = getWeekStat_In(month,3)- getWeekStat_Out(month,3);
        if(w1 != 0) {res+=w1;div++;}
        if(w2 != 0) {res+=w2;div++;}
        if(w3 != 0) {res+=w3;div++;}
        if(w4 != 0) {res+=w1;div++;}
        return div>0? res/div:0;
    }

    public static void hide() {
        dialog.hide();
    }

    private static void InvokeStatService() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_number",Account_Management.getAccountNumberString(context));
        params.put("nb_month", String.valueOf(my_stats.NUMBER_MONTHS));
        client.get(ip+"/WebServices/stats/getStats",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.hide();
            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status")){

                        t_in = new float[my_stats.NUMBER_MONTHS][4];
                        t_out = new float[my_stats.NUMBER_MONTHS][4];
                        for (int i = 0; i < my_stats.NUMBER_MONTHS; i++) {
                            JSONObject month = obj.getJSONObject("month_" + i);
                            for(int j =1;j<5;j++) {
                                t_in[i][j-1] = (float) month.getDouble("in_week_"+j);
                                t_out[i][j-1] = (float) month.getDouble("out_week_"+j);
                            }
                        }
                        my_stats.usable= true;
                    }
                    else{
                        Toast.makeText(context, context.getString(R.string.error_falseStats), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, context.getString(R.string.error_JSON), Toast.LENGTH_LONG).show();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                retry++;
                if (retry < 5)
                    InvokeStatService();
                else {
                    if (statusCode == 404) {
                        Toast.makeText(context, context.getString(R.string.error_404), Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(context, context.getString(R.string.error_500), Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Toast.makeText(context, context.getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

}
