package com.wsapplication.rpd.wsapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONStringer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    // Web Servisimizdeki Namspace alanı
    private final String _Namspace      =   "http://tempuri.org/";
    // Web Servimizdeki Method ismi
    private final String _MethodName    =   "SG_WebService";
    // Namspace ile Method isminin birleşimi
    private final String _Action        =   "http://tempuri.org/SG_WebService";
    // Web Servisimizin Adresi
    private final String _Url           =   "http://rpdwebservice.azurewebsites.net/WebService1.asmx"; ///??
    private String _ResultValue         =   "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new _WebServiceAsyncTask().execute();
    }


    public class _WebServiceAsyncTask extends AsyncTask<Void,Void,Void>
    {
        // Arkaplan işlemi başlamadan önce çalışacak olan fonksiyonumuz.
        protected void onPreExecute() {
            // Burada kullanıcımıza Yükkleniyor mesajını göstermek için bir ProgressDialog olşturuyoruz.
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show(); // ProgressDialog u gösteriyoruz.
        }
        protected Void doInBackground(Void... voids) {


            SoapObject request = new SoapObject(_Namspace, _MethodName);
            // Web Servisimize gönderilcek parametreleri ekliyoruz.
          //  request.addProperty("s1", _birinci_sayi.toString());
          //  request.addProperty("s2",_birinci_sayi.toString());
            

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.env ="http://schemas.xmlsoap.org/soap/envelope/";

            // WebServisimiz ASP.NET ile hazırlandığı için mutlaka TRUE değerini vermeliyiz.
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            AndroidHttpTransport aht = new AndroidHttpTransport(_Url);
            aht.debug=true;

            try {

                aht.call(_Action,envelope);

                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                // Web servisimizden geri gelen sonucu değişkenimize aktarıyoruz.

                SoapObject body = (SoapObject)envelope.bodyIn;


                _ResultValue=response.toString();
            }catch (Exception e){

                _ResultValue=e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz
            // Sonucu yazdıracağımız TextView'ı tanımlıyoruz ve değerini veriyoruz.
            TextView textView =(TextView)findViewById(R.id.textView);
            textView.setText(_ResultValue.toString());

        }
    }

}
