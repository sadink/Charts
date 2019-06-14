# Charts
图表库


引用：

    api 'com.lhf.charts:Charts:0.0.1'


布局

    `<?xml version="1.0" encoding="utf-8"?> 
     <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:app="http://schemas.android.com/apk/res-auto"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:background="@color/white"
         android:gravity="center_horizontal"
         android:paddingTop="10dp">

      <com.lhf.charts.widget.rose.RoseChartView
        android:id="@+id/rose_chartView"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp"
        app:roseArcStartAngle="-45"
        app:roseIsLabelLineColorFromLeaf="false"
        app:roseRadiusCircleLegal="60dp" />

    </RelativeLayout>`

调用

     List<RoseEntity> listRoseLeafs = new ArrayList<>();
        listRoseLeafs.add(new RoseEntity("面粉", 0.19f, 0.33f, R.color.red_1));
        listRoseLeafs.add(new RoseEntity("白砂糖", 0.21f, 0.27f, R.color.orange_2));
        listRoseLeafs.add(new RoseEntity("果仁1", 0.18f, 0.20f, R.color.green_2));
        listRoseLeafs.add(new RoseEntity("果仁2", 0.28f, 0.13f, R.color.blue_3));
        listRoseLeafs.add(new RoseEntity("果仁3", 0.14f, 0.07f, R.color.purple_3));
        roseChartView.setDataRoseLeaf(listRoseLeafs);


属性：

    <declare-styleable name="RoseLeafChart">
        <attr format="integer"  name="roseArcStartAngle"/>
        <attr format="color" name="roseLabelTextColor"/>
        <attr format="dimension" name="roseRadiusCircleLegal"/>
        <attr format="boolean" name="roseIsLabelLineColorFromLeaf"/>
        <attr format="color" name="roseCircleLegalColor"/>
        <attr format="color" name="roseCircleSideColor"/>
    </declare-styleable>

属性说明

1. roseArcStartAngle：第一个花瓣的起始角度
1. roseLabelTextColor：花瓣标签颜色
2. roseRadiusCircleLegal：所有花瓣总面积圆-圆半径
3. roseCircleLegalColor：所有花瓣总面积圆-圆半径颜色
3. roseIsLabelLineColorFromLeaf：花瓣标签是颜色是否和花瓣颜色一致
4. roseCircleSideColor：以最大花瓣半径为基础，平分的指示圈周颜色




