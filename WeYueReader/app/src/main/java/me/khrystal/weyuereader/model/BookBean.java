package me.khrystal.weyuereader.model;

import java.io.Serializable;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class BookBean implements Serializable {
    /**
     * _id : 59ba0dbb017336e411085a4e
     * title : 元尊
     * author : 天蚕土豆
     * longIntro : 彼时的归途，已是一条命运倒悬的路。 昔日的荣华，如白云苍狗，恐大梦一场。 少年执笔，龙蛇飞动。 是为一抹光芒劈开暮气沉沉之乱世，问鼎玉宇苍穹。 复仇之路，与吾同行。 一口玄黄真气定可吞天地日月星辰，雄视草木苍生。 铁画夕照，雾霭银钩，笔走游龙冲九州。 横姿天下，墨洒青山，鲸吞湖海纳百川。
     * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F2107590%2F2107590_55d1f1bf10684e62a51d9f0ca3dd08fc.jpg%2F
     * majorCate : 玄幻
     * minorCate : 东方玄幻
     * hasCopyright : true
     * contentType : txt
     * latelyFollower : 95840
     * wordCount : 432088
     * serializeWordCount : 3717
     * retentionRatio : 51.69
     * updated : 2017-12-06T15:49:21.246Z
     * chaptersCount : 169
     * lastChapter : 正文 第一百六十八章 再遇
     * rating : {"count":6755,"score":8.119,"isEffect":true}
     * tags : []
     * gender : ["male"]
     */

    private String _id;
    private String title;
    private String author;
    private String longIntro;
    private String cover;
    private String majorCate;
    private String minorCate;
    private boolean hasCopyright;
    private boolean isCollect;
    private String contentType;
    private int latelyFollower;
    private int wordCount;
    private int serializeWordCount;
    private String retentionRatio;
    private String updated;
    private int chaptersCount;
    private String lastChapter;
    private String copyright;
    private RatingBean rating;
    private List<String> tags;
    private List<String> gender;


    public static class RatingBean implements Serializable {
        /**
         * count : 6755
         * score : 8.119
         * isEffect : true
         */
        private int count;
        private double score;
        private boolean isEffect;

        public int getCount() {
            return count;
        }

        public double getScore() {
            return score;
        }

        public boolean isEffect() {
            return isEffect;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public void setEffect(boolean effect) {
            isEffect = effect;
        }
    }

}
