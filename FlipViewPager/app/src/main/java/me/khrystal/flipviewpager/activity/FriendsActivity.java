package me.khrystal.flipviewpager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.khrystal.flipviewpager.R;
import me.khrystal.flipviewpager.Utils;
import me.khrystal.flipviewpager.pojo.Friend;
import me.khrystal.widget.flipviewpager.adapter.BaseFlipAdapter;
import me.khrystal.widget.flipviewpager.utils.FlipSettings;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/29
 * update time:
 * email: 723526676@qq.com
 */
public class FriendsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final RecyclerView friends = findViewById(R.id.friends);

        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
        // TODO: 19/4/29
        friends.setLayoutManager(new LinearLayoutManager(this));
        friends.setAdapter(new FriendsAdapter(this, Utils.friends, settings));
    }

    class FriendsAdapter extends BaseFlipAdapter {

        private final int PAGES = 3;
        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4, R.id.interest_5};

        public FriendsAdapter(Context context, List items, FlipSettings settings) {
            super(context, items, settings);
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, Object item1, Object item2, CloseListener closeListener) {
            final FriendsHolder holder;
            if (convertView == null) {
                holder = new FriendsHolder();
                convertView = getLayoutInflater().inflate(R.layout.friends_merge_page, parent, false);
                holder.leftAvatar = convertView.findViewById(R.id.first);
                holder.rightAvatar = convertView.findViewById(R.id.second);
                holder.infoPage = getLayoutInflater().inflate(R.layout.friends_info, parent, false);
                holder.nickName = holder.infoPage.findViewById(R.id.nickname);

                for (int id : IDS_INTEREST) {
                    holder.interests.add((TextView) holder.infoPage.findViewById(id));
                }
                convertView.setTag(holder);
            } else {
                holder = (FriendsHolder) convertView.getTag();
            }

            switch (position) {
                // merged page with 2 friends
                case 1:
                    holder.leftAvatar.setImageResource(((Friend) item1).getAvatar());
                    if (item2 != null) {
                        holder.rightAvatar.setImageResource(((Friend) item2).getAvatar());
                    }
                    break;
                default:
                    fillHolder(holder, position == 0 ? (Friend) item1 : (Friend) item2);
                    holder.infoPage.setTag(holder);
                    return holder.infoPage;
            }
            return convertView;
        }

        @Override
        public int getPagesCount() {
            return PAGES;
        }

        private void fillHolder(FriendsHolder holder, Friend friend) {
            if (friend == null)
                return;
            Iterator<TextView> iViews = holder.interests.iterator();
            Iterator<String> iInterests = friend.getInterests().iterator();
            while (iViews.hasNext() && iInterests.hasNext()) {
                iViews.next().setText(iInterests.next());
            }
            holder.infoPage.setBackgroundColor(getResources().getColor(friend.getBackground()));
            holder.nickName.setText(friend.getNickname());
        }

        class FriendsHolder {
            ImageView leftAvatar;
            ImageView rightAvatar;
            View infoPage;

            List<TextView> interests = new ArrayList<>();
            TextView nickName;
        }
    }
}
