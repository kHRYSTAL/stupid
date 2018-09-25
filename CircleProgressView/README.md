### CircleProgressView


#### screenshot

![](http://ww2.sinaimg.cn/large/7853084cjw1f82kyqcjxwg20cz0l940y.gif)

#### attribute

```
<declare-styleable name="CircleProgressView">
    <attr name="duration" format="integer"/>
    <attr name="strokeWidth" format="dimension"/>
    <attr name="pointRadius" format="dimension"/>
    <attr name="strokeColor" format="color"/>
    <attr name="startColor" format="color"/>
    <attr name="endColor" format="color"/>
    <attr name="animRepeatCount" format="integer"/>
    <attr name="centerTextSize" format="dimension"/>
    <attr name="unitTextSize" format="dimension"/>
    <attr name="unitText" format="string"/>
    <attr name="strokeAlpha" format="integer"/>
    <attr name="blurWidth" format="dimension"/>
    <attr name="startAngle" format="float"/>
    <attr name="endAngle" format="float"/>
    <attr name="totalAngle" format="float"/>
</declare-styleable>
```

also you can set attribute in java file 

#### usage

```
mCircleRingView = (CircleProgressView) findViewById(R.id.circle_view);
        mCircleRingView.setGradinetColor(Color.parseColor("#FF26FF00"), Color.parseColor("#FF126912"));
        mCircleRingView.setCenterTextSize(40);
        mCircleRingView.setCenterText(""+0);
        mCircleRingView.setUnitText("kg");
        mCircleRingView.setUnitTextSize(20);
        mCircleRingView.setOnProgressListener(new CircleProgressView.OnProgressListener() {
            @Override
            public void onProgress(float currentValue) {
                mCircleRingView.setCenterText("" + (int)(200 * currentValue));
            }
        });
        
```

