package cn.qd.peiwen.pwtextspeecher;

/**
 * Created by nick on 2018/5/31.
 */

public interface ISpeechListener {
    void onInitError();
    void onInitSuccess();
    void onLanguageError();
    void onPlayError();
    void onPlayStated();
    void onPlayStoped();
}
