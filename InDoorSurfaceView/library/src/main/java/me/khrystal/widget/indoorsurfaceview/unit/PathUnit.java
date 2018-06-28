package me.khrystal.widget.indoorsurfaceview.unit;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

import java.util.List;

/**
 * usage: 集合了坐标点和判断Region
 * author: kHRYSTAL
 * create time: 18/6/28
 * update time:
 * email: 723526676@qq.com
 */

public class PathUnit {
    public String name;
    public String id;
    public Region region;
    public Path path;

    public PathUnit(List<PointF> list) {
        int i = 0;
        path = new Path();
        for (PointF pointF : list) {
            if (i==0)
                path.moveTo(pointF.x, pointF.y);
            path.lineTo(pointF.x, pointF.y);
            i++;
        }
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
