package com.example.pooreum.phonewear;

import android.app.Activity;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.wearable.MessageApi;
        import com.google.android.gms.wearable.Node;
        import com.google.android.gms.wearable.NodeApi;
        import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,SensorEventListener{

    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";

    private GoogleApiClient mApiClient;

    //private ArrayAdapter<String> mAdapter;

    //private ListView mListView;
    private TextView mTextView;
    private String text;
    private View mView;

    //센서 매니저 및 센서
    private Sensor mHeartRateSensor;
    private SensorManager mSensorManager;

    //센서로 받은 값들
    private float mHeartRateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //화면 항상 켜진채로
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //센서 매니저 및 센서 객체 취득
        mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        init();
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    private void init() {
        /*mListView = (ListView) findViewById( R.id.list_view );

        mAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 );
        mListView.setAdapter( mAdapter );*/

        mTextView = (TextView)findViewById(R.id.value);

                /*if(mHeartRateValue>0) {
                    //폰에 보내지는 문자
                    text = String.valueOf(mHeartRateValue);
                    mAdapter.notifyDataSetChanged();

                    sendMessage(WEAR_MESSAGE_PATH, text);
                }*/
    }

    protected void onResume() {
        super.onResume();
        //센서 이벤트 리스너 등록
        if (mSensorManager != null){
            mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause() {
        super.onPause();
        //센서 이벤트 리스너 등록해제
        if (mSensorManager!=null)
            mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        //값이 변했을 때 데이터 업데이트
        if ((int) event.values[0] > 0) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_HEART_RATE:
                    mHeartRateValue = event.values[0];
                    break;
            }
        }
        //시계에 출력 될 문자
        text = String.valueOf(mHeartRateValue);
        //mAdapter.notifyDataSetChanged();

        sendMessage(WEAR_MESSAGE_PATH, text);

        //텍스트를 뷰에 출력
        if (mTextView != null) {
            mTextView.setText(text);
        }

    }


    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                //메세지의 유형을 결정하는데 사용되는 경로 전송, 바이트
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }

                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }

    @Override
    public void onConnected(Bundle bundle) {
        sendMessage(START_ACTIVITY, "");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

