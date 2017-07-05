package me.khrystal.animationplayground;

import com.example.mwajeeh.animations.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/7/5
 * update time:
 * email: 723526676@qq.com
 */

public class Categories {
    private static List<Category> categories = generateItems();

    public static List<Category> getCategories() {
        return categories;
    }

    private static List<Category> generateItems() {
        Random rand = new Random();
        List<Category> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add(new Category("title:" + i, getImage(rand.nextInt(2))));
        }
        return items;
    }


    public static class Category {
        public final String title;
        public final int image;

        private Category(String title, int image) {
            this.title = title;
            this.image = image;
        }
    }

    private static int getImage(int position) {
        switch (position) {
            case 0:
                return R.drawable.image_1;
            default:
                return R.drawable.image_2;
        }
    }
}
