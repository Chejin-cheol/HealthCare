package cs.healthCare.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cs.healthCare.bluetooth.BluetoothClient;

public class BluetoothDataService extends Service {
    private BluetoothBinder bluetoothBinder;
    private BluetoothClient client;
    private BluetoothSocket socket;

    byte[] readBuffer;
    private int readBufferPosition = 0;
    private OutputStream outputStream;
    private InputStream inputStream;

    private Thread workerThread ;

    public class BluetoothBinder extends Binder
    {
        public BluetoothDataService getService()
        {
            return  BluetoothDataService.this;
        }
    }

    public  void setClient(BluetoothClient client)
    {
        this.client = client;
    }
    public void setSocket(BluetoothDevice device)
    {
        try
        {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            inputStream =  socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        client.Binded();        // 액티비티에 소캣세팅 알림
        receiveData();
    }

    private void receiveData()
    {
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while( ! Thread.currentThread().isInterrupted() )
                    {
                        try
                        {
                            if(inputStream != null) {
                                int availableBytes = inputStream.available();

                                if (availableBytes > 0) {
                                    byte[] bytes = new byte[availableBytes];
                                    inputStream.read(bytes);
                                    for (int i = 0; i < availableBytes; i++) {
                                        byte tempByte = bytes[i];
                                        if (tempByte == 32) {
                                            byte[] encodedBytes = new byte[readBufferPosition];
                                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                            final String text = new String(encodedBytes, "US-ASCII");
                                            Log.i("답",readBufferPosition+" " + text );
                                            readBufferPosition = 0;
                                        } else {
                                            readBuffer[readBufferPosition++] = tempByte;
                                        }
                                    }
                                }
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        catch(NullPointerException e)
                        {
                        }
                    }
                }
            });
        workerThread.start();
    }

    public void sendData(byte[] data)
    {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disConnect()
    {
        try
        {
            inputStream.close();
            inputStream = null;
            outputStream.close();
            outputStream = null;
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        bluetoothBinder = new BluetoothBinder();
        return bluetoothBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workerThread.interrupt();
        disConnect();
    }
}
