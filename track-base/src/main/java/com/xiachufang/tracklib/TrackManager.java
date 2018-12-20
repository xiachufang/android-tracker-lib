package com.xiachufang.tracklib;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.xiachufang.tracklib.exception.EventException;
import com.xiachufang.tracklib.model.ISendControl;
import com.xiachufang.tracklib.model.SendControler;
import com.xiachufang.tracklib.net.IHeaderConfig;
import com.xiachufang.tracklib.net.IHttpManager;
import com.xiachufang.tracklib.services.TrackPushService;
import com.xiachufang.tracklib.util.DeviceUtils;
import com.xiachufang.tracklib.util.GlobalParams;


/**
 * sdk初始化入口类，全局管理类
 */

public final class TrackManager {

    private static Context context;
    private static Application application;
    public volatile static boolean hasInit = false;

    private static IHttpManager httpManager;

    private static ISendControl iSendControl;

    private static IHeaderConfig headerConfig;


    private static boolean isDebug;

    /**
     * 初始化sdk, 要在application中的onCreate() 方法中进行初始化.
     *  @param context 全局上下文
     * @param isDebug 是否是debug模式(控制开启log等)
     */
    private static void init(Context context, Application applications, boolean isDebug) {

        if (context == null) {
            return;
        }

        if (applications==null){
            throw new EventException("请先在应用层传入Application");
        }

        //处理app拥有多个进程
        String processName = DeviceUtils.getProcessName(context, Process.myPid());
        //不在主进程中
        if (processName == null || !processName.equals(context.getPackageName() + "")) {
            return;
        }

        if (hasInit) {
            return;
        }
        hasInit = true;
        GlobalParams.SWITCH_ON = true;
        GlobalParams.DEVELOP_MODE = isDebug;
//        XcfLocationManager.getInstance().start(context);

        TrackPushService.startService();

        Log.e(GlobalParams.TAG, "推送服务已开始");

    }



    /**
     * 停止sdk所有服务(停止事件统计,停止事件推送)
     */
    public static void destoryEventService() {
        //变为 可初始化
        hasInit = false;
        //关闭一切统计事务
        GlobalParams.SWITCH_ON = true;
        TrackPushService.getInstance().stopEventService();

    }


    /**
     * 停止事件的推送任务(仍会记录事件,停止事件推送)
     */
    public static void cancelEventPush() {
        //变为 可初始化
        hasInit = false;
        TrackPushService.getInstance().stopEventService();

    }

    public static IHttpManager getHttpManager() {
        return httpManager;
    }


    public static Context getContext() {
        return context;
    }

    public static Application getApplication() {
        return application;
    }


    public static void setConfig(Config configBuilder) {
        application = configBuilder.application;
        context = configBuilder.applicationContext;
        httpManager = configBuilder.httpManager;
        isDebug = configBuilder.DEVELOP_MODE;
        headerConfig = configBuilder.headConfig;
        if (configBuilder.iSendControl==null){
            iSendControl = new SendControler();
        }else {
            iSendControl = configBuilder.iSendControl;
        }
        init(context,application,isDebug);
    }

    public static IHeaderConfig getHeadrConfig() {
        return headerConfig;
    }

    public static ISendControl getSendControler() {
        return iSendControl;
    }


    /**
     * sdk初始化配置，必须设置才能使用
     */
    public static class Config {


        public IHeaderConfig headConfig;

        private Context applicationContext;

        private boolean DEVELOP_MODE = GlobalParams.DEVELOP_MODE;

        private int PUSH_CUT_NUMBER = GlobalParams.PUSH_CUT_NUMBER;

        private double PUSH_CUT_DATE = GlobalParams.PUSH_CUT_TIMER_INTERVAL;

        private Application application;

        private IHttpManager httpManager;

        private ISendControl iSendControl;

        public Config(Context applicationContext) {
            this.applicationContext = applicationContext;

        }

        public Config setHeadConfig(IHeaderConfig headConfig) {
            this.headConfig = headConfig;
            return this;
        }

        /**
         * 是否是开发者模式
         *
         * @param isDebug
         * @return
         */
        public Config setDebug(boolean isDebug) {
            DEVELOP_MODE = isDebug;
            return this;
        }


        public Config setiSendControl(ISendControl iSendControl) {
            this.iSendControl = iSendControl;
            return this;
        }

        /**
         * 主动推送上限数
         *
         * @param num
         * @return
         */
        public Config setPushLimitNum(int num) {
            PUSH_CUT_NUMBER = num;
            return this;
        }


        /**
         * 推送周期
         * @param minutes
         * @return
         */
        public Config setPushLimitMinutes(double minutes) {
            PUSH_CUT_DATE = minutes;
            return this;
        }

        /**
         * application
         * @param application
         * @return
         */
        public Config setApplication(Application application) {
            this.application = application;
            return this;
        }

        /**
         * 网络请求引擎
         * @param httpManager
         * @return
         */
        public Config setHttpManager(IHttpManager httpManager) {
            this.httpManager = httpManager;
            return this;
        }

        /**
         * 开始构建
         */
        public Config init() {

            GlobalParams.PUSH_CUT_NUMBER = PUSH_CUT_NUMBER;
            GlobalParams.PUSH_CUT_TIMER_INTERVAL = PUSH_CUT_DATE;
            GlobalParams.DEVELOP_MODE = DEVELOP_MODE;
            return this;
        }

    }


    /**
     * 立即发送数据
     * TODO 应用在退出时，应该手动调用该方法，将未发的数据发出去
     */
    public static void pushEvent() {
        TrackPushService.getInstance().excutePushEvent();
    }

}
