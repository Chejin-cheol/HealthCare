package cs.healthCare.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.Console;

import cs.healthCare.R;

public class CharacterService extends Service implements  Runnable{
    private final int DEFAULT_MOTION = 0 , TAIL_MOTION = 1;
    private int currentMotion = 0;
    boolean  isPause;

    IBinder mBinder;
    Handler _handler;
    Thread _thread;
    //view
    ImageView _imageView;

    public class CharacterBinder extends Binder
    {
        public CharacterService getService()
        {
            return  CharacterService.this;
        }
    }
    public class CharacterHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(_imageView);
            switch(msg.what)
            {
                case DEFAULT_MOTION :
                    Glide.with(CharacterService.this).load(R.drawable.characater_default).into(gifImage);
                    break;
                case TAIL_MOTION :
                    break;
            }
        }
}

    @Override
    public IBinder onBind(Intent intent) {
        if( mBinder == null)
        {
            mBinder = new CharacterBinder();
            _handler = new CharacterHandler();
            _thread = new Thread(this);
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder = null;
    }

    // custom
    public void setCharacterView(ImageView v)
    {
        _imageView = v;
        _handler.sendEmptyMessage(0);
    }

    public void setCharacterState(int state)
    {
        _handler.sendEmptyMessage(state);
    }
    public void changeActionMode()
    {
        if(! isPause)
        {
            _thread.interrupt();
        }
        isPause = !isPause;
    }

    @Override
    public void run() {
        while(true)
        {
            if(!isPause) {
                        /*
                        Thread.sleep(3000);
                        if(currentMotion ==0)
                        {
                            _handler.sendEmptyMessage(1);
                            currentMotion = 1;
                        }
                        else
                        {
                            _handler.sendEmptyMessage(0);
                            currentMotion = 0;
                        }
                        */
            }
        }
    }
}