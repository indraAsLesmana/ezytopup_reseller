package com.ezytopup.reseller.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezytopup.reseller.R;
import com.ezytopup.reseller.utility.Helper;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

public class PrintDemo extends Activity {
	Button btnSearch;
	Button btnSendDraw;
	Button btnSend;
	Button btnClose;
	EditText edtContext;
	EditText edtPrint;
	private static final int REQUEST_ENABLE_BT = 2;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final String TAG = "PrintDemo";

	public static void start(Activity caller) {
		Intent intent = new Intent(caller, PrintDemo.class);
		caller.startActivity(intent);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mService = new BluetoothService(this, mHandler);

		if( mService.isAvailable() == false ){
            Toast.makeText(this, R.string.bluetooth_notfound, Toast.LENGTH_LONG).show();
            finish();
		}		
	}

    @Override
    public void onStart() {
    	super.onStart();

		if( mService.isBTopen() == false)
		{
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		try {
			btnSendDraw = (Button) this.findViewById(R.id.btn_test);
			btnSendDraw.setOnClickListener(new ClickEvent());
			btnSearch = (Button) this.findViewById(R.id.btnSearch);
			btnSearch.setOnClickListener(new ClickEvent());
			btnSend = (Button) this.findViewById(R.id.btnSend);
			btnSend.setOnClickListener(new ClickEvent());
			btnClose = (Button) this.findViewById(R.id.btnClose);
			btnClose.setOnClickListener(new ClickEvent());
			edtContext = (EditText) findViewById(R.id.txt_content);
			btnClose.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendDraw.setEnabled(false);
		} catch (Exception ex) {
            Log.e("Bluetooth no ready",ex.getMessage());
		}
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) 
			mService.stop();
		mService = null; 
	}
	String code = "JJ4A1 - L120O - 1IG6S - B0O6S";

	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			if (v == btnSearch) {			
				Intent serverIntent = new Intent(PrintDemo.this,DeviceListActivity.class);
				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
			} else if (v == btnSend) {
                String msg = edtContext.getText().toString();
                if( msg.length() > 0 ){
                    mService.sendMessage(msg+"\n", "GBK");
                }
			} else if (v == btnClose) {
				mService.stop();
			} else if (v == btnSendDraw) {
				/*printImage(); // Header
				byte[] cmd = new byte[3];
				cmd[0] = 0x1b;
				cmd[1] = 0x21;
				mService.write(cmd);
				mService.sendMessage("Jl. Pangeran Jayakarta No. 129 \n" +
						"     Jakarta Pusat - 10730  \n", "GBK");
				cmd[1] = 0x21;
				mService.write(cmd);
				mService.sendMessage("Steam Wallet IDR \n", "GBK");

				cmd[1] = 0x21;
				mService.write(cmd);
				mService.sendMessage("  Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
						"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n", "GBK");

				cmd[2] &= 0xEF;
				mService.write(cmd);
				mService.sendMessage("Your Voucher code is : \n","GBK");
				cmd[2] |= 0x69;
				mService.write(cmd);
				mService.sendMessage(Helper.printTextCenter(code) + "\n", "GBK");*/

				/*Make font reverse
				byte[] cmd = new byte[3];
				cmd[0] = 0x1B;
				cmd[1] = 0x7B;
				cmd[2] = 0x1;*/

				/*
				make 1 line feed (space)
				cmd[0] = 0x0A;
				mService.write(cmd);*/

				/*
				make bold
				cmd[0] = 0x1B;
				cmd[1] = 0x45;
				cmd[2] = 0x1;
				mService.write(cmd);*/

				/*
				Selecting print mode
				 */

				byte[] cmd = new byte[4];
				cmd[0] = 0x1B;
				cmd[1] = 0x21;
				mService.write(cmd);
				mService.sendMessage(getResources().getString(R.string.dummy_text), "GBK");
				/*cmd[2] &= 0xEF;
				mService.write(cmd);
				mService.sendMessage("    Jl Pangeran Keren No 100", "GBK");
				mService.sendMessage("      www.xxxxxxxxx.com", "GBK");
				mService.sendMessage("         021-12345689   ", "GBK");
				cmd[2] |= 0x10;
				mService.write(cmd);
				mService.sendMessage("===============================", "GBK");*/

			}
		}
	}

    /**
     *  Handler BluetoothService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                	Toast.makeText(getApplicationContext(), R.string.connection_success,
                            Toast.LENGTH_SHORT).show();
        			btnClose.setEnabled(true);
        			btnSend.setEnabled(true);
        			btnSendDraw.setEnabled(true);
                    break;
                case BluetoothService.STATE_CONNECTING:
                	Helper.log(TAG,"state connecting", null);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                	Helper.log(TAG,"state none", null);
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:
                Toast.makeText(getApplicationContext(), R.string.printer_connectionlost,
                               Toast.LENGTH_SHORT).show();
    			btnClose.setEnabled(false);
    			btnSend.setEnabled(false);
    			btnSendDraw.setEnabled(false);
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:
            	Toast.makeText(getApplicationContext(), R.string.unbale_toconnect,
                        Toast.LENGTH_SHORT).show();
            	break;
            }
        }
        
    };
        
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
            	Toast.makeText(this, R.string.bluetooth_open, Toast.LENGTH_LONG).show();
            } else {
            	finish();
            }
            break;
        case  REQUEST_CONNECT_DEVICE:
        	if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                con_dev = mService.getDevByMac(address);   
                
                mService.connect(con_dev);
            }
            break;
        }
    }

    @SuppressLint("SdCardPath")
	private void printImage() {
    	byte[] sendData = null;
    	PrintPic pg = new PrintPic();
    	pg.initCanvas(384);     
    	pg.initPaint();
		pg.drawImage(100, 0, "/mnt/sdcard/ezy_for_print.jpg"); //this from internal storage.
		sendData = pg.printDraw();
    	mService.write(sendData);
    }
}
