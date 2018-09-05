package me.khrystal.widget.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * usage: 一个既可以编辑又可以下拉选择的自定义View
 * 1. 获得一个列表内容 在编辑框文本变化时 动态显示相关列表内容 列表内容通过服务端获取 当获取到数据后显示下拉弹窗展示内容
 * 2. 点击列表item能够修改编辑框显示文本 同时触发文本修改变化回调, 文本修改后光标改到文本末尾(完成)
 * 3. 当Focus发生变化且是列表选中时 显示相关列表内容
 * 4. 点击选中项触发事件 获得选中的bean
 * 5. 支持自定义TextSize
 * 6. 支持设置popupwindow中文本大小与颜色
 * 7. 支持设置popupwindow弹出框高度
 * 8. 在弹出框显示之前支持回调 控制edittext在屏幕内移动
 * author: kHRYSTAL
 * create time: 18/9/5
 * update time:
 * email: 723526676@qq.com
 */
public class SpinnerEditText<T> extends AppCompatEditText {
    private Context context;
    private int childHeight;

    private boolean isNecessary = false;//是否是必须条件
    private boolean isItemClickCauseChange;
    public static final int TYPE_UP = 0;//Pop向上显示
    public static final int TYPE_DOWN = 1;//Pop向下显示

    public int showType = TYPE_UP;// Popupwindow显示类型

    private int popTextColor;
    private float popTextSize;
    private float popMinHeight;
    private float popMaxHeight;

    public SpinnerEditText(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public SpinnerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public SpinnerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }


    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            return false;
        }
        return super.onTextContextMenuItem(id);
    }


    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpinnerEditText);
            popTextColor = typedArray.getColor(R.styleable.SpinnerEditText_pop_textcolor, Color.BLACK);
            popTextSize = typedArray.getDimension(R.styleable.SpinnerEditText_pop_textsize, 0f);
            popMinHeight = typedArray.getDimension(R.styleable.SpinnerEditText_pop_min_height, 40f);
            popMaxHeight = typedArray.getDimension(R.styleable.SpinnerEditText_pop_max_height, 0f);
            typedArray.recycle();
        }

        setLongClickable(false);
        childHeight = dp2px(context, 40);

        this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        //文本变换事件
        addTextChangedListener(new TextWatchAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (popupWindow != null && SpinnerEditText.this.hasFocus()) {
                    // TODO 发起网络请求 获取需要的列表数据
                    // TODO 执行setList后再执行 betterShow
                    // 点击item 引起的edittext改变 不进行弹窗
                    if (charSequence.length() >= 2 && !isItemClickCauseChange) {
                        betterShow(250);
                    } else {
                        isItemClickCauseChange = false;
                    }
                }
            }
        });

        //焦点变换事件
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (popupWindow != null && hasFocus && getText().toString().length() >= 2) {
                    // TODO 发起网络请求 获取需要的列表数据
                    // TODO 执行setList后再执行 betterShow
                    betterShow(250);
                } else {
                    dismissPopupWindow();
                }

                for (OnFocusChangeListener onFocusChangeListener : onFocusChangeListenerList) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dismissPopupWindow();
            showPopupWindow();
            popupWindowIsShowing = true;
        }
    };


    /**
     * 在网络请求后调用改方法执行弹窗显示
     * @param delayTime
     */
    //优化过后的Popupwindo显示方法
    public void betterShow(long delayTime) {
        Message message = Message.obtain();
        handler.sendMessageDelayed(message, delayTime);
    }

    private class ViewHolder {
        TextView itemTextView;
    }

    public void setList(List<T> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        initOrUpdateListPopupWindow();
    }

    private Map<String, List<T>> map = new HashMap<>();//根据文本值获得对应的集合

    private List<T> getFilterList(String key, List<T> addList) {
        if (map.get(key) == null) {
            List<T> list = new ArrayList<>();
            list.addAll(addList);
            map.put(key, list);
        } else if (isAlwaysClearList()) {
            map.put(key, addList);
        }
        return map.get(key);
    }

    //显示当前文本下的列表
    private void showPopupWindow() {
        if (!needShowSpinner) return;
        if (!itemList.isEmpty()) {
            updateHeightAndShow();
        } else {
            dismissPopupWindow();
        }
    }


    protected void dismissPopupWindow() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, SpinnerEditText<T> var1, View var2, int position, long var4, String selectContent);
    }

    private OnItemClickListener<T> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<OnFocusChangeListener> onFocusChangeListenerList = new ArrayList<>();

    public void addOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        onFocusChangeListenerList.add(onFocusChangeListener);
    }

    //获得当前值
    public String getValue() {
        String value = getText().toString();
        if (value.equals("null")) {
            return null;
        }
        return value.trim();
    }

    private T selectedItem;

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    private boolean alwaysClearList = true;//是否总是清空集合

    public boolean isAlwaysClearList() {
        return alwaysClearList;
    }

    public void setAlwaysClearList(boolean alwaysClearList) {
        this.alwaysClearList = alwaysClearList;
    }

    private boolean needShowSpinner = true;//是否需要显示下拉框

    public boolean isNeedShowSpinner() {
        return needShowSpinner;
    }

    public void setNeedShowSpinner(boolean needShowSpinner) {
        this.needShowSpinner = needShowSpinner;
    }

    public int selectedItemPosition;

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    //------------------------------初始化Popupwindow ----------------------------
    private static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1;
    private boolean popupWindowIsShowing = false;//当前Popupwindow是否正在显示
    private PopupWindow popupWindow;
    private List<T> itemList = new ArrayList<>();
    private BaseAdapter adapter;
    private ListView listView;
    private FrameLayout popupView;
    public List<T> getItemList() {
        return itemList;
    }


    @SuppressLint({"WrongConstant", "NewApi"})
    private void initOrUpdateListPopupWindow() {

        if (popupWindow == null) {

            popupWindow = new PopupWindow(context);
            listView = new ListView(context);
            setVerticalScrollBarEnabled(true);
            listView.setBackground(getResources().getDrawable(R.drawable.graybox));
            popupView = new FrameLayout(context);
            popupView.setBackgroundColor(Color.GRAY);
            popupView.addView(listView);
            popupWindow.setContentView(popupView);
//            popupView.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return itemList.size();
                }

                @Override
                public T getItem(int position) {
                    return itemList.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder holder = null;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = LayoutInflater.from(context).inflate(R.layout.item_listpopupwindow, null, false);
                        holder.itemTextView = (TextView) convertView.findViewById(R.id.tv);
                        convertView.setTag(holder);

                        if (popTextColor != 0)
                            holder.itemTextView.setTextColor(popTextColor);
                        if (popTextSize != 0)
                            holder.itemTextView.setTextSize(px2dp(context, popTextSize));

                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }

                    if (itemList != null) {
                        final String itemName = itemList.get(position).toString();
                        if (holder.itemTextView != null) {
                            holder.itemTextView.setText(itemName);
                        }
                    }

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            T t = itemList.get(position);

                            int i = position;
                            for (int i1 = 0; i1 < itemList.size(); i1++) {
                                if (itemList.get(i1).toString().equals(t.toString())) {
                                    i = i1;
                                    break;
                                }
                            }

                            String selectedContent = t.toString();

                            setSelectedItemPosition(i);
                            SpinnerEditText.this.setSelectedItem(t);
                            if (onItemClickListener != null) {
                                isItemClickCauseChange = true;
                                onItemClickListener.onItemClick(t, SpinnerEditText.this, v, i, i, selectedContent);
                            }

                            if (!itemList.isEmpty() && i < itemList.size()) {
                                SpinnerEditText.this.setText(selectedContent);
                                setSelectedItem(itemList.get(i));
                                setSelectedItemPosition(i);
                            } else {
                                setText("");
                            }

                            setSelection(getText().toString().length());
                            handler.removeMessages(1);
                            popupWindow.dismiss();
                        }
                    });

                    return convertView;
                }
            };


            listView.setAdapter(adapter);
            popupWindow.setWidth(AbsListView.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(AbsListView.LayoutParams.WRAP_CONTENT);
            popupWindow.setSoftInputMode(ListPopupWindow.INPUT_METHOD_NEEDED);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindowIsShowing = false;
                }
            });


            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.AnimationFromButtom);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(false);
        }
        adapter.notifyDataSetChanged();
    }

    private int willShowHeight;

    private void updateHeightAndShow() {
        post(new Runnable() {
            @Override
            public void run() {

                willShowHeight = itemList.size() * childHeight;
                if (popMaxHeight > 0 && willShowHeight > popMaxHeight) {
                    willShowHeight = (int) popMaxHeight;
                }

                Rect rect = new Rect();
                getGlobalVisibleRect(rect);

                if (willShowHeight < popMinHeight)
                    willShowHeight = (int) popMinHeight;

                popupWindow.setHeight(willShowHeight);
                listView.setLayoutParams(new FrameLayout.LayoutParams(getWidth(), willShowHeight));

                initOrUpdateListPopupWindow();


                if (showType == TYPE_UP) {
                    showAsPopUp(SpinnerEditText.this);
                } else {
                    showAsPopBottom(SpinnerEditText.this);
                }

            }
        });
    }

    /**
     * anchor上方
     *
     * @param anchor
     */
    public void showAsPopUp(View anchor) {
        showAsPopUp(anchor, 0, dp2px(context, 0));
    }

    public void showAsPopBottom(View anChor) {
        popupWindow.showAsDropDown(anChor, 0, 0);
    }

    private void showAsPopUp(View anchor, int xoff, int yoff) {
        popupWindow.setAnimationStyle(R.style.AnimationUpPopup);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int[] location = new int[2];
        anchor.getLocationInWindow(location);


        //计算显示位置 如果高度不够自动调整到合适高度
        int offsetY = -getHeight() - willShowHeight;
        if (offsetY + location[1] < 0) {
            popupWindow.setHeight(location[1] - getHeight() / 2);
            listView.setLayoutParams(new FrameLayout.LayoutParams(getWidth(), location[1] - getHeight() / 2));
        }

        popupWindow.showAsDropDown(anchor, 0, offsetY);
    }

    public void dismissPop() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }


    //------------------------------初始化Popupwindow ----------------------------


    /**
     * 根据手机的分辨率从 DP 的单位 转成为PX(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //-----------设置自动判断Popupwindow显示类型---------------------

    //手动设置显示类型 自动判断显示类型失效
    public void setShowType(int showType) {
        this.showType = showType;
    }

    //-----------设置自动判断Popupwindow显示类型---------------------

    //-------------------根据文本值为空判断状态是否为异常-----------------
    public void setNecessary(boolean necessary) {
        isNecessary = necessary;
    }

    public boolean isNecessary() {
        return isNecessary;
    }
    //-------------------根据文本值为空判断状态是否为异常-----------------
}