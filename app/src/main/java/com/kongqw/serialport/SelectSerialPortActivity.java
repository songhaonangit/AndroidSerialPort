package com.kongqw.serialport;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kongqw.serialport.adapter.DeviceAdapter;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;

import java.util.ArrayList;

public class SelectSerialPortActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = SelectSerialPortActivity.class.getSimpleName();
    private DeviceAdapter mDeviceAdapter;
    private Toolbar mTopToolbar;
    private String baud_rate ="9600";
    boolean isClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_serial_port);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        ListView listView = (ListView) findViewById(R.id.lv_devices);


        SerialPortFinder serialPortFinder = new SerialPortFinder();

        ArrayList<Device> devices = serialPortFinder.getDevices();

        if (listView != null) {
            listView.setEmptyView(findViewById(R.id.tv_empty));
            mDeviceAdapter = new DeviceAdapter(getApplicationContext(), devices);
            listView.setAdapter(mDeviceAdapter);
            listView.setOnItemClickListener(this);
        }

        openDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            //Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();
            openDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Device device = mDeviceAdapter.getItem(position);

        Intent intent = new Intent(this, SerialPortActivity.class);
        intent.putExtra(SerialPortActivity.DEVICE, device);
        intent.putExtra(SerialPortActivity.BAUD_RATE,baud_rate);
        startActivity(intent);
    }


    private void openDialog(){

        final String[] baud_rates = {
                "4800", "9600", "19200","38400","57600","115200","230400","460800","500000","576000",
                "921600", "1152000", "1500000", "2000000","2500000","3000000","3500000","4000000"
        };

        final AlertDialog.Builder myDialog =
                new AlertDialog.Builder(this);
        myDialog.setTitle("设置下波特率呗！！！");
       // setCanceledOnTouchOutside(false);
        myDialog.setCancelable(false);
        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, baud_rates);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                //String item = (String)parent.getItemAtPosition(position);
                baud_rate = baud_rates[position];
                isClick = true;
                Toast.makeText(getApplicationContext(),
                        baud_rate, Toast.LENGTH_LONG).show();
            }});
        myDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isClick){
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "要选择波特率哈,不选就默认9600喽", Toast.LENGTH_LONG).show();
                    isClick = false;
                }

            }
        });

        myDialog.setView(listView);
        myDialog.show();
    }



}
