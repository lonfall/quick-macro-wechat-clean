package com.lh.util;

import com.lh.img.Coordinate;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * @auther: loneyfall
 * @date: 2020/9/28
 * @description:
 */
public class RootUtil {
    private volatile static Robot robot;

    private static Robot getRoot() throws AWTException {
        if (robot == null) {
            synchronized (TaskUtil.class) {
                if (robot == null) {
                    robot = new Robot();
                }
            }
        }
        return robot;
    }

    public static void click(Coordinate coordinate, int deploy) throws AWTException {
        Robot robot = getRoot();
        //模拟移动鼠标
        robot.mouseMove(coordinate.getX(), coordinate.getY());
        robot.delay(100);
        //模拟鼠标按下左键
        robot.mousePress(InputEvent.BUTTON1_MASK);
        //模拟鼠标松开左键
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(deploy);
    }

    public static void delay(int deploy) {
        try {
            Robot robot = getRoot();
            robot.delay(deploy);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
