package me.khrystal.widget;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/3
 * update time:
 * email: 723526676@qq.com
 */

public interface ListenerStubs {

    class OverScrollStateListenerStub implements IOverScrollStateListener {

        @Override
        public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {

        }
    }

    class OverScrollUpdateListenerStub implements IOverScrollUpdateListener {

        @Override
        public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {

        }
    }
}
