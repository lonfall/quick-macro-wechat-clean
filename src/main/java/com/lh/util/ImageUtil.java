package com.lh.util;

import com.lh.img.Coordinate;
import com.lh.img.finder.ImageFinder;
import com.lh.img.finder.impl.ScreenImageFinder;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @auther: loneyfall
 * @date: 2020/9/27
 * @description:
 */
public class ImageUtil {
    private static Logger logger = Logger.getLogger(ImageUtil.class);

    private volatile static ImageFinder imageFinder;

    public static int[][] getRGB(BufferedImage image) {
        int width = image.getWidth(),
                height = image.getHeight(),
                minWidth = image.getMinX(),
                minHeigth = image.getMinY();

        int[][] rgbs = new int[width][height];
        for (int x = minWidth; x < width; x++) {
            for (int y = minHeigth; y < height; y++) {
                rgbs[x][y] = image.getRGB(x, y);
            }
        }

        return rgbs;
    }

    public static ImageFinder getImageFinderInstance() {
        if (imageFinder == null) {
            synchronized (ImageFinder.class) {
                if (imageFinder == null) {
                    imageFinder = new ScreenImageFinder();
                }
            }
        }
        return imageFinder;
    }

    public static List<Coordinate> findCoordinateByImg(BufferedImage search) {
        long start = System.currentTimeMillis();
        getImageFinderInstance().resetScreen();
        List<Coordinate> coordinateList = getImageFinderInstance().match(search, 0.9);
        logger.info("寻找图片,耗时:" + (double) ((System.currentTimeMillis() - start) / 1000) + "s");
        coordinateList = coordinateList.stream().filter(Objects::nonNull).map(coordinate -> {
            logger.info(String.format("获取图片位置: x:%d,y:%d", coordinate.getX(), coordinate.getY()));
            return coordinate;
        }).collect(Collectors.toList());

        return coordinateList;
    }
}
