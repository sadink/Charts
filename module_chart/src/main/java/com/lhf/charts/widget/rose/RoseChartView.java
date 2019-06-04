package com.lhf.charts.widget.rose;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lhf.charts.R;
import com.lhf.charts.base.BaseConfig;
import com.lhf.charts.mode.entity.RoseEntity;
import com.lhf.charts.utils.DensityUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 南丁格尔玫瑰图
 * Created by lhfBoy on 2019/5/28 0028 11:33.
 */
public class RoseChartView extends View {


    private Context context;

    private int width;
    private int height;
    //图形的中心点
    private int centerX;
    private int centerY;
    private Paint paintLeaf;
    private Paint paintCircleLegal;
    private float radiusCircleLegal;// 设置标准圆面积半径
    private float areaCircleLegal = 0;
    private int startAngle; // 设置第一个花瓣的起始角度
    private List<RoseEntity> listRoseLeaf = new ArrayList<>();

    private boolean isDrawLabel = true;
    private Paint paintLineLabel;
    private float lineLabelBroken;
    private float radiusLeafMax;
    private Paint paintLabel;
    private boolean isLabelLineColorFromLeaf;
    private int labelTextColor;
    private int circleLegalColor;
    private int circleSideColor;

    public RoseChartView(Context context) {
        this(context, null);
    }

    public RoseChartView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoseChartView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoseLeafChart, defStyleAttr, 0);
        labelTextColor = typedArray.getColor(R.styleable.RoseLeafChart_roseLabelTextColor, Color.parseColor("#cccccc"));
        startAngle = typedArray.getInt(R.styleable.RoseLeafChart_roseArcStartAngle, 0);
        radiusCircleLegal = typedArray.getDimension(R.styleable.RoseLeafChart_roseRadiusCircleLegal, DensityUtils.dpTopx(60));
        circleLegalColor = typedArray.getColor(R.styleable.RoseLeafChart_roseCircleLegalColor, Color.parseColor("#333333"));
        circleSideColor = typedArray.getColor(R.styleable.RoseLeafChart_roseCircleSideColor, Color.parseColor("#cccccc"));
        isLabelLineColorFromLeaf = typedArray.getBoolean(R.styleable.RoseLeafChart_roseIsLabelLineColorFromLeaf, false);

        typedArray.recycle();

        if (!(startAngle >= -90 && startAngle < 360)) {
            startAngle = 0;
            return;
        }
        init();
    }

    private void init() {

        paintCircleLegal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircleLegal.setDither(true);
        paintCircleLegal.setTextSize(DensityUtils.sp2px(context, 12));
        paintCircleLegal.setStrokeWidth(DensityUtils.dpTopx(0.5f));
        paintCircleLegal.setStyle(Paint.Style.STROKE);

        paintLeaf = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLeaf.setDither(true);
        paintLeaf.setTextSize(DensityUtils.sp2px(context, 12));
        paintLeaf.setStrokeWidth(DensityUtils.dpTopx(0.5f));
        paintLeaf.setStyle(Paint.Style.FILL_AND_STROKE);

        paintLineLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLineLabel.setDither(true);
        paintLineLabel.setTextSize(DensityUtils.sp2px(context, 12));
        paintLineLabel.setStrokeWidth(DensityUtils.dpTopx(0.5f));
        paintLineLabel.setStyle(Paint.Style.STROKE);


        paintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLabel.setDither(true);
        paintLabel.setTextSize(DensityUtils.sp2px(context, 12));
        paintLabel.setStyle(Paint.Style.FILL);

        lineLabelBroken = DensityUtils.dpTopx(10);
    }

    /**
     * 当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        KLog.d(BaseConfig.LOG, "onFinishInflate()");
    }

    /**
     * 在View放置到父容器时调用
     * 作用：测量View的大小，也可以通过下面方式，修改View的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        height = width;
        setMeasuredDimension(width, height);
    }


    /**
     * 在控件大小发生改变时调用。所以这里初始化会被调用一次
     * 作用：获取控件的宽和高度
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = (w - getPaddingStart() - getPaddingEnd()) / 2;
        centerY = (h - getPaddingTop() - getPaddingBottom()) / 2;

        KLog.e(BaseConfig.LOG, "centerX：" + centerX + ",centerY：" + centerY);
    }

    /**
     * 最后绘制图形
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        drawCircleStandard(canvas);
        drawCircleSideLines(canvas);
        drawLineLabel(canvas);
        drawRoseLeaf(canvas);
    }


    /**
     * 画标准面积圆
     *
     * @param canvas
     */
    private void drawCircleStandard(Canvas canvas) {
        paintCircleLegal.setColor(circleLegalColor);
        canvas.drawCircle(0, 0, radiusCircleLegal, paintCircleLegal);
    }

    /**
     * 画每个扇形的圆的指示边线
     */
    private void drawCircleSideLines(Canvas canvas) {
        paintCircleLegal.setColor(circleSideColor);
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            int radio = (int) (radiusLeafMax / listRoseLeaf.size() * (i + 1));
            canvas.drawCircle(0, 0, radio, paintCircleLegal);
        }
    }


    /**
     * 画花瓣
     *
     * @param canvas
     */
    private void drawRoseLeaf(Canvas canvas) {
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            paintLeaf.setColor(getResources().getColor(listRoseLeaf.get(i).getColorLeaf()));
            canvas.drawArc(
                    new RectF(-listRoseLeaf.get(i).getRadiusLeaf(), -listRoseLeaf.get(i).getRadiusLeaf(), listRoseLeaf.get(i).getRadiusLeaf(), listRoseLeaf.get(i).getRadiusLeaf()),
                    listRoseLeaf.get(i).getArcLeafStart(),
                    listRoseLeaf.get(i).getArcLeaf(),
                    true,
                    paintLeaf
            );
        }
    }


    /**
     * 画label标签和延长线
     *
     * @param canvas
     */
    private void drawLineLabel(Canvas canvas) {
        for (int i = 0; i < listRoseLeaf.size() && isDrawLabel; i++) {
            float cos = (float) Math.cos(Math.toRadians(listRoseLeaf.get(i).getArcLeafHalf())); // 角度转弧度后计算余弦值
            float sin = (float) Math.sin(Math.toRadians(listRoseLeaf.get(i).getArcLeafHalf())); // 角度转弧度后计算正弦值
//
//        //通过正余弦算出延长点的坐标
            float xCirclePoint = (radiusLeafMax + lineLabelBroken) * cos;
            float yCirclePoint = (radiusLeafMax + lineLabelBroken) * sin;

            if (isLabelLineColorFromLeaf) {
                paintLabel.setColor(getResources().getColor(listRoseLeaf.get(i).getColorLeaf()));
                paintLineLabel.setColor(getResources().getColor(listRoseLeaf.get(i).getColorLeaf()));
            } else {
                paintLabel.setColor(labelTextColor);
                paintLineLabel.setColor(labelTextColor);
            }

            float xLineStartPoint = 0;
            float yLineStartPoint = 0;
            float xLineTurningPoint = 0;
            float yLineTurningPoint = 0;
            float xLineEndPoint = 0;
            float yLineEndPoint = 0;

            float quadrant = listRoseLeaf.get(i).getArcLeafHalf();
            if (quadrant > 360) {
                quadrant = quadrant - 360;
            }
            float extend = paintLineLabel.measureText(listRoseLeaf.get(i).getLabel()) + lineLabelBroken;

            // 第一象限
            if (quadrant >= 0 && quadrant < 90) {
                xLineStartPoint = xCirclePoint;
                yLineStartPoint = yCirclePoint;
                xLineTurningPoint = xLineStartPoint;
                yLineTurningPoint = yLineStartPoint;
                xLineEndPoint = xLineTurningPoint + extend;
                yLineEndPoint = yLineTurningPoint;

                paintLabel.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(listRoseLeaf.get(i).getLabel(), xLineEndPoint, yLineEndPoint - 5, paintLabel);
            } else if (quadrant >= 90 && quadrant < 180) {
                // 第二象限
                xLineStartPoint = xCirclePoint;
                yLineStartPoint = yCirclePoint;
                xLineTurningPoint = xLineStartPoint;
                yLineTurningPoint = yLineStartPoint;
                xLineEndPoint = xLineTurningPoint - extend;
                yLineEndPoint = yLineTurningPoint;

                paintLabel.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(listRoseLeaf.get(i).getLabel(), xLineEndPoint, yLineEndPoint - 5, paintLabel);
            } else if (quadrant >= 180 && quadrant < 270) {
                // 第三象限
                xLineStartPoint = xCirclePoint;
                yLineStartPoint = yCirclePoint;
                xLineTurningPoint = xLineStartPoint;
                yLineTurningPoint = yLineStartPoint;
                xLineEndPoint = xLineTurningPoint - extend;
                yLineEndPoint = yLineTurningPoint;

                paintLabel.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(listRoseLeaf.get(i).getLabel(), xLineEndPoint, yLineEndPoint - 5, paintLabel);
            } else if ((quadrant >= -90 && quadrant < 0) || (quadrant >= 270 && quadrant < 360)) {
                // 第四象限
                xLineStartPoint = xCirclePoint;
                yLineStartPoint = yCirclePoint;
                xLineTurningPoint = xLineStartPoint;
                yLineTurningPoint = yLineStartPoint;
                xLineEndPoint = xLineTurningPoint + extend;
                yLineEndPoint = yLineTurningPoint;

                paintLabel.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(listRoseLeaf.get(i).getLabel(), xLineEndPoint, yLineEndPoint - 5, paintLabel);
            }
            //绘制延长线
            canvas.drawLine(0, 0, xLineTurningPoint, yLineTurningPoint, paintLineLabel);
            canvas.drawLine(xLineTurningPoint, yLineTurningPoint, xLineEndPoint, yLineEndPoint, paintLineLabel);
        }
    }

    /**
     * 设置执行动画
     */
    public void executeAnim() {
        isDrawLabel = false;
        final float arcleafLast = listRoseLeaf.get(listRoseLeaf.size() - 1).getArcLeaf();
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, listRoseLeaf.get(i).getArcLeaf());
            valueAnimator.setDuration(500);
            final int j = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) valueAnimator.getAnimatedValue();
                    listRoseLeaf.get(j).setArcLeaf(value); // 设置转过角
                    // 设置起始角
                    int arcAngle = 0;
                    for (int k = 0; k < j; k++) {
                        arcAngle += listRoseLeaf.get(k).getArcLeaf();
                    }
                    listRoseLeaf.get(j).setArcLeafStart(startAngle + arcAngle);
                    listRoseLeaf.get(j).setArcLeafHalf(listRoseLeaf.get(j).getArcLeafStart() + listRoseLeaf.get(j).getArcLeaf() / 2.0f);

                    if (j == listRoseLeaf.size() - 1 && value == arcleafLast) {
                        isDrawLabel = true;
                    }
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    /**
     * 获取扇形半径
     *
     * @param areaRatio
     * @param n
     * @param i
     * @return
     */
    private float getRadius(float areaRatio, float n, int i) {
        // 计算扇形面积
        float s = Math.round(getAreaLegal() * areaRatio);
        listRoseLeaf.get(i).setAreaLeaf(s);
        // 计算扇形半径
        float r = (float) Math.sqrt(360 * s / Math.PI / n);
        return Math.round(r);
    }

    /**
     * 获取标准面积圆
     *
     * @return
     */
    private float getAreaLegal() {
        if (areaCircleLegal <= 0) {
            areaCircleLegal = Math.round(Math.PI * radiusCircleLegal * radiusCircleLegal);
            KLog.e(BaseConfig.LOG, "标准半径：" + radiusCircleLegal + ",标准面积：" + areaCircleLegal);
        }
        return areaCircleLegal;
    }

    /**
     * 设置数据
     *
     * @param roseLeafList
     */
    public void setDataRoseLeaf(List<RoseEntity> roseLeafList) {

        listRoseLeaf.clear();
        listRoseLeaf.addAll(roseLeafList);

        // 计算转过角度
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            listRoseLeaf.get(i).setArcLeaf(Math.round(listRoseLeaf.get(i).getArcRatioLeaf() * 360));
        }

        // 面积比转换半径比
        radiusLeafMax = 0;
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            listRoseLeaf.get(i).setRadiusLeaf(getRadius(listRoseLeaf.get(i).getAreaRatioLeaf(), listRoseLeaf.get(i).getArcLeaf(), i));
            if (radiusLeafMax < listRoseLeaf.get(i).getRadiusLeaf()) {
                radiusLeafMax = listRoseLeaf.get(i).getRadiusLeaf();
            }
        }

        Collections.sort(listRoseLeaf); // 按照半径排序升序

        // 计算起始角、半角
        for (int i = 0; i < listRoseLeaf.size(); i++) {
            int arcAngle = 0;
            for (int j = 0; j < i; j++) {
                arcAngle += listRoseLeaf.get(j).getArcLeaf();
            }
            listRoseLeaf.get(i).setArcLeafStart(startAngle + arcAngle);
            listRoseLeaf.get(i).setArcLeafHalf(listRoseLeaf.get(i).getArcLeafStart() + listRoseLeaf.get(i).getArcLeaf() / 2.0f);
        }

        invalidate();
    }


}
