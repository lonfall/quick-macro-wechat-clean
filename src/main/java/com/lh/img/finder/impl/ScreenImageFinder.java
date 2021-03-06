package com.lh.img.finder.impl;

import com.lh.img.ColorBlock;
import com.lh.img.Coordinate;
import com.lh.img.finder.ImageFinder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lh.util.ImageUtil.getRGB;

/**
 * @auther: loneyfall
 * @date: 2020/9/27
 * @description:
 */
public class ScreenImageFinder implements ImageFinder {
    protected Robot robot;
    protected BufferedImage screen;
    protected Dimension dimension;

    public ScreenImageFinder() {
        try {
            dimension = Toolkit.getDefaultToolkit().getScreenSize();
            robot = new Robot();
            screen = robot.createScreenCapture(new Rectangle(0, 0, dimension.width, dimension.height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void resetScreen() {
        screen = robot.createScreenCapture(new Rectangle(0, 0, dimension.width, dimension.height));
        try {
            // 保存截图
            ImageIO.write(screen, "jpeg", new File("D:/tmp/quick-macro-wechat-clean-last-screen.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Coordinate> match(BufferedImage image, double percent) {
        //0 0  left top
        //5 0  right top
        //0 5  left  bottom
        //5 5  right bottom
        //2 2  center
        int[][] screenRgbs = getRGB(screen);
        ColorBlock block = new ColorBlock(image);
        List<Coordinate> coordinates = new ArrayList<>();
        for (int x = 0; x < screen.getWidth() - block.getWidth(); x++) {
            for (int y = 0; y < screen.getHeight() - block.getHeight(); y++) {
                int topLeft = screenRgbs[x][y];
                int topRight = screenRgbs[x + block.getRightTop().getX()][y + block.getRightTop().getY()];
                int bottomLeft = screenRgbs[x + block.getLeftBottom().getX()][y + block.getLeftBottom().getY()];
                int bottomRight = screenRgbs[x + block.getRightBottom().getX()][y + block.getRightBottom().getY()];
                int center = screenRgbs[x + block.getCenter().getX()][y + block.getCenter().getY()];
                if (!block.isPointMatch(topLeft, topRight, bottomLeft, bottomRight, center)) continue;
                coordinates.add(new Coordinate(x, y));
            }
        }

        return coordinates.stream().filter(coordinate -> block.allMatch(coordinate, screenRgbs, percent)).collect(Collectors.toList());
    }

    public List<Coordinate> getCenter(BufferedImage image, List<Coordinate> list) {
        ColorBlock block = new ColorBlock(image);
        return list.stream().filter(Objects::nonNull).map(coordinate -> {
            coordinate.setX(coordinate.getX() + block.getCenter().getX());
            coordinate.setY(coordinate.getY() + block.getCenter().getY());
            return coordinate;
        }).collect(Collectors.toList());
    }

    private boolean colorCompare(int source, int compare) {
        return source == compare;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public BufferedImage getScreen() {
        return screen;
    }

    public void setScreen(BufferedImage screen) {
        this.screen = screen;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
}
