package com.koteyka.firelampwifi;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.devadvance.circularseekbar.CircularSeekBar;
import java.io.IOError;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    View ID;
    final Handler handler = new Handler();
    String OutputMess = "GET", GlobalAddress, GlobalPort, Res, ValStrSp, Chang;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonBackSet, Search, btnInterval, btnRasbros, buttonBack, buttonBackFav, buttonSetTimer,
            buttonBackTimet, buttonChangeConnect;
    TextView TextReceived1, TextReceived2, TextReceived3, TextReceived4, TextReceived5, Text_Speed, Text_Scale, Text_Bright, AlarmTime1, AlarmTime2, AlarmTime3,
            AlarmTime4, AlarmTime5, AlarmTime6, AlarmTime7, Text_SCALE, Text_SPEED, textViewTimerOff, textViewTomerOstOff;
    UDPHelper udp;
    Pinger PING = new Pinger();
    boolean running, itsFromESP = true, closed, TestConnect, GET_Sendet, TimerON;

    UdpClientThread udpClientThread;
    public String DataMess;
    MyTask mt;
    TestTask TT;
    private MalibuCountDownTimer countDownTimer;
    private TimerCountDownTimer TimerOff;
    private PingerCountDownTimer PingerTimer;
    LinearLayout SettingsLayer;
    FrameLayout AlarmLayout, MainLayout, FavsLayout, TimerLayout;
    ScrollView ScrollVievEffect;
    SharedPreferences AddressPortFile;
    CircularSeekBar SeekSpeed, SeekBright, SeekScale;
    Spinner spinner, spinnerA;
    ImageView CHSV, StatConnect;
    int progressSp; // Прогресс Скорости
    int progressBr; // Прогресс Яркости
    int progressSc; // Прогресс Масштаба
    int Position, ValIntSp, Rinterval, Rrasbros, StatButtonOn, sch = 0, StartPosition, Rtimer;
    Switch switchON, switchMODE, switchSost, AlarmOn1, AlarmOn2, AlarmOn3, AlarmOn4, AlarmOn5, AlarmOn6, AlarmOn7;
    RadioGroup radioGroupInterval, radioGroupRasbros, radioGroupTimer;
    // ********* Будильник *******************
    Calendar dateAndTime = Calendar.getInstance();
    // ***************************************
    CheckBox CheckEffect1,CheckEffect2,CheckEffect3,CheckEffect4,CheckEffect5,CheckEffect6,CheckEffect7,
            CheckEffect8,CheckEffect9,CheckEffect10,CheckEffect11,CheckEffect12,CheckEffect13,CheckEffect14,
            CheckEffect15,CheckEffect16,CheckEffect17,CheckEffect18,CheckEffect19,CheckEffect20,CheckEffect21,
            CheckEffect22,CheckEffect23,CheckEffect24,CheckEffect25,CheckEffect26, SelectAll;
    long startTime = 500 * 1000;
    long interval = 2 * 1000;
    long StartTimer;
    SimpleDateFormat simpleDateFormat;
    String AP = ""; int Cnt = 0;

    @SuppressLint({"ClickableViewAccessibility", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckEffect1 = findViewById(R.id.checkBox1); CheckEffect2 = findViewById(R.id.checkBox2); CheckEffect3 = findViewById(R.id.checkBox3);
        CheckEffect4 = findViewById(R.id.checkBox4); CheckEffect5 = findViewById(R.id.checkBox5); CheckEffect6 = findViewById(R.id.checkBox6);
        CheckEffect7 = findViewById(R.id.checkBox7); CheckEffect8 = findViewById(R.id.checkBox8); CheckEffect9 = findViewById(R.id.checkBox9);
        CheckEffect10 = findViewById(R.id.checkBox10); CheckEffect11 = findViewById(R.id.checkBox11); CheckEffect12 = findViewById(R.id.checkBox12);
        CheckEffect13 = findViewById(R.id.checkBox13); CheckEffect14 = findViewById(R.id.checkBox14); CheckEffect15 = findViewById(R.id.checkBox15);
        CheckEffect16 = findViewById(R.id.checkBox16); CheckEffect17 = findViewById(R.id.checkBox17); CheckEffect18 = findViewById(R.id.checkBox18);
        CheckEffect19 = findViewById(R.id.checkBox19); CheckEffect20 = findViewById(R.id.checkBox20); CheckEffect21 = findViewById(R.id.checkBox21);
        CheckEffect22 = findViewById(R.id.checkBox22); CheckEffect23 = findViewById(R.id.checkBox23); CheckEffect24 = findViewById(R.id.checkBox24);
        CheckEffect25 = findViewById(R.id.checkBox25); CheckEffect26 = findViewById(R.id.checkBox26);
        SelectAll = findViewById(R.id.checkBoxSelectAll);

        countDownTimer = new MalibuCountDownTimer(startTime, interval);
        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);
        buttonConnect = findViewById(R.id.connect);
        buttonBackSet = findViewById(R.id.buttonBackSett);
        btnInterval = findViewById(R.id.buttonInterval);
        btnRasbros = findViewById(R.id.buttonRasbros);
        buttonBackFav = findViewById(R.id.buttonBackFav);
        buttonSetTimer = findViewById(R.id.buttonSetTimer);
        buttonBackTimet = findViewById(R.id.buttonBackTimet);
        buttonChangeConnect = findViewById(R.id.buttonChangeConnect);
        textViewTimerOff = findViewById(R.id.textViewTimerOff);
        textViewTomerOstOff = findViewById(R.id.textViewTomerOstOff);
        Text_SPEED = findViewById(R.id.t_Spedd);
        Text_SCALE = findViewById(R.id.t_Scale);
        Text_Speed = findViewById(R.id.textViewSp);    // Значение Сикбара скорости
        Text_Bright = findViewById(R.id.textView_Br);  // Значение Сикбара яркости
        Text_Scale = findViewById(R.id.textViewSc);    // Значение Сикбара масштаба
        TextReceived1 = findViewById(R.id.TextReceived1);
        TextReceived2 = findViewById(R.id.TextReceived2);
        TextReceived3 = findViewById(R.id.TextReceived3);
        TextReceived4 = findViewById(R.id.TextReceived4);
        TextReceived5 = findViewById(R.id.TextReceived5);
        Search = findViewById(R.id.Search);
        SeekSpeed = findViewById(R.id.seekBarSpeed);
        SeekBright = findViewById(R.id.seekBarBrightness);
        SeekScale = findViewById(R.id.seekBarScale);
        spinner = findViewById(R.id.spinnerMode);
        spinnerA = findViewById(R.id.spinnerAlarm);
        CHSV = findViewById(R.id.imageViewCHSV);
        switchON = findViewById(R.id.swtch_ON);
        switchMODE = findViewById(R.id.swtch_MODE);
        switchSost = findViewById(R.id.switchSost);
        StatConnect = findViewById(R.id.imageViewWIFI);
        AlarmLayout = findViewById(R.id.LayAlarm);
        MainLayout = findViewById(R.id.ButPanel);
        FavsLayout = findViewById(R.id.LayFavs);
        SettingsLayer = findViewById(R.id.SettingsLayer);
        TimerLayout = findViewById(R.id.TimerLayer);
        ScrollVievEffect = findViewById(R.id.ScrolViewEffects);
        radioGroupInterval = findViewById(R.id.RadioGroupInterval);
        radioGroupRasbros = findViewById(R.id.RadioGroupRasbros);
        radioGroupTimer = findViewById(R.id.radioGroupTimer);
        // ********* Будильник *******************
        buttonBack = findViewById(R.id.ButtonBack);
        AlarmTime1 = findViewById(R.id.TextView1);
        AlarmTime2 = findViewById(R.id.TextView2);
        AlarmTime3 = findViewById(R.id.TextView3);
        AlarmTime4 = findViewById(R.id.TextView4);
        AlarmTime5 = findViewById(R.id.TextView5);
        AlarmTime6 = findViewById(R.id.TextView6);
        AlarmTime7 = findViewById(R.id.TextView7);
        AlarmOn1 = findViewById(R.id.switch1);
        AlarmOn2 = findViewById(R.id.switch2);
        AlarmOn3 = findViewById(R.id.switch3);
        AlarmOn4 = findViewById(R.id.switch4);
        AlarmOn5 = findViewById(R.id.switch5);
        AlarmOn6 = findViewById(R.id.switch6);
        AlarmOn7 = findViewById(R.id.switch7);
        // ***************************************
        Toast.makeText(getApplicationContext(), "Соединяемся...", Toast.LENGTH_SHORT).show();
        AddressPortFile = this.getSharedPreferences("AddressPortFile", Context.MODE_PRIVATE);
        GlobalAddress = AddressPortFile.getString("Address", "");
        GlobalPort = AddressPortFile.getString("Port", "");
        editTextAddress.setText(GlobalAddress);
        editTextPort.setText(GlobalPort);
        StartPosition = 0;
        countDownTimer.start();
        final RadioButton Radio10 = findViewById(R.id.radioButton10);
        final RadioButton Radio11 = findViewById(R.id.radioButton11);


        simpleDateFormat = new SimpleDateFormat("mm:ss");

        switchON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchON.isChecked()) { OutputMess = "P_ON"; SendData(); } else {
                    OutputMess = "P_OFF";
                    SendData();
                }
            }
        });
        switchMODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Radio10.isChecked()) {
                    Radio10.setChecked(false);
                    Radio11.setChecked(true);
                }
                CreateStringFav();
            }
        });

        switchSost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateStringFav();
            }
        });

        RadioButton CheckedButtonInterval = findViewById(radioGroupInterval.getCheckedRadioButtonId());
        btnInterval.setText(CheckedButtonInterval.getText());
        RadioButton CheckedButtonRasbros = findViewById(radioGroupRasbros.getCheckedRadioButtonId());
        btnRasbros.setText(CheckedButtonRasbros.getText());
        RadioButton CheckButtonTimer = findViewById(radioGroupTimer.getCheckedRadioButtonId());
        buttonSetTimer.setText(CheckButtonTimer.getText());
        //                                                                                          Кнопка Интервал
        btnInterval.setOnClickListener(new View.OnClickListener() {                                 // Кнопка Интервал
            @Override
            public void onClick(View view) {
                itsFromESP = false;
                ScrollVievEffect.setVisibility(View.INVISIBLE);
                radioGroupInterval.setVisibility(View.VISIBLE);
            }
        });
        //                                                                                          Кнопка Разброс
        btnRasbros.setOnClickListener(new View.OnClickListener() {                                  // Кнопка расброс
            @Override
            public void onClick(View view) {
                itsFromESP = false;
                ScrollVievEffect.setVisibility(View.INVISIBLE);
                radioGroupRasbros.setVisibility(View.VISIBLE);
            }
        });
        //                                                                                          radioGroup Интервал
        radioGroupInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        btnInterval.setText("1 минута");
                        Rinterval = 60;
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton2:
                        Rinterval = 60 * 2;
                        btnInterval.setText("2 минуты");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton3:
                        Rinterval = 60 * 3;
                        btnInterval.setText("3 минуты");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton4:
                        Rinterval = 60 * 5;
                        btnInterval.setText("5 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton5:
                        Rinterval = 60 * 10;
                        btnInterval.setText("10 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton6:
                        Rinterval = 60 * 15;
                        btnInterval.setText("15 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton7:
                        Rinterval = 60 * 20;
                        btnInterval.setText("20 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton8:
                        Rinterval = 60 * 30;
                        btnInterval.setText("30 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton9:
                        Rinterval = 60 * 60;
                        btnInterval.setText("60 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    default:
                        break;
                }

            }
        });
        //                                                                                          radioGroup Разброс
        radioGroupRasbros.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton10:
                        Rrasbros = 0;
                        btnRasbros.setText("0 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton11:
                        Rrasbros = 60;
                        btnRasbros.setText("1 минута");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton12:
                        Rrasbros = 60 * 2;
                        btnRasbros.setText("2 минуты");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton13:
                        Rrasbros = 60 * 3;
                        btnRasbros.setText("3 минуты");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton14:
                        Rrasbros = 60 * 5;
                        btnRasbros.setText("5 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton15:
                        Rrasbros = 60 * 10;
                        btnRasbros.setText("10 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton16:
                        Rrasbros = 60 * 15;
                        btnRasbros.setText("15 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton17:
                        Rrasbros = 60 * 20;
                        btnRasbros.setText("20 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;
                    case R.id.radioButton18:
                        Rrasbros = 60 * 30;
                        btnRasbros.setText("30 минут");
                        ScrollVievEffect.setVisibility(View.VISIBLE);
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {CreateStringFav();}
                        break;

                    default:
                        break;
                }

            }
        });
        //                                                                                          radioGroup Таймер
        radioGroupTimer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton19 :
                        buttonSetTimer.setText("Не выключать");
                        TimerON = false;
                        TimerOff.cancel();
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) {OutputMess = "TMR_SET 0 0 0"; SendData();}
                        break;
                    case R.id.radioButton20 :
                        buttonSetTimer.setText("1 минуту");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 60;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 1 60"; SendData();}
                        break;
                    case R.id.radioButton21 :
                        buttonSetTimer.setText("5 минут");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 300;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 2 300"; SendData();}
                        break;
                    case R.id.radioButton22 :
                        buttonSetTimer.setText("15 минут");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 900;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 3 900"; SendData();}
                        break;
                    case R.id.radioButton23 :
                        buttonSetTimer.setText("30 минут");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 1800;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 4 1800"; SendData();}
                        break;
                    case R.id.radioButton24 :
                        buttonSetTimer.setText("45 минут");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 2700;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 5 2700"; SendData();}
                        break;
                    case R.id.radioButton25 :
                        buttonSetTimer.setText("60 минут");
                        TimerON = true;
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                        if(!itsFromESP) StartTimer = 3600;
                        TimerOff = new TimerCountDownTimer(StartTimer*1000, 1000);
                        TimerOff.start();
                        if(!itsFromESP) {OutputMess = "TMR_SET 1 6 3600"; SendData();}
                        break;
                }
            }
        });
        //                                                                                          Кнопка Установки таймера
        buttonSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTimer = 0;
                if (TimerOff != null) {
                    TimerOff.cancel();
                }
                radioGroupTimer.setVisibility(View.VISIBLE);
                itsFromESP = false;
            }
        });
        //                                                                                          Кнопка Назад в таймере
        buttonBackTimet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerLayout.setVisibility(View.INVISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
                countDownTimer.start();
            }
        });

        // ==================================  Выпадающий список Эффекты ===========================
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                countDownTimer.cancel();
                itsFromESP = false;
                return false;
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerMode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
                //                                                                                  Огонь и Цвет
                if (position == 1 || position == 16) {
                    Text_SCALE.setText("ОТТЕНОК");
                    CHSV.setVisibility(View.VISIBLE);
                } else {
                    Text_SCALE.setText("МАСШТАБ");
                    CHSV.setVisibility(View.INVISIBLE);
                }
                //                                                                                  Радуги горизонтальная и вертикальная
                if (position == 3 || position == 4 || position == 5) {
                    SeekScale.setMax(50);
                } else {
                    SeekScale.setMax(100);
                }
                //                                                                                  Смена цвета
                if (position == 6) {
                    SeekScale.setProgress(1);
                    OutputMess = "SCA1";
                    SendData();
                    SeekScale.setEnabled(false);
                    Text_SCALE.setText("НЕ ИСП.");
                } else {
                    SeekScale.setEnabled(true);
                }
                //                                                                                  Если огонь или смена цвета, то Макс значение 255
                if ((position > 0 && position < 6) || (position > 6 && position < 25)) {
                    SeekSpeed.setMax(100);
                } else {
                    SeekSpeed.setMax(255);
                }
                //                                                                                  В эффектах цвет СКОРОСТЬ не используется
                if (position == 16) {
                    SeekSpeed.setEnabled(false);
                    Text_SPEED.setText("НЕ ИСП.");
                } else {
                    SeekSpeed.setEnabled(true);
                    Text_SPEED.setText("СКОРОСТЬ");
                }
                if(position == 25) {
                    Text_SPEED.setText("ХОЛОД/ТЕПЛО");
                }

                OutputMess = "EFF" + position;
                if(!itsFromESP) {
                    SendData();
                    countDownTimer.start();
                }
                Position = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        // ==============================  Сикбары  ================================================
        //                                                                                          Скорость
        SeekSpeed.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {
                progressSp = Math.round(progress);
                if (progressSp == 0) {
                    progressSp = 1;
                }
                Text_Speed.setText(progressSp + "");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Text_Speed.setText(progressSp + "");
                int OverProgress = progressSp;
                if ((Position >= 0 && Position < 7) || (Position > 15)) {                           // У 3D эффектов скорость работает наоборот
                    OverProgress = Math.round(SeekSpeed.getMax()) - progressSp;
                }
                OutputMess = "SPD" + OverProgress;
                SendData();
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        //                                                                                          Яркость
        SeekBright.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {
                progressBr = Math.round(progress);
                if (progressBr == 0) {
                    progressBr = 1;
                }
                Text_Bright.setText(progressBr + "");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Text_Bright.setText(progressBr + "");
                OutputMess = "BRI" + progressBr;
                SendData();
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        //                                                                                          Масштаб
        SeekScale.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {
                progressSc = Math.round(progress);
                if (progressSc == 0) {
                    progressSc = 1;
                }
                Text_Scale.setText(progressSc + "");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Text_Scale.setText(progressSc + "");
                int OverProgress = Math.round(SeekSpeed.getMax()) - progressSc;
                if ((Position >= 0 && Position < 7) || (Position > 15)) {                           // У 3D эффектов скорость работает наоборот
                    OverProgress = progressSc;
                }
                OutputMess = "SCA" + OverProgress;
                SendData();
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        // ==============================  Сикбары  END ============================================
        //                                                                                          Кнопка НАЙТИ в настройках соединения
        Search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countDownTimer.cancel();
                TextReceived1.setText("");
                TextReceived2.setText("");
                TextReceived3.setText("");
                TextReceived4.setText("");
                TextReceived5.setText("");
                editTextAddress.setText("");
                editTextPort.setText("");
                AP = ""; Cnt = 0;
                Toast.makeText(getApplicationContext(), "Идет поиск...", Toast.LENGTH_LONG).show();
                running = true;
                closed = false;
                PingerTimer = new PingerCountDownTimer(5000, 500);
                PingerTimer.start();
            }
        });
        //                                                                                          Кнопка Проверить соединение в настройках соединения
        buttonChangeConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                ChangeConnect();
            }
        });
        //                                                                                          Кнопка Сохранить и закрыть в настройках соединения
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countDownTimer.start();
                SaveAddressAndPort(editTextAddress.getText().toString(), editTextPort.getText().toString());
                TimerLayout.setVisibility(View.INVISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
            }
        });
        //                                                                                          Кнопка Назад в настройках соединения
        buttonBackSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsLayer.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                countDownTimer.start();
            }
        });

        // ============================== БУДИЛЬНИК ================================================

        // ==================================  Выпадающий список время до рассвета =================
        spinnerA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                countDownTimer.cancel();
                itsFromESP = false;
                return false;
            }
        });
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this, R.array.spinnerAlarm, android.R.layout.simple_spinner_item);
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerA.setAdapter(adapterA);
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
                if(position != 0) {
                    OutputMess = "DAWN" + position;
                    if(!itsFromESP){SendData();}
                    countDownTimer.start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //                                                                                          Кнопка назад в будильниках
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerLayout.setVisibility(View.INVISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
                countDownTimer.start();
            }
        });
        //                                                                                          Выключатели будильников
        AlarmOn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn1.isChecked()) {OutputMess = "ALM_SET1 ON";} else {OutputMess = "ALM_SET1 OFF";}
                SendData();
            }
        });
        AlarmOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn2.isChecked()) {OutputMess = "ALM_SET2 ON";} else {OutputMess = "ALM_SET2 OFF";}
                SendData();
            }
        });
        AlarmOn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn3.isChecked()) {OutputMess = "ALM_SET3 ON";} else {OutputMess = "ALM_SET3 OFF";}
                SendData();
            }
        });
        AlarmOn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn4.isChecked()) {OutputMess = "ALM_SET4 ON";} else {OutputMess = "ALM_SET4 OFF";}
                SendData();
            }
        });
        AlarmOn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn5.isChecked()) {OutputMess = "ALM_SET5 ON";} else {OutputMess = "ALM_SET5 OFF";}
                SendData();
            }
        });
        AlarmOn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn6.isChecked()) {OutputMess = "ALM_SET6 ON";} else {OutputMess = "ALM_SET6 OFF";}
                SendData();
            }
        });
        AlarmOn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlarmOn7.isChecked()) {OutputMess = "ALM_SET7 ON";} else {OutputMess = "ALM_SET7 OFF";}
                SendData();
            }
        });
        // ============================= БУДИЛЬНИК END =============================================
        //                                                                                          Избранное
        // =========================== FAVORITES ===================================================
        CheckEffect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });
        CheckEffect26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { CreateStringFav(); }
        });

        SelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectAll.isChecked()) {
                    SelectAll.setText("ОТМЕНИТЬ ВЫБОР");
                    CheckEffect1.setChecked(true);
                    CheckEffect2.setChecked(true);
                    CheckEffect3.setChecked(true);
                    CheckEffect4.setChecked(true);
                    CheckEffect5.setChecked(true);
                    CheckEffect6.setChecked(true);
                    CheckEffect7.setChecked(true);
                    CheckEffect8.setChecked(true);
                    CheckEffect9.setChecked(true);
                    CheckEffect10.setChecked(true);
                    CheckEffect11.setChecked(true);
                    CheckEffect12.setChecked(true);
                    CheckEffect13.setChecked(true);
                    CheckEffect14.setChecked(true);
                    CheckEffect15.setChecked(true);
                    CheckEffect16.setChecked(true);
                    CheckEffect17.setChecked(true);
                    CheckEffect18.setChecked(true);
                    CheckEffect19.setChecked(true);
                    CheckEffect20.setChecked(true);
                    CheckEffect21.setChecked(true);
                    CheckEffect22.setChecked(true);
                    CheckEffect23.setChecked(true);
                    CheckEffect24.setChecked(true);
                    CheckEffect25.setChecked(true);
                    CheckEffect26.setChecked(true);
                    CreateStringFav();
                } else {
                    SelectAll.setText("ВЫБРАТЬ ВСЕ");
                    CheckEffect1.setChecked(false);
                    CheckEffect2.setChecked(false);
                    CheckEffect3.setChecked(false);
                    CheckEffect4.setChecked(false);
                    CheckEffect5.setChecked(false);
                    CheckEffect6.setChecked(false);
                    CheckEffect7.setChecked(false);
                    CheckEffect8.setChecked(false);
                    CheckEffect9.setChecked(false);
                    CheckEffect10.setChecked(false);
                    CheckEffect11.setChecked(false);
                    CheckEffect12.setChecked(false);
                    CheckEffect13.setChecked(false);
                    CheckEffect14.setChecked(false);
                    CheckEffect15.setChecked(false);
                    CheckEffect16.setChecked(false);
                    CheckEffect17.setChecked(false);
                    CheckEffect18.setChecked(false);
                    CheckEffect19.setChecked(false);
                    CheckEffect20.setChecked(false);
                    CheckEffect21.setChecked(false);
                    CheckEffect22.setChecked(false);
                    CheckEffect23.setChecked(false);
                    CheckEffect24.setChecked(false);
                    CheckEffect25.setChecked(false);
                    CheckEffect26.setChecked(false);
                    CreateStringFav();
                }
            }
        });
        
        //                                                                                          Кнопка Назад в избранном
        buttonBackFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
                MainLayout.setVisibility(View.VISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
            }
        });
        // =============================FAV END ===================================================

        TextReceived1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ParceStr = TextReceived1.getText().toString().split(":");
                editTextAddress.setText(ParceStr[0]);
                editTextPort.setText(ParceStr[1].substring(0, 4));
            }
        });
        TextReceived2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ParceStr = TextReceived2.getText().toString().split(":");
                editTextAddress.setText(ParceStr[0]);
                editTextPort.setText(ParceStr[1].substring(0, 4));
            }
        });
        TextReceived3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ParceStr = TextReceived3.getText().toString().split(":");
                editTextAddress.setText(ParceStr[0]);
                editTextPort.setText(ParceStr[1].substring(0, 4));
            }
        });
        TextReceived4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ParceStr = TextReceived4.getText().toString().split(":");
                editTextAddress.setText(ParceStr[0]);
                editTextPort.setText(ParceStr[1].substring(0, 4));
            }
        });
        TextReceived5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ParceStr = TextReceived5.getText().toString().split(":");
                editTextAddress.setText(ParceStr[0]);
                editTextPort.setText(ParceStr[1].substring(0, 4));
            }
        });

    }
    // ===================== END onCreate =========================================================


    // =========================== УСТАНОВКА ВРЕМЕНИ БУДИЛЬНИКОВ =================================
    //                                                                                              Отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(MainActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
        ID = v;
    }
    //                                                                                              Устанавливаем время в нужный TextView
    private void setInitialDateTime() {
        TextView AlarmTime;
        int AlarmNum;
        switch (ID.getId()) {
            case R.id.TextView1:
                AlarmTime = findViewById(R.id.TextView1); AlarmNum = 1;
                break;
            case R.id.TextView2:
                AlarmTime = findViewById(R.id.TextView2); AlarmNum = 2;
                break;
            case R.id.TextView3:
                AlarmTime = findViewById(R.id.TextView3); AlarmNum = 3;
                break;
            case R.id.TextView4:
                AlarmTime = findViewById(R.id.TextView4); AlarmNum = 4;
                break;
            case R.id.TextView5:
                AlarmTime = findViewById(R.id.TextView5); AlarmNum = 5;
                break;
            case R.id.TextView6:
                AlarmTime = findViewById(R.id.TextView6); AlarmNum = 6;
                break;
            case R.id.TextView7:
                AlarmTime = findViewById(R.id.TextView7); AlarmNum = 7;
                break;
            default:
                AlarmTime = findViewById(R.id.textViewDef); AlarmNum = 0;
        }

        AlarmTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        String[] TimeString = DateUtils.formatDateTime(this, dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME).split(":");
        int A_Time = Integer.parseInt(TimeString[0]) * 60 + Integer.parseInt(TimeString[1]);
        //Log.e("TIME_ALARM", A_Time + "");
        OutputMess = "ALM_SET" + AlarmNum + " " + A_Time;
        SendData();
    }

    //                                                                                              установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
    // ============================================================================================

    // ============================= МЕНЮ =========================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //                                                                                              Пукты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.Menu_alarms :
                countDownTimer.cancel();
                AlarmLayout.setVisibility(View.VISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
                TimerLayout.setVisibility(View.INVISIBLE);
                OutputMess = "ALM_GET";
                SendData();
                return true;
            case R.id.Menu_connects :
                countDownTimer.cancel();
                SettingsLayer.setVisibility(View.VISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                TimerLayout.setVisibility(View.INVISIBLE);
                return true;
            case R.id.Menu_favorites :
                countDownTimer.cancel();
                FavsLayout.setVisibility(View.VISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
                TimerLayout.setVisibility(View.INVISIBLE);
                OutputMess = "FAV_GET";
                SendData();
                return true;
            case R.id.Menu_timer :
                countDownTimer.cancel();
                TimerLayout.setVisibility(View.VISIBLE);
                FavsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                AlarmLayout.setVisibility(View.INVISIBLE);
                SettingsLayer.setVisibility(View.INVISIBLE);
                OutputMess = "TMR_GET";
                SendData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ============================================================================================

    // =============================== ЗАПРОС И ПРИЁМ IP ==========================================
    //                                                                                              AsyncTask пингера
    @SuppressLint("StaticFieldLeak")
    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //                                                                                          Отправляем Ping
        @Override
        protected Void doInBackground(Void... params) {
            Res = null;
            PING.run();
            return null;
        }
        //                                                                                          Принимаем данные
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String sPort, sAddress;
            if(Res != null) {
                String[] ParseStr = Res.split(":");
                if(ParseStr.length > 1) {
                    sPort = ParseStr[1];
                    sAddress = ParseStr[0].substring(3);
//                    editTextAddress.setText(sAddress);
//                    editTextPort.setText(sPort);
                    String AdrPrt = sAddress + ":" + sPort + "\n";
                    if (!AP.contains(AdrPrt)) {
                        Cnt ++;
                        AP = AP + AdrPrt;
                        if (Cnt == 1) {
                            TextReceived1.setText(AdrPrt);
                            Toast.makeText(getApplicationContext(), "Лампа 1 найдена", Toast.LENGTH_LONG).show();
                        }
                        else if (Cnt == 2) {
                            TextReceived2.setText(AdrPrt);
                            Toast.makeText(getApplicationContext(), "Лампа 2 найдена", Toast.LENGTH_LONG).show();
                        }
                        else if (Cnt == 3) {
                            TextReceived3.setText(AdrPrt);
                            Toast.makeText(getApplicationContext(), "Лампа 3 найдена", Toast.LENGTH_LONG).show();
                        }
                        else if (Cnt == 4) {
                            TextReceived4.setText(AdrPrt);
                            Toast.makeText(getApplicationContext(), "Лампа 4 найдена", Toast.LENGTH_LONG).show();
                        }
                        else if (Cnt == 5) {
                            TextReceived5.setText(AdrPrt);
                            Toast.makeText(getApplicationContext(), "Лампа 5 найдена", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }
    //                                                                                              Пингер. Отправляе запрос DISCOVER для поиска платы 5 раз
    private class Pinger extends Thread {
        @Override
        public void run() {
            //for (int i = 0; i < 5; i++) {
                if (!closed) {
                    udp = new UDPHelper(getApplicationContext(), new UDPHelper.BroadcastListener() {
                        @Override
                        public void onReceive(String msg, String ip) {
                            Log.v("ПРИНЯЛИ ", "receive message " + msg + " from " + ip);
                            //closed = true;
                            Res = msg;
                        }
                    });
                    udp.start();

                    try {
                        udp.send();
                        Log.v("", "ping sended ....");
                        sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            //}
        }

    }
    // ============================================================================================

    // ============================= ОТПРАВКА И ПРИЁМ ДАННЫХ ======================================
    //                                                                                              UDP Клиент *************************************************************
    public class UdpClientThread extends Thread{

        private String dstAddress;
        private int dstPort;
        private String Mess;
        DatagramSocket socket;

        UdpClientThread(String addr, int port, String Mes) {
            super();
            dstAddress = addr;
            dstPort = port;
            Mess = Mes;
        }

        @Override
        public void run() {
            try {
                socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(dstAddress);
                //                                                                                  send request
                byte[] buf = new byte[1024];
                byte[] buff = Mess.getBytes();
                DatagramPacket packet = new DatagramPacket(buff, buff.length, address, dstPort);
                socket.send(packet);
                //                                                                                  get response
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String line = new String(packet.getData(), 0, packet.getLength());
                Chang = line;
                sleep(10);
                Log.e("Ответ от ESP ", line);
                UpdateUI(line);
                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        private void cancel() {
            Toast.makeText(getApplicationContext(), "Close - SOCKET", Toast.LENGTH_LONG).show();
            try {
                socket.close();
            }
            catch (IOError e) {
                e.printStackTrace();
            }
        }

    }
    // ============================================================================================

    //============================================================================================= // Таймер пингера
    public class PingerCountDownTimer extends CountDownTimer {

        PingerCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {
            mt = new MyTask();
            mt.execute();
        }
    }


    // ====================== CountDownTimer class ================================================ // Тайиер. Каждые 2 секунды отправляет GET
    public class MalibuCountDownTimer extends CountDownTimer {

        MalibuCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {
            GET_Sendet = true;
            OutputMess = "GET";
            SendData();
        }
    }
    // CURR = currentMode Brightness Speed Scale ONflag espMode USE_NTP TimerRunning buttonEnabled currentTicks
    // CURR         1       50          1   100     1       1       1       0           1           14:31:08
    // ============================================================================================

    // ====================== CountDownTimer class ================================================ // Тайиер отсчета времени отключения
    public class TimerCountDownTimer extends CountDownTimer {

        TimerCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {
            String time = simpleDateFormat.format(millisUntilFinished);
            textViewTimerOff.setText(time);
        }
    }
    //==============================================================================================
    //                                                                                              Сохраняем адрес и порт в файл
    void SaveAddressAndPort(String A, String P) {
        GlobalAddress = A;
        GlobalPort = P;
        AddressPortFile.edit().putString("Address", GlobalAddress).apply();
        AddressPortFile.edit().putString("Port", GlobalPort).apply();
    }
    //                                                                                              Проверка на число
    public static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    //                                                                                              Закрытие приложения
    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if(udpClientThread!=null) udpClientThread.cancel();
    }
    //                                                                                              Обновление интерфейса после получения данных от платы
    @SuppressLint("SetTextI18n")
    public void UpdateUI(final String Data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String HH, MM;
                String[] StriStat = Data.split(" ");
                //                                                                                  Если получили настройки будильников
                if(StriStat.length > 0) {
                    if (StriStat[0].equals("ALMS") && StriStat.length == 16) {
                        itsFromESP = true;
                        if(isDigit(StriStat[1]) && Integer.parseInt(StriStat[1]) == 1) {AlarmOn1.setChecked(true);} else {AlarmOn1.setChecked(false);}
                        if(isDigit(StriStat[2]) && Integer.parseInt(StriStat[2]) == 1) {AlarmOn2.setChecked(true);} else {AlarmOn2.setChecked(false);}
                        if(isDigit(StriStat[3]) && Integer.parseInt(StriStat[3]) == 1) {AlarmOn3.setChecked(true);} else {AlarmOn3.setChecked(false);}
                        if(isDigit(StriStat[4]) && Integer.parseInt(StriStat[4]) == 1) {AlarmOn4.setChecked(true);} else {AlarmOn4.setChecked(false);}
                        if(isDigit(StriStat[5]) && Integer.parseInt(StriStat[5]) == 1) {AlarmOn5.setChecked(true);} else {AlarmOn5.setChecked(false);}
                        if(isDigit(StriStat[6]) && Integer.parseInt(StriStat[6]) == 1) {AlarmOn6.setChecked(true);} else {AlarmOn6.setChecked(false);}
                        if(isDigit(StriStat[7]) && Integer.parseInt(StriStat[7]) == 1) {AlarmOn7.setChecked(true);} else {AlarmOn7.setChecked(false);}

                        if(isDigit(StriStat[8]) && Integer.parseInt(StriStat[8]) != 0) {
                            int t = Integer.parseInt(StriStat[8]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime1.setText(HH + ":" + MM);
                        } else { AlarmTime1.setText("00:00"); }
                        if(isDigit(StriStat[9]) && Integer.parseInt(StriStat[9]) != 0) {
                            int t = Integer.parseInt(StriStat[9]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime2.setText(HH + ":" + MM);
                        } else { AlarmTime2.setText("00:00"); }
                        if(isDigit(StriStat[10]) && Integer.parseInt(StriStat[10]) != 0) {
                            int t = Integer.parseInt(StriStat[10]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime3.setText(HH + ":" + MM);
                        } else { AlarmTime3.setText("00:00"); }
                        if(isDigit(StriStat[11]) && Integer.parseInt(StriStat[11]) != 0) {
                            int t = Integer.parseInt(StriStat[11]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime4.setText(HH + ":" + MM);
                        } else { AlarmTime4.setText("00:00"); }
                        if(isDigit(StriStat[12]) && Integer.parseInt(StriStat[12]) != 0) {
                            int t = Integer.parseInt(StriStat[12]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime5.setText(HH + ":" + MM);
                        } else { AlarmTime5.setText("00:00"); }
                        if(isDigit(StriStat[13]) && Integer.parseInt(StriStat[13]) != 0) {
                            int t = Integer.parseInt(StriStat[13]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime6.setText(HH + ":" + MM);
                        } else { AlarmTime6.setText("00:00"); }
                        if(isDigit(StriStat[14]) && Integer.parseInt(StriStat[14]) != 0) {
                            int t = Integer.parseInt(StriStat[14]);
                            int hours = t / 60;
                            int minutes = t % 60;
                            if(hours < 10) {HH = "0" + hours;} else {HH = hours + "";}
                            if(minutes < 10) {MM = "0" + minutes;} else {MM = minutes + "";}
                            AlarmTime7.setText(HH + ":" + MM);
                        } else { AlarmTime7.setText("00:00"); }

                        spinnerA.setSelection(Integer.parseInt(StriStat[15]));
                        Log.v("ПРИНЯЛИ ", "БУДИЛЬНИКИ " + StriStat[0]);
                    }
                    //                                                                              Если получили настройки избранного
                    if (StriStat[0].equals("FAV")) {
                        itsFromESP = true;
                        if(isDigit(StriStat[1]) && Integer.parseInt(StriStat[1]) == 1) {switchMODE.setChecked(true);} else {switchMODE.setChecked(false);}
                        Rinterval = Integer.parseInt(StriStat[2]);
                        View Butt; RadioButton B;
                        switch (Rinterval) {
                            case 60:
                                Butt = radioGroupInterval.getChildAt(0); B = findViewById(Butt.getId()); B.setChecked(false);
                                break;
                            case 120:
                                Butt = radioGroupInterval.getChildAt(1); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 180:
                                Butt = radioGroupInterval.getChildAt(2); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 300:
                                Butt = radioGroupInterval.getChildAt(3); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 600:
                                Butt = radioGroupInterval.getChildAt(4); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 900:
                                Butt = radioGroupInterval.getChildAt(5); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 1200:
                                Butt = radioGroupInterval.getChildAt(6); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 1800:
                                Butt = radioGroupInterval.getChildAt(7); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 3600:
                                Butt = radioGroupInterval.getChildAt(8); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                        }
                        radioGroupInterval.setVisibility(View.INVISIBLE);
                        Rrasbros = Integer.parseInt(StriStat[3]);
                        switch (Rrasbros) {
                            case 0:
                                Butt = radioGroupRasbros.getChildAt(0); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 60:
                                Butt = radioGroupRasbros.getChildAt(1); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 120:
                                Butt = radioGroupRasbros.getChildAt(2); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 180:
                                Butt = radioGroupRasbros.getChildAt(3); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 300:
                                Butt = radioGroupRasbros.getChildAt(4); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 600:
                                Butt = radioGroupRasbros.getChildAt(5); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 900:
                                Butt = radioGroupRasbros.getChildAt(6); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 1200:
                                Butt = radioGroupRasbros.getChildAt(7); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 1800:
                                Butt = radioGroupRasbros.getChildAt(8); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                        }
                        radioGroupRasbros.setVisibility(View.INVISIBLE);
                        if(Integer.parseInt(StriStat[4]) == 1) {switchSost.setChecked(true);} else {switchSost.setChecked(false);}

                        if(Integer.parseInt(StriStat[5]) == 1) {CheckEffect1.setChecked(true);} else {CheckEffect1.setChecked(false);}
                        if(Integer.parseInt(StriStat[6]) == 1) {CheckEffect2.setChecked(true);} else {CheckEffect2.setChecked(false);}
                        if(Integer.parseInt(StriStat[7]) == 1) {CheckEffect3.setChecked(true);} else {CheckEffect3.setChecked(false);}
                        if(Integer.parseInt(StriStat[8]) == 1) {CheckEffect4.setChecked(true);} else {CheckEffect4.setChecked(false);}
                        if(Integer.parseInt(StriStat[9]) == 1) {CheckEffect5.setChecked(true);} else {CheckEffect5.setChecked(false);}
                        if(Integer.parseInt(StriStat[10]) == 1) {CheckEffect6.setChecked(true);} else {CheckEffect6.setChecked(false);}
                        if(Integer.parseInt(StriStat[11]) == 1) {CheckEffect7.setChecked(true);} else {CheckEffect7.setChecked(false);}
                        if(Integer.parseInt(StriStat[12]) == 1) {CheckEffect8.setChecked(true);} else {CheckEffect8.setChecked(false);}
                        if(Integer.parseInt(StriStat[13]) == 1) {CheckEffect9.setChecked(true);} else {CheckEffect9.setChecked(false);}
                        if(Integer.parseInt(StriStat[14]) == 1) {CheckEffect10.setChecked(true);} else {CheckEffect10.setChecked(false);}
                        if(Integer.parseInt(StriStat[15]) == 1) {CheckEffect11.setChecked(true);} else {CheckEffect11.setChecked(false);}
                        if(Integer.parseInt(StriStat[16]) == 1) {CheckEffect12.setChecked(true);} else {CheckEffect12.setChecked(false);}
                        if(Integer.parseInt(StriStat[17]) == 1) {CheckEffect13.setChecked(true);} else {CheckEffect13.setChecked(false);}
                        if(Integer.parseInt(StriStat[18]) == 1) {CheckEffect14.setChecked(true);} else {CheckEffect14.setChecked(false);}
                        if(Integer.parseInt(StriStat[19]) == 1) {CheckEffect15.setChecked(true);} else {CheckEffect15.setChecked(false);}
                        if(Integer.parseInt(StriStat[20]) == 1) {CheckEffect16.setChecked(true);} else {CheckEffect16.setChecked(false);}
                        if(Integer.parseInt(StriStat[21]) == 1) {CheckEffect17.setChecked(true);} else {CheckEffect17.setChecked(false);}
                        if(Integer.parseInt(StriStat[22]) == 1) {CheckEffect18.setChecked(true);} else {CheckEffect18.setChecked(false);}
                        if(Integer.parseInt(StriStat[23]) == 1) {CheckEffect19.setChecked(true);} else {CheckEffect19.setChecked(false);}
                        if(Integer.parseInt(StriStat[24]) == 1) {CheckEffect20.setChecked(true);} else {CheckEffect20.setChecked(false);}
                        if(Integer.parseInt(StriStat[25]) == 1) {CheckEffect21.setChecked(true);} else {CheckEffect21.setChecked(false);}
                        if(Integer.parseInt(StriStat[26]) == 1) {CheckEffect22.setChecked(true);} else {CheckEffect22.setChecked(false);}
                        if(Integer.parseInt(StriStat[27]) == 1) {CheckEffect23.setChecked(true);} else {CheckEffect23.setChecked(false);}
                        if(Integer.parseInt(StriStat[28]) == 1) {CheckEffect24.setChecked(true);} else {CheckEffect24.setChecked(false);}
                        if(Integer.parseInt(StriStat[29]) == 1) {CheckEffect25.setChecked(true);} else {CheckEffect25.setChecked(false);}
                        if(Integer.parseInt(StriStat[30]) == 1) {CheckEffect26.setChecked(true);} else {CheckEffect26.setChecked(false);}
                    }
                    //                                                                              Если получили ответ на GET
                    if (StriStat[0].equals("CURR")) {
                        itsFromESP = true;
                        if (isDigit(StriStat[3]) && isDigit(StriStat[1])) {
                            if ((Integer.parseInt(StriStat[1]) >= 0 && Integer.parseInt(StriStat[1]) < 7) || (Integer.parseInt(StriStat[1]) > 15)) {
                                ValIntSp = Math.round(SeekSpeed.getMax()) - Integer.parseInt(StriStat[3]);
                                ValStrSp = ValIntSp + "";
                            } else {
                                ValIntSp = Integer.parseInt(StriStat[3]);
                                ValStrSp = ValIntSp + "";
                            }
                        }
                        SeekSpeed.setProgress(ValIntSp);

                        Text_Speed.setText(ValStrSp);
                        SeekBright.setProgress(Integer.parseInt(StriStat[2]));

                        if((Position >= 0 && Position < 7) || (Position > 15)) {
                            SeekScale.setProgress(Integer.parseInt(StriStat[4]));
                        } else {
                            SeekScale.setProgress(Math.round(SeekScale.getMax()) - Integer.parseInt(StriStat[4]));
                        }


                        spinner.setSelection(Integer.parseInt(StriStat[1]));
                        if (isDigit(StriStat[5])) {
                            if (Integer.parseInt(StriStat[5]) == 1) {
                                StatButtonOn = 1;
                            }
                            if (Integer.parseInt(StriStat[5]) == 0) {
                                StatButtonOn = 0;
                            }
                        }
                        if (StatButtonOn == 1) {
                            switchON.setChecked(true);
                        } else if (StatButtonOn == 0) {
                            switchON.setChecked(false);
                        }
                        StatConnect.setImageResource(R.drawable.wifigreen);
                        DataMess = "";
                        if(SettingsLayer.getVisibility() == View.VISIBLE) {
                            Toast.makeText(getApplicationContext(), "Соединение установлено", Toast.LENGTH_LONG).show();
                            SettingsLayer.setVisibility(View.INVISIBLE);
                        }
                        GET_Sendet = false;
                        Log.v("ПРИНЯЛИ ", "GET " + StriStat.length);
                    } else if(GET_Sendet) {
                        sch ++;
                        StatConnect.setImageResource(R.drawable.wifired);
                        if(sch == 5){//                                                             Если 5 раз лампа не ответила на GET
                            countDownTimer.cancel();
                            Toast.makeText(getApplicationContext(), "Соединение разорвано", Toast.LENGTH_LONG).show();
                            SettingsLayer.setVisibility(View.VISIBLE);
                           // MainLayout.setVisibility(View.INVISIBLE);
                            sch = 0;
                        }
                    }
                    //                                                                              Если получили таймеры
                    if(StriStat[0].equals("TMR")) {
                        itsFromESP = true;
                        StartTimer = Integer.parseInt(StriStat[3]);
                        View Butt; RadioButton B;
                        Rtimer = Integer.parseInt(StriStat[2]);
                        switch (Rtimer) {
                            case 0 :
                                Butt = radioGroupTimer.getChildAt(0); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 1 :
                                Butt = radioGroupTimer.getChildAt(1); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 2 :
                                Butt = radioGroupTimer.getChildAt(2); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 3 :
                                Butt = radioGroupTimer.getChildAt(3); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 4 :
                                Butt = radioGroupTimer.getChildAt(4); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 5 :
                                Butt = radioGroupTimer.getChildAt(5); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                            case 6 :
                                Butt = radioGroupTimer.getChildAt(6); B = findViewById(Butt.getId()); B.setChecked(true);
                                break;
                        }
                        radioGroupTimer.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }
    //                                                                                              Создаем строку для FAV и отправляем
    public void CreateStringFav() {
        String FAV_SET = "FAV_SET ";
        if(switchMODE.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        FAV_SET += Rinterval + " " + Rrasbros + " ";
        if(switchSost.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}

        if(CheckEffect1.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect2.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect3.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect4.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect5.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect6.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect7.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect8.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect9.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect10.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect11.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect12.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect13.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect14.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect15.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect16.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect17.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect18.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect19.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect20.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect21.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect22.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect23.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect24.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect25.isChecked()) {FAV_SET += 1 + " ";} else {FAV_SET += 0 + " ";}
        if(CheckEffect26.isChecked()) {FAV_SET += 1;} else {FAV_SET += 0;}
        OutputMess = FAV_SET;
        SendData();
    }
    //                                                                                              Проверка соединения
    public void ChangeConnect() {
        TestConnect = true;
        TT = new TestTask();
        TT.execute();
    }
    //                                                                                              Отправка данных
    public void SendData() {
        if(GlobalAddress.length() > 1 && GlobalPort.length() > 1 && OutputMess.length() > 1) {
            udpClientThread = new UdpClientThread(GlobalAddress, Integer.parseInt(GlobalPort), OutputMess);
            udpClientThread.start();
        }
    }
    //                                                                                              AsyncTask проверки соединения
    @SuppressLint("StaticFieldLeak")
    class TestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Chang = null;
            String TestAddr = editTextAddress.getText().toString();
            int TestPort = Integer.parseInt(editTextPort.getText().toString());
            udpClientThread = new UdpClientThread(TestAddr, TestPort, "DISCOVER");
            udpClientThread.start();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SystemClock.sleep(100);
            if(Chang == null) {
                Toast.makeText(getApplicationContext(), "IP " + editTextAddress.getText().toString() + "\n" +
                        "PORT " + Integer.parseInt(editTextPort.getText().toString()) + "\n" +
                        "Лампа не отвечает\n\nПопробуйте еще раз", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Лампа даёт добро ;-)\n\nМожно сохранять", Toast.LENGTH_LONG).show();
            }
        }
    }
}
