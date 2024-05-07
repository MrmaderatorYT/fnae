package com.ccs.fnae;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class Game extends AppCompatActivity {
    byte nightNumber;
    private Thread spawnThread = null;
    private boolean stopSpawn = false;

    VideoView bgInCamera;
    ImageView logoLoadingNight, bgInelevator;
    private int flashlightValue = 100;
    private Thread thread = new Thread();
    private final Handler handler = new Handler();

    MediaPlayer nightSound;
    TextView flashlightTextView, timeText;
    int randomNumber;
    private int decreaseInterval, night = 9 * 60 * 1000;//night 540000
    private boolean doubleSpeed = false, buttonClicked = false; // Флаг двойной скорости уменьшения

    Button openCamera, makeLoud, closeDoor;
    TextView cam1, cam2, cam3, cam4, cam5, cam6, cam7;
    int delay;
    int camNum;
    View palka1, palka2, palka3, palka4, palka5, palka6, palka7;

    boolean isOpenDoor = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bgInelevator = findViewById(R.id.bgInelevator);
        bgInCamera = findViewById(R.id.bgInCamera);
        closeDoor = findViewById(R.id.closeDoor);
        cam1 = findViewById(R.id.cam1);
        cam2 = findViewById(R.id.cam2);
        cam3 = findViewById(R.id.cam3);
        cam4 = findViewById(R.id.cam4);
        cam5 = findViewById(R.id.cam5);
        cam6 = findViewById(R.id.cam6);
        cam7 = findViewById(R.id.cam7);

        palka1 = findViewById(R.id.palka1);
        palka2 = findViewById(R.id.palka2);
        palka3 = findViewById(R.id.palka3);
        palka4 = findViewById(R.id.palka4);
        palka5 = findViewById(R.id.palka5);
        palka6 = findViewById(R.id.palka6);
        palka7 = findViewById(R.id.palka7);
        openCamera = findViewById(R.id.openCamera);
        makeLoud = findViewById(R.id.makeLoud);
        logoLoadingNight = findViewById(R.id.logoLoading);
        flashlightTextView = findViewById(R.id.energy);
        timeText = findViewById(R.id.time);
        flashlightTextView.setText("Энергия " + flashlightValue);


        nightNumber = PreferenceConfig.getByte(Game.this);
        switch (nightNumber) {
            case 0:
                // Создаем MediaPlayer и воспроизводим звук
                nightSound = MediaPlayer.create(Game.this, R.raw.startnight);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();

                // Устанавливаем изображение и делаем его видимым
                logoLoadingNight.setImageResource(R.drawable.night1);
                logoLoadingNight.setVisibility(View.VISIBLE);

                // Запланируем скрытие изображения через 2 секунды
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logoLoadingNight.setVisibility(View.INVISIBLE);
                        decreaseTask().run();
                        createTimerRunnable().run();

                    }
                }, 1000);


                break;
            case 1:
                // Создаем MediaPlayer и воспроизводим звук
                nightSound = MediaPlayer.create(this, R.raw.startnight);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();

                // Устанавливаем изображение и делаем его видимым
                logoLoadingNight.setImageResource(R.drawable.night2);
                logoLoadingNight.setVisibility(View.VISIBLE);

                // Запланируем скрытие изображения через 2 секунды
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logoLoadingNight.setVisibility(View.INVISIBLE);
                        decreaseTask().run();
                        createTimerRunnable().run();

                    }
                }, 1000);
                break;
            case 2:
                // Создаем MediaPlayer и воспроизводим звук
                nightSound = MediaPlayer.create(Game.this, R.raw.startnight);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();

                    }
                });
                nightSound.start();

                // Устанавливаем изображение и делаем его видимым
                logoLoadingNight.setImageResource(R.drawable.night3);
                logoLoadingNight.setVisibility(View.VISIBLE);

                // Запланируем скрытие изображения через 2 секунды
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logoLoadingNight.setVisibility(View.INVISIBLE);
                        decreaseTask().run();
                        createTimerRunnable().run();
                    }
                }, 1000);

                break;
        }
        bgInCamera.setBackgroundResource(R.drawable.elevatoropen);


        cam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #1");
                camNum = 1;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==0){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #1");
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam1);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    bgInCamera.start();
                    System.out.println("Video is started");
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam1_static);
                }

            }
        });
        cam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #2");
                camNum = 2;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==1){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #2");
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam2);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    System.out.println("Video is started");

                    bgInCamera.start();
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam2_static);
                }
            }
        });
        cam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #3");
                camNum = 3;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==2){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #3");
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam3);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    System.out.println("Video is started");

                    bgInCamera.start();
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam3_static);
                }

            }
        });
        cam4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #4");
                camNum = 4;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==3){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #4");
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam4);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    System.out.println("Video is started");

                    bgInCamera.start();
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam4_static);
                }

            }
        });
        cam5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #5");
                camNum = 5;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==4){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #5");
                    //TODO bgInelevator.setVisibility();
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam5);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    System.out.println("Video is started");

                    bgInCamera.start();
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam5_static);
                }

            }
        });
        cam6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opened can #6");
                camNum = 6;
                //TODO короче - менять видос а также невзлопотреблять кнопкой "Испугнуть" - 2-3 камеры.
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                System.out.println("Picture number in camera " + randomNumber);
                if(randomNumber==5){
                    bgInCamera.setBackground(null);
                    System.out.println("Random is here. It could start an video in cam #6");
                    Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cam6);
                    bgInCamera.setVideoURI(videoUri);
                    bgInCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mp.setVolume(0f, 0f);
                        }
                    });
                    System.out.println("Video is started");

                    bgInCamera.start();
                }
                else{
                    System.out.println("Random is not here");
                    bgInCamera.setBackgroundResource(R.drawable.cam6_static);
                }
            }
            //TODO if energy<=0 remove all
        });

        closeDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightSound = MediaPlayer.create(Game.this, R.raw.closedoor);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();

                if (doubleSpeed) {
                    doubleSpeed = false;
                    isOpenDoor = true;
                    bgInCamera.setBackgroundResource(R.drawable.elevatoropen);

                } else {
                    isOpenDoor = false;
                    // Устанавливаем интервал в 2.5 секунды
                    doubleSpeed = true;
                    bgInCamera.setBackgroundResource(R.drawable.elevator);

                }

                // Останавливаем предыдущую задачу уменьшения
                // Запускаем новую задачу уменьшения с новыми параметрами
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightSound = MediaPlayer.create(Game.this, R.raw.btnclicked);
                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();
                    }
                });
                nightSound.start();
                if (doubleSpeed) {
                    doubleSpeed = false;
                    cam1.setVisibility(View.INVISIBLE);
                    cam2.setVisibility(View.INVISIBLE);
                    cam3.setVisibility(View.INVISIBLE);
                    cam4.setVisibility(View.INVISIBLE);
                    cam5.setVisibility(View.INVISIBLE);
                    cam6.setVisibility(View.INVISIBLE);
                    cam7.setVisibility(View.INVISIBLE);

                    palka1.setVisibility(View.INVISIBLE);
                    palka2.setVisibility(View.INVISIBLE);
                    palka3.setVisibility(View.INVISIBLE);
                    palka4.setVisibility(View.INVISIBLE);
                    palka5.setVisibility(View.INVISIBLE);
                    palka6.setVisibility(View.INVISIBLE);
                    palka7.setVisibility(View.INVISIBLE);
                    makeLoud.setVisibility(View.INVISIBLE);
                    closeDoor.setVisibility(View.VISIBLE);
                    bgInCamera.setBackgroundResource(R.drawable.elevator);
                    //TODO видос убрать камеры

                    //TODO cam 4 - 2x speed
                    //TODO cam1 - energy = 0 (10 sec)
                } else {
                    doubleSpeed = true;
                    cam1.setVisibility(View.VISIBLE);
                    cam2.setVisibility(View.VISIBLE);
                    cam3.setVisibility(View.VISIBLE);
                    cam4.setVisibility(View.VISIBLE);
                    cam5.setVisibility(View.VISIBLE);
                    cam6.setVisibility(View.VISIBLE);
                    cam7.setVisibility(View.VISIBLE);

                    palka1.setVisibility(View.VISIBLE);
                    palka2.setVisibility(View.VISIBLE);
                    palka3.setVisibility(View.VISIBLE);
                    palka4.setVisibility(View.VISIBLE);
                    palka5.setVisibility(View.VISIBLE);
                    palka6.setVisibility(View.VISIBLE);
                    palka7.setVisibility(View.VISIBLE);
                    makeLoud.setVisibility(View.VISIBLE);
                    closeDoor.setVisibility(View.INVISIBLE);


                }


            }
        });
        //TODO 
        makeLoud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = true;
                randomNumber = 20;

                stopSpawn();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startSpawn();
                            }
                        });
                    }
                };
                timer.schedule(task, 5000); // Почекати 5 секунд перед початком спавна

                switch (camNum) {
                    case 1:
                        nightSound = MediaPlayer.create(Game.this, R.raw.bottle);
                        bgInCamera.setBackgroundResource(R.drawable.cam1_static);
                        break;
                    case 2:
                        bgInCamera.setBackgroundResource(R.drawable.cam2_static);
                        nightSound = MediaPlayer.create(Game.this, R.raw.drobovik);
                    case 5:
                        bgInCamera.setBackgroundResource(R.drawable.cam5_static);
                        nightSound = MediaPlayer.create(Game.this, R.raw.drobovik);
                        break;
                    case 3:
                        bgInCamera.setBackgroundResource(R.drawable.cam3_static);
                        nightSound = MediaPlayer.create(Game.this, R.raw.ukr);
                        break;
                    case 4:
                        bgInCamera.setBackgroundResource(R.drawable.cam4_static);
                        nightSound = MediaPlayer.create(Game.this, R.raw.cam);
                        break;
                    case 6:
                        bgInCamera.setBackgroundResource(R.drawable.cam6_static);
                        nightSound = MediaPlayer.create(Game.this, R.raw.moskvvremya);
                        break;
                }


                // Здесь выполняйте код, который вы хотите выполнить через 5 секунд



// Запуск задачи через 5 секунд


                nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nightSound.stop();

                    }
                });
                nightSound.start();
            }
        });
    }


    private void updateFlashlightValue() {
        flashlightTextView.setText("Энергия " + flashlightValue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nightSound.isPlaying()) {
            nightSound.stop();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (nightSound.isPlaying()) {
            nightSound.stop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFlashlightValue(); // Обновить отображение значения фонарика
        decreaseTask();
        createTimerRunnable().run();
        Thread spawnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startSpawn(); // Ваш метод startSpawn()
            }
        });

        spawnThread.start();
        cam1.setVisibility(View.INVISIBLE);
        cam2.setVisibility(View.INVISIBLE);
        cam3.setVisibility(View.INVISIBLE);
        cam4.setVisibility(View.INVISIBLE);
        cam5.setVisibility(View.INVISIBLE);
        cam6.setVisibility(View.INVISIBLE);
        cam7.setVisibility(View.INVISIBLE);

        palka1.setVisibility(View.INVISIBLE);
        palka2.setVisibility(View.INVISIBLE);
        palka3.setVisibility(View.INVISIBLE);
        palka4.setVisibility(View.INVISIBLE);
        palka5.setVisibility(View.INVISIBLE);
        palka6.setVisibility(View.INVISIBLE);
        palka7.setVisibility(View.INVISIBLE);
        makeLoud.setVisibility(View.INVISIBLE);


    }

    Runnable decreaseTask() {
        Runnable decreaseTask = new Runnable() {
            @Override
            public void run() {
                if (flashlightValue > 0) {
                    // Планирование следующего выполнения
                    // Обновление отображения значения фонарика
                    if (doubleSpeed) {
                        decreaseInterval = 5000;
                    } else {
                        decreaseInterval = 7000;
                    }
                    flashlightValue--;
                    updateFlashlightValue(); // Обновление отображения значения фонарика

                    handler.removeCallbacks(this, decreaseInterval);
                    handler.postDelayed(this, decreaseInterval); // Планирование следующего выполнения
                }
            }
        };

        return decreaseTask;
    }

    private Runnable createTimerRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (night > 0) {
                    long seconds = (night / 1000);

                    handleTimeRemaining(seconds);

                    night -= 1000;
                    handler.postDelayed(this, 1000);
                    System.out.println("Night " + night);
                    System.out.println("Second " + seconds);
                    System.out.println("Picture "+randomNumber);
                    System.out.println("delay "+delay);
                    System.out.println("energy " + flashlightValue);
                    System.out.println("-----------------------------");
                } else {
                    logoLoadingNight.setVisibility(View.VISIBLE);
                    if(nightNumber!=3){
                        nightNumber++;
                        PreferenceConfig.saveByte(getApplicationContext(), nightNumber);
                    }
                    Thread delayedThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                logoLoadingNight.setImageResource(R.drawable.win);
                                Thread.sleep(5000); // Затримка в 5 секунд
                                logoLoadingNight.setVisibility(View.VISIBLE);
                                Thread.sleep(5000); // Затримка в 5 секунд
                                Intent intent = new Intent(Game.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                // Код, який ви хочете виконати через 5 секунд
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    delayedThread.start();

                }
            }
        };
    }
//TODO короче создать переменную для времини спавна. потом когда нажал на кнопку

    //Не трогать
    private void handleTimeRemaining(long seconds) {
        // Виконайте необхідні дії в залежності від часу

        if (seconds <= 1) {
            timeText.setText("6 A.M.");
        } else if (seconds <= 90) {
            timeText.setText("5 A.M.");
        } else if (seconds <= 180) {
            timeText.setText("4 A.M.");
        } else if (seconds <= 270) {
            timeText.setText("3 A.M.");
        } else if (seconds <= 360) {
            timeText.setText("2 A.M.");
        } else if (seconds <= 450) {
            timeText.setText("1 A.M.");
        } else {
            // залишається більше 450 секунд, тому встановлюємо "12 A.M."
            timeText.setText("12 A.M.");
        }
    }


    public void playRandomSoundAndVideo() {

        // Генерируем случайное целое число от 0 до 5
        randomNumber = (int)(Math.random()*(5+1));
        buttonClicked = false;
        button_pressed();

        // Здесь вы можете выбрать случайное видео и задать его URI для отображения в выбранном VideoView.
        // Также воспроиграйте случайный звук.
    }
    //TODO короче тут надо делать ё

    void button_pressed(){
        if (!buttonClicked) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO теоретически нужно создать разнве булеаны что бы не можно было отпугивать с разных камер.
                    //TODO смотря что для разынх камер разное (свитч кейсом можно)
                    switch (randomNumber){
                        case 0:
                            cam1.setVisibility(View.INVISIBLE);
                            cam2.setVisibility(View.INVISIBLE);
                            cam3.setVisibility(View.INVISIBLE);
                            cam4.setVisibility(View.INVISIBLE);
                            cam5.setVisibility(View.INVISIBLE);
                            cam6.setVisibility(View.INVISIBLE);
                            cam7.setVisibility(View.INVISIBLE);

                            palka1.setVisibility(View.INVISIBLE);
                            palka2.setVisibility(View.INVISIBLE);
                            palka3.setVisibility(View.INVISIBLE);
                            palka4.setVisibility(View.INVISIBLE);
                            palka5.setVisibility(View.INVISIBLE);
                            palka6.setVisibility(View.INVISIBLE);
                            palka7.setVisibility(View.INVISIBLE);
                            makeLoud.setVisibility(View.INVISIBLE);
                            closeDoor.setVisibility(View.INVISIBLE);
                            openCamera.setVisibility(View.INVISIBLE);
                            flashlightValue=0;
                            updateFlashlightValue();
                            break;
                        case 1:
                            //TODO бабку заспавнить в дверях
                            nightSound = MediaPlayer.create(Game.this, R.raw.startnight);
                            nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    nightSound.stop();
                                }
                            });
                            nightSound.start();

                            // Устанавливаем изображение и делаем его видимым
                            logoLoadingNight.setImageResource(R.drawable.elevatorbabka);
                            logoLoadingNight.setVisibility(View.VISIBLE);

                            // Запланируем скрытие изображения через 2 секунды
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    logoLoadingNight.setImageResource(R.drawable.lose);

                                }
                            }, 3000);
                            System.out.println("ТУмер от бабки");
                            break;
                        case 2:
                            //TODO деда заспавнить в дверях
                            nightSound = MediaPlayer.create(Game.this, R.raw.startnight);
                            nightSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    nightSound.stop();
                                }
                            });
                            nightSound.start();

                            // Устанавливаем изображение и делаем его видимым
                            logoLoadingNight.setImageResource(R.drawable.elevatorded);
                            logoLoadingNight.setVisibility(View.VISIBLE);

                            // Запланируем скрытие изображения через 2 секунды
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    logoLoadingNight.setImageResource(R.drawable.lose);

                                }
                            }, 3000);
                            System.out.println("ТУмер от деда");
                            break;
                        case 3:
                            switch (nightNumber){
                                case 0:
                                    delay = 45000;
                                    break;
                                case 1:
                                    delay = 37500;
                                    break;
                                case 2:
                                    delay = 30000;
                                    break;

                            }
                            //TODO ускорить спавн людей
                            break;
                        case 4:
                            if(isOpenDoor){
                                logoLoadingNight.setImageResource(R.drawable.elevatorbabka);
                            }
                            else{
                                nightSound = MediaPlayer.create(Game.this, R.raw.doorbb);
                                flashlightValue = (flashlightValue - 10 >= 0) ? flashlightValue - 10 : 0;
                                updateFlashlightValue();

                            }

                            updateFlashlightValue();
                        case 5:
                            if(isOpenDoor){
                                logoLoadingNight.setImageResource(R.drawable.elevatorded);
                            }else {
                                //TODO дверь вышибать -> -5% of energy
                                flashlightValue = (flashlightValue - 5 >= 0) ? flashlightValue - 5 : 0;
                                updateFlashlightValue();
                            }
                    }
                    System.out.println("Done");
                }
            }, 10000); // Запустите через 10 секунд
        }
    }
    private void startSpawn() {
        playRandomSoundAndVideo();

        spawnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Ваш код в потоці

                while (!Thread.interrupted()) {
                    // В зависимости от nightNumber задайте интервал для следующего запуска
                    int delay = 3000; // 1 ночь - 30 секунд

                    if (nightNumber == 1) {
                        delay = 2500; // Если у нас 2 ночь, задержка 25 секунд
                    } else if (nightNumber == 2) {
                        delay = 2000; // Если у нас 3 ночь, задержка 20 секунд
                    }

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        // Обработка прерывания потока
                        return;
                    }

                }
            }
        });

        spawnThread.start();
    }

    private void stopSpawn() {
        if (spawnThread != null && spawnThread.isAlive()) {
            bgInCamera.stopPlayback();
            bgInCamera.setBackground(null);
            spawnThread.interrupt();
            spawnThread = null;
        }
    }




}
//TODO камера не открываеться когда рандомом выбрали её что бы создать видос. когда время заканчиваеться - опять можно открыть эту камеру