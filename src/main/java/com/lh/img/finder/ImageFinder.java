package com.lh.img.finder;

import com.lh.img.Coordinate;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 屏幕图片查找器
 *
 * @auther: loneyfall
 * @date: 2020/9/27
 * @description:
 */
public interface ImageFinder {
    /**
     * 查询匹配图片坐标
     *
     * @param image
     * @param percent
     * @return
     */
    List<Coordinate> match(BufferedImage image, double percent);

    /**
     * 转换为中心点
     *
     * @param image
     * @param list
     * @return
     */
    List<Coordinate> getCenter(BufferedImage image, List<Coordinate> list);

    /**
     * 重置当前屏幕
     */
    void resetScreen();
}
