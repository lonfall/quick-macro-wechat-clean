package com.lh.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @auther: loneyfall
 * @date: 2020/9/27
 * @description:
 */
public class TaskUtil {
    private volatile static Timer timer;

    private static Timer getTimer() {
        if (timer == null) {
            synchronized (TaskUtil.class) {
                if (timer == null) {
                    timer = new Timer();
                }
            }
        }
        return timer;
    }

    /**
     * 定时任务回调接口
     */
    public interface TaskCallback {
        void call();
    }

    /**
     * 执行一次任务
     *
     * @param callback 回调
     * @param deploy   延迟多少秒
     */
    public static void TaskOneTime(TaskCallback callback, int deploy) {
        getTimer().schedule(new TimerTask() {
            public void run() {
                callback.call();
            }
        }, deploy);// 设定指定的时间time,单位为毫秒
    }

    /**
     * 每天定时执行任务
     *
     * @param callback 回调
     * @param time     开始执行时间
     */
    public static void TaskEveryDay(TaskCallback callback, Date time) {
        getTimer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                callback.call();
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
    }

    /**
     * 定时三分钟执行任务
     *
     * @param callback 回调
     */
    public static void TaskThreeMinute(TaskCallback callback) {
        getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                callback.call();
            }
        }, 3 * 60 * 1000, 3 * 60 * 1000);
    }

    /**
     * 关闭所有任务，并重置
     */
    public static void stop() {
        if (timer != null) {
            synchronized (TaskUtil.class) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        }
    }
}
