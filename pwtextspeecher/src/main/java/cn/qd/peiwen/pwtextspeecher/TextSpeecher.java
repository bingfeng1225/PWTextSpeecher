package cn.qd.peiwen.pwtextspeecher;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by nick on 2018/5/31.
 */

public class TextSpeecher extends UtteranceProgressListener implements TextToSpeech.OnInitListener {
    private Locale locale;
    private Context context;
    private boolean success = false;
    private ISpeechListener listener;
    private TextToSpeech textToSpeech;

    public TextSpeecher(Context context) {
        this.context = context;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ISpeechListener getListener() {
        return listener;
    }

    public void setListener(ISpeechListener listener) {
        this.listener = listener;
    }

    public void init() {
        if (null == this.textToSpeech) {
            this.textToSpeech = new TextToSpeech(this.context, this);
            this.textToSpeech.setOnUtteranceProgressListener(this);
            this.textToSpeech.setPitch(1.0f);
        }
    }

    public void queue(String text) {
        this.speak(text,TextToSpeech.QUEUE_ADD);
    }

    public void flush(String text) {
        this.speak(text,TextToSpeech.QUEUE_FLUSH);
    }

    public void release() {
        this.success = false;
        if (null != this.textToSpeech) {
            this.textToSpeech.stop();
            this.textToSpeech.shutdown();
            this.textToSpeech = null;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.ERROR) {
            this.onInitError();
        } else {
            this.onInitSuccess();
        }
    }

    @Override
    public void onStart(String utteranceId) {
        if (null != listener) {
            listener.onPlayStated();
        }
    }

    @Override
    public void onDone(String utteranceId) {
        if (null != listener) {
            listener.onPlayStoped();
        }
    }

    @Override
    public void onError(String utteranceId) {
        if (null != listener) {
            listener.onPlayError();
        }
    }

    private void speak(String text, int queueMode) {
        if(this.success) {
            this.textToSpeech.speak(text, queueMode, null, UUID.randomUUID().toString());
        }
    }

    private void onInitError() {
        this.success = false;
        if (null != listener) {
            listener.onInitError();
        }
    }

    private void onInitSuccess() {
        int result = textToSpeech.setLanguage(this.locale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            this.success = false;
            if (null != listener) {
                listener.onLanguageError();
            }
        } else {
            this.success = true;
            if (null != listener) {
                listener.onInitSuccess();
            }
        }
    }

}
