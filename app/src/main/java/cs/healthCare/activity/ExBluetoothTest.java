package cs.healthCare.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs.healthCare.R;

public class ExBluetoothTest extends AppCompatActivity {
    BluetoothAdapter mbluetoothAdapter; //BluetoothAdapter
    //로컬 블루투스 장치 (=현재 실행중인 안드로이드 장치)

    final static int BLUETOOTH_REQUEST_CODE = 100; //블루투스 요청 액티비티 코드

    //UI
    TextView txtState;
    Button btnSearch;
    CheckBox chkFindme;
    ListView listDevice;
    ListView listParied;

    //Adapter
    SimpleAdapter adapterDevice;
    SimpleAdapter adapterPaired;

    //list - Device 목록 저장
    List<Map<String, String>> dataDevice; //검색된 디바이스 저장
    List<Map<String, String>> dataPaired; //페어링된 기기 저장
    List<BluetoothDevice> bluetoothDevices;

    //통신하고자 하는 원격 장치 (bluetooth 기기, 우리 기준으로는 아두이노 블루투스 ?)
    int selectDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_bt_test); //xml 연결

        //UI
        txtState = (TextView)findViewById(R.id.txtState);
        chkFindme = (CheckBox)findViewById(R.id.chkFindme);
        //btnSearch = (Button)findViewById(R.id.btnSearch);
        listDevice = (ListView)findViewById(R.id.listDevice);
        listParied = (ListView)findViewById(R.id.listPaired);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        //Event
                        mOnBluetoothSearch();

                    }//void onClick
                }//Button.OnClickListenter()
        );//setOnClickListenter

        //Adapter - dataDevice
        dataDevice = new ArrayList<>();
        adapterDevice = new SimpleAdapter(this, dataDevice, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listDevice.setAdapter(adapterDevice);

        //Adapter - dataPaired
        dataPaired = new ArrayList<>();
        adapterPaired = new SimpleAdapter(this, dataPaired, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listParied.setAdapter(adapterPaired);

        //블루투스 지원 유무 확인
        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //선택한 디바이스 없음
        selectDevice = -1;

        //블루투스 미 지원시, null 리턴
        if(mbluetoothAdapter == null){
            Toast.makeText(ExBluetoothTest.this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }//if(블루투스 지원 유무 확인)


        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //Bluethooth.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        registerReceiver(mBluetoothStateReceiver, stateFilter);
        //리시버2
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); ////BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //BluetoothDevice.ACTION_BOND_STATE_CHANGED : 원격장치의 연결 상태가 변경 되었음을 알려준다.
        registerReceiver(mBluetoothSearchReceiver, searchFilter);
        //리시버3
        IntentFilter scanmodeFilter = new IntentFilter();
        scanmodeFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //BluetoothAdapter.ACTION_SCAN_MODE_CHANGED : 블루투스 스캔(검색?) 모드가 변경되었음을 나타낸다.
        registerReceiver(mBluetoothScanmodeReceiver, scanmodeFilter);


        //블루투스가 꺼져있으면 사용자에게 활성화 요청
        if(!mbluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //블루투스 기능 ON 시킴
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }//if(블루투스가 꺼져있다면...)
        else {
            GetListPairedDevice();
        }

        //검색된 디바이스 목록 클릭시 페어링 요청
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bluetoothDevices.get(position);

                try{
                    //선택한 디바이스에 페어링을 요청
                    Method method = device.getClass().getMethod("createBond",(Class[])null);
                } catch(Exception e) {
                    e.printStackTrace(); //예외처리
                }//try catch
            }//void onItemClick
        });//listDevice.setOnItemClickListener
    }//void onCreate


    //블루투스 상태변화 BroadcastReceiver
    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1); //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화

            if(state == BluetoothAdapter.STATE_ON){
                txtState.setText("블루투스 활성화");
            }//블루투스 활성화
            else if(state == BluetoothAdapter.STATE_TURNING_ON){
                txtState.setText("블루투스 활성화 중...");
            }//블루투스 활성화 중
            else if(state == BluetoothAdapter.STATE_OFF){
                txtState.setText("블루투스 비활성화");
            }//블루투스 비활성화
            else if(state == BluetoothAdapter.STATE_TURNING_OFF){
                txtState.setText("블루투스 비활성화 중...");
            }//블루투스 비활성화 중
        }//onReceive
    };//mBluetoothStateReceiver


    //블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED: //bluetooth Adapter가 원격장치 검색을 시작했을 때,
                    dataDevice.clear();
                    if (bluetoothDevices != null){
                        bluetoothDevices.clear();
                    }
                    Toast.makeText(ExBluetoothTest.this, "블루투스 검색 시작", Toast.LENGTH_SHORT).show();
                    btnSearch.setEnabled(true);
                    break;
                 //블루투스 디바이스를 찾음
                case BluetoothDevice.ACTION_FOUND:
                    //검색한 블루투스 디바이스의 객체를 구한다.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //데이터 저장
                    Map map = new HashMap();
                    map.put("name", device.getName()); //블루투스 디바이스의 이름
                    map.put("adress", device.getAddress()); //블루투스 디바이스의 MAC 주소
                    dataDevice.add(map);
                    //리스트 목록 갱신
                    adapterDevice.notifyDataSetChanged();
                    Toast.makeText(ExBluetoothTest.this, "데이터 디바이스 " + dataDevice.size() , Toast.LENGTH_SHORT).show();

                    //블루투스 디바이스 저장
                    bluetoothDevices.add(device);
                    break;
                 //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(ExBluetoothTest.this, "블루투스 검색 종료", Toast.LENGTH_SHORT).show();
                    btnSearch.setEnabled(true);
                    break;
                 //블루투스 디바이스 페어링 상태 변화
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED: //원격장치의 연결 상태가 변경 되었음을 알려줬을 때,
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(paired.getBondState()== BluetoothDevice.BOND_BONDED){ //BluethoothDevice.BOND_BONDED : 원격장치가 페어링 되었음 (cf. BOND_BONDING : 페어링 중)
                        //데이터 저장
                        Map map2 = new HashMap();
                        map2.put("name", paired.getName()); //블루투스 디바이스의 이름
                        map2.put("address", paired.getAddress()); //블루투스 디바이스의 MAC주소
                        dataPaired.add(map2);

                        //리스트 목록갱신
                        adapterPaired.notifyDataSetChanged();

                        //검색된 목록
                        if (selectDevice != -1 ){
                            bluetoothDevices.remove(selectDevice);
                            dataDevice.remove(selectDevice);
                            adapterDevice.notifyDataSetChanged();
                            selectDevice = -1;
                        }//if(선택한 디바이스가 있다면)
                    }//if

                    break;
            }//switch(action)
        }//onReceive
    }; //mBluetoothSearchReceiver


    //블루투스 검색 응답 모드 BroadcastReceiver
    BroadcastReceiver mBluetoothScanmodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            switch (state) {
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                //BluetoothAdapter.SCAN_MODE_CONNECTABLE : 조회(inquiry) 스캔이 비활성화 되어있지만, bluetooth Adapter에서 페이지(page) 스캔이 활성화되어 있음.
                //즉, 원격장치에서 검색 할 수 없지만, 이전에 발견된(연결 경험이 있다면?) 원격장치에서 연결 가능.
                case BluetoothAdapter.SCAN_MODE_NONE:
                //BluetoothAdapter.SCAN_MODE_NONE : Bluetooth Adapter 에서 조회(inquiry) 스캔과 페이지(page) 스캔이 모두 비활성화 되어있음.
                //즉, 원격장치에서 검색이 가능하나 연결 할 수는 없음.
                    chkFindme.setChecked(false);
                    chkFindme.setEnabled(true);
                    Toast.makeText(ExBluetoothTest.this, "검색응답 모드 종료", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Toast.makeText(ExBluetoothTest.this, "다른 블루투스 기기에서 내 퓨대폰을 찾을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }//switch
        }//void onReceive
    }; //mBluetoothScanmodeReceiver


    //블루투스 검색 버튼 클릭
    public void mOnBluetoothSearch(){
        //검색 버튼 비활성화
        btnSearch.setEnabled(false);

        //mbluetoothAdapter.isDiscovering() : 블루투스 검색 중인지 여부 확인
        //mbluetoothAdapter.cancelDiscovery() : 블루투스 검색 취소
        if(mbluetoothAdapter.isDiscovering()) {
            mbluetoothAdapter.cancelDiscovery();
        }//if
        mbluetoothAdapter.startDiscovery(); //블루투스 검색 시작
    }//void mOnBlutoothSearch

    //검색 응답 모드 - 블루투스가 외부 블루투스의 요청에 답변하는 슬레이브 상태
    public void mOnChkFindme(View v){ //검색 응답 체크박스 클릭 함수
        //검색 응답 체크
        if(chkFindme.isChecked()){
            //BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE : 검색 응답 (inquiry) 모드 활성화 + 페이지(page) 모드 활성화
            //BluetoothAdapter.SCAN_MODE_CONNECTABLE : 검색 응답 (inquiry) 모드 비활성화 + 페이지(page) 모드 활성화
            //BluetoothAdapter.SCAN_MODE_NONE : 검색 응답 (inquiry) 모드 비활성화 + 페이지(page) 모드 비활성화
            if(mbluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60); //60초 동안 상대방이 나를 검색할 수 있도록
                startActivity(intent);
            }//if - 검색 응답 모드가 활성화이면, 하지 않는다.
        }//if
    }//void mOnChkFindme

    //이미 페어링된 목록 가져오기
    public void GetListPairedDevice(){
        Set<BluetoothDevice> pairedDevice = mbluetoothAdapter.getBondedDevices();

        dataPaired.clear();
        if(pairedDevice.size() > 0){
            for(BluetoothDevice device : pairedDevice){
                //데이터 저장
                Map map = new HashMap();
                map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                dataPaired.add(map);
            }//for
        }//ir
        //리스트 목록갱신
        adapterPaired.notifyDataSetChanged();
    }//void GetListPairedDevice()


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case BLUETOOTH_REQUEST_CODE:
                //블루투스 활성화 승인
                if(resultCode == Activity.RESULT_OK){
                    GetListPairedDevice();
                }//if
                //블루투스 활성화 거절
                else{
                    Toast.makeText(this, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }//else
                break;
        }//switch
    }//void onACtivityResult

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBluetoothStateReceiver);
        unregisterReceiver(mBluetoothSearchReceiver);
        unregisterReceiver(mBluetoothScanmodeReceiver);
        super.onDestroy();
    }//void onDestroy();


}//ExVBluetoothTest


