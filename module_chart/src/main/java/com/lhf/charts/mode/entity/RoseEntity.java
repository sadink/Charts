package com.lhf.charts.mode.entity;


import java.io.Serializable;

/**
 * 玫瑰图实体
 * Created by lhfBoy on 2019/5/29 0029 13:52.
 */
public class RoseEntity implements Serializable, Comparable<RoseEntity> {

    private String label; // 标签
    private float arcRatioLeaf; // 角度比率
    private float areaRatioLeaf; // 面积比率
    private int colorLeaf; // 颜色

    private int arcLeaf; // 旋转角度
    private int arcLeafStart; // 起始角度
    private float arcLeafHalf; // 一半的角度（=起始角度+旋转角度/2）
    private float radiusLeaf; // 花瓣半径
    private float areaLeaf; // 面积


    public RoseEntity(String label, float arcRatioLeaf, float areaRatioLeaf, int colorLeaf) {
        this.label = label;
        this.arcRatioLeaf = arcRatioLeaf;
        this.areaRatioLeaf = areaRatioLeaf;
        this.colorLeaf = colorLeaf;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getArcRatioLeaf() {
        return arcRatioLeaf;
    }

    public void setArcRatioLeaf(float arcRatioLeaf) {
        this.arcRatioLeaf = arcRatioLeaf;
    }

    public float getAreaRatioLeaf() {
        return areaRatioLeaf;
    }

    public void setAreaRatioLeaf(float areaRatioLeaf) {
        this.areaRatioLeaf = areaRatioLeaf;
    }

    public int getColorLeaf() {
        return colorLeaf;
    }

    public void setColorLeaf(int colorLeaf) {
        this.colorLeaf = colorLeaf;
    }

    public int getArcLeaf() {
        return arcLeaf;
    }

    public void setArcLeaf(int arcLeaf) {
        this.arcLeaf = arcLeaf;
    }

    public int getArcLeafStart() {
        return arcLeafStart;
    }

    public void setArcLeafStart(int arcLeafStart) {
        this.arcLeafStart = arcLeafStart;
    }

    public float getArcLeafHalf() {
        return arcLeafHalf;
    }

    public void setArcLeafHalf(float arcLeafHalf) {
        this.arcLeafHalf = arcLeafHalf;
    }

    public float getRadiusLeaf() {
        return radiusLeaf;
    }

    public void setRadiusLeaf(float radiusLeaf) {
        this.radiusLeaf = radiusLeaf;
    }

    public float getAreaLeaf() {
        return areaLeaf;
    }

    public void setAreaLeaf(float areaLeaf) {
        this.areaLeaf = areaLeaf;
    }

    @Override
    public int compareTo(RoseEntity o) {
        return (int) (this.getRadiusLeaf() - o.getRadiusLeaf());
    }
}
