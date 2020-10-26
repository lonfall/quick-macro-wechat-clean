package com.lh;

import com.lh.img.Coordinate;
import com.lh.util.ImageUtil;
import com.lh.util.RootUtil;
import com.lh.util.TaskUtil;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自动清理微信应用缓存
 * 注意：使用前请替换img资源文件，为对应系统的截图，否则无法找到定位点
 *
 * @auther: loneyfall
 * @date: 2020/9/27
 * @description:
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    static List<BufferedImage> queue = new ArrayList<>();

    public static void main(String[] args) {
        // 设定任务开始时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时
        calendar.set(Calendar.MINUTE, 0);       // 控制分
        calendar.set(Calendar.SECOND, 0);       // 控制秒

        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00

        initImageQueue();

        TaskUtil.TaskEveryDay(() -> {
            logger.info("开始清空任务");
            findImgAndClickByQueue(0, 0);
        }, time);
    }

    /**
     * 开始循环图片队列，并点击
     *
     * @param index
     */
    private static void findImgAndClickByQueue(int index, int retry) {
        if (index >= queue.size()) {
            return;
        }
        BufferedImage img = queue.get(index);
        logger.info("查找图片位置,index=" + index + "   --------");
        List<Coordinate> coordinateList = ImageUtil.findCoordinateByImg(img);
        if (!coordinateList.isEmpty()) {
            if (clickByCoordinate(coordinateList, img)) {
                findImgAndClickByQueue(index + 1, 0);
            }
        } else {
            retry++;
            if (retry >= 5) {
                logger.info("重试次数已达上限，将退出任务队列！");
                return;
            }
            logger.warn("未获取到图片坐标，index=" + index);
            logger.info("重试查找图片，index=" + index + ",重试次数：" + retry);
            RootUtil.delay(1000);
            findImgAndClickByQueue(index, retry);
        }
    }

    /**
     * 初始化图片队列
     */
    private static void initImageQueue() {
        try {
            BufferedImage menu = ImageIO.read(Main.class.getResource("/img/menu.png"));
            BufferedImage setting = ImageIO.read(Main.class.getResource("/img/setting.png"));
            BufferedImage generalSettings = ImageIO.read(Main.class.getResource("/img/general_settings.png"));
            BufferedImage clean = ImageIO.read(Main.class.getResource("/img/clean.png"));
            BufferedImage cleanOk = ImageIO.read(Main.class.getResource("/img/clean_ok.png"));

            queue.add(menu);
            queue.add(setting);
            queue.add(generalSettings);
            queue.add(clean);
            queue.add(cleanOk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据坐标点点击（点击图片中点）
     *
     * @param coordinateList
     * @param img
     * @return
     */
    private static boolean clickByCoordinate(List<Coordinate> coordinateList, BufferedImage img) {
        coordinateList = ImageUtil.getImageFinderInstance().getCenter(img, coordinateList);
        try {
            Coordinate coordinate = coordinateList.get(0);
            RootUtil.click(coordinate, 1000);
            return true;
        } catch (AWTException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return false;
    }
}
