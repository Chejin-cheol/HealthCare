package cs.healthCare.bluetooth;

public interface BluetoothClient {
    public void receiveData(String data);
    public void sendData();
    public void Binded();
}
