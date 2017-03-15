package me.khrystal.widget.gridimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage: 展示九宫格图片的控件
 * author: kHRYSTAL
 * create time: 17/3/15
 * update time:
 * email: 723526676@qq.com
 */

public class GridImageView<T> extends ViewGroup {

    private static final String TAG = GridImageView.class.getSimpleName();

    // 宫格展示
    public static final int STYLE_GRID = 0; // zhisland中大于两张图片时 设置该属性
    // 全填充布局展示
    public static final int STYLE_FILL = 1; // zhisland中等于两张图片时设置该属性

    private int mRowCount; // 行数
    private int mColumnCount; // 列数

    private int mMaxSize; // 最大图片数
    private int mShowStyle; // 显示风格
    private int mGap; // 图片间距
    private int mSingleImageHeight, mSingleImageWidth; // 单一一张图片时展示的宽高(服务器回传宽高后设置)
    private int[] mGridSize; // 图片宽高

    private List<GridChildImageView> mImageViewList = new ArrayList<>();
    private List<T> mImgDataList; // 范型可能是String url 也可能是资源id

    private GridImageViewAdapter<T> mAdapter;
    private ItemImageClickListener<T> mItemImageClickListener;


    public GridImageView(Context context) {
        this(context, null);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GridImageView);
        this.mGap = (int) ta.getDimension(R.styleable.GridImageView_imgGap, 0);
        this.mSingleImageHeight = ta.getDimensionPixelSize(R.styleable.GridImageView_singleImgHeight, -1);
        this.mSingleImageWidth = ta.getDimensionPixelSize(R.styleable.GridImageView_singleImgWidth, -1);
        this.mShowStyle = ta.getInt(R.styleable.GridImageView_showStyle, STYLE_GRID);
        this.mMaxSize = ta.getInt(R.styleable.GridImageView_maxSize, 9);
        ta.recycle();
    }

    /**
     * 计算容器的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int totalHeight = height - getPaddingTop() - getPaddingBottom();
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImgDataList != null) {
            int[] param = calculateGridParam(getNeedShowCount(mImgDataList.size()), getShowStyle());
            mRowCount = param[0];
            mColumnCount = param[1];
        }

        mGridSize = new int[2];

        if (null != mImgDataList && !mImgDataList.isEmpty()) {
            // 只有一张图片的情况
            if (mImgDataList.size() == 1) {
                if (mSingleImageWidth != -1 && mSingleImageHeight != -1) {
                    mGridSize[0] = mSingleImageWidth > totalWidth ? totalWidth : mSingleImageWidth;
                    mGridSize[1] = mSingleImageHeight > totalHeight ? (totalHeight == 0 ? mSingleImageHeight : totalHeight) : mSingleImageHeight;
                    setMeasuredDimension(mGridSize[0], mGridSize[1]);
                } else if (mSingleImageWidth != -1) {
                    int temp = mSingleImageWidth > totalWidth ? totalWidth : mSingleImageWidth;
                    mGridSize[0] =  temp;
                    mGridSize[1] = temp;
                    setMeasuredDimension(mGridSize[0], mGridSize[1]);
                } else if (mSingleImageHeight != -1) {
                    int temp = mSingleImageHeight > totalHeight ? (totalHeight == 0 ? mSingleImageHeight : totalHeight) : mSingleImageHeight;
                    mGridSize[0] = temp;
                    mGridSize[1] = temp;
                    setMeasuredDimension(mGridSize[0], mGridSize[1]);
                } else {
                    mImageViewList.get(0).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mGridSize[0] = (totalWidth - mGap * (mColumnCount - 1)) / mColumnCount;
                    mGridSize[1] = mGridSize[0];
                    height = mGridSize[0] * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();
                    setMeasuredDimension(width, height);
                }

            } else { // 多张图片的情况
                mImageViewList.get(0).setScaleType(ImageView.ScaleType.CENTER_CROP);
                mGridSize[0] = (totalWidth - mGap * (mColumnCount - 1)) / mColumnCount;
                mGridSize[1] = mGridSize[0];
                height = mGridSize[0] * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();
                setMeasuredDimension(width, height);
            }
        } else {
            height = width;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    private void layoutChildrenView() {
        if (null == mImgDataList) {
            return;
        }
        int showCount = getNeedShowCount(mImgDataList.size());
        for (int i = 0; i < showCount; i++) {
            GridChildImageView childrenView = (GridChildImageView) getChildAt(i);
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i));
            }
            int rowNum = i / mColumnCount;
            int columnNum = i % mColumnCount;
            int left = (mGridSize[0] + mGap) * columnNum + getPaddingLeft();
            int top = (mGridSize[1] + mGap) * rowNum + getPaddingTop();
            int right = left + mGridSize[0];
            int bottom = top + mGridSize[1];

            childrenView.layout(left, top, right, bottom);
        }
    }

    private int getNeedShowCount(int size) {
        if (mMaxSize > 0 && size > mMaxSize) {
            return mMaxSize;
        } else {
            return size;
        }
    }

    public void setImagesData(List list) {
        if (null == list || list.isEmpty()) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }
        int newShowCount = getNeedShowCount(list.size());

        int[] gridParam = calculateGridParam(newShowCount, mShowStyle);
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];
        if (mImgDataList == null) {
            int i = 0;
            while (i < newShowCount) {
                GridChildImageView iv = getImageView(i);
                if (null == iv) {
                    return;
                }
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldShowCount = getNeedShowCount(mImgDataList.size());
            if (oldShowCount > newShowCount) {
                removeViews(newShowCount, oldShowCount - newShowCount);
            } else if (oldShowCount < newShowCount) {
                for (int i = oldShowCount; i < newShowCount; i++) {
                    GridChildImageView iv = getImageView(i);
                    if (iv == null)
                        return;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        mImgDataList = list;
        requestLayout();
    }

    private GridChildImageView getImageView(final int position) {
        if (position < mImageViewList.size()) {
            return mImageViewList.get(position);
        } else {
            if (mAdapter != null) {
                GridChildImageView imageView = mAdapter.generateImageView(getContext());
                mImageViewList.add(imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.onItemImageClick(getContext(), (ImageView) v, position, mImgDataList);
                        if (mItemImageClickListener != null) {
                            mItemImageClickListener.onItemImageClick(getContext(), (ImageView) v, position, mImgDataList);
                        }
                    }
                });
                return imageView;
            } else {
                Log.e(TAG, "You must set a GridImageViewAdapter!");
                return null;
            }
        }
    }

    /**
     * 设置宫格图片参数
     * @param imageSize 图片数量
     * @param showStyle 显示风格
     * @return {行数, 列数}
     */
    protected static int[] calculateGridParam(int imageSize, int showStyle) {
        int[] gridParam = new int[2];
        switch (showStyle) {
            case STYLE_FILL:
                if (imageSize < 3) {
                    gridParam[0] = 1;
                    gridParam[1] = imageSize;
                } else if (imageSize <= 4) {
                    gridParam[0] = 2;
                    gridParam[1] = 2;
                } else {
                    gridParam[0] = imageSize / 3 + (imageSize % 3 == 0 ? 0 : 1);
                    gridParam[1] = 3;
                }
                break;
            case STYLE_GRID:
                default:
                    gridParam[0] = imageSize / 3 + (imageSize % 3 == 0 ? 0 : 1);
                    gridParam[1] = 3;
                    break;
        }
        return gridParam;
    }

    /**
     * 设置适配器
     * @param adapter
     */
    public void setAdapter(GridImageViewAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 设置图片间距
     * @param gap
     */
    public void setGap(int gap) {
        mGap = gap;
    }

    /**
     * 设置显示风格
     * @param showStyle
     */
    public void setShowStyle(int showStyle) {
        mShowStyle = showStyle;
    }

    private int getShowStyle() {
        return mShowStyle;
    }

    /**
     * 当图片为单一一张时 设置图片宽高
     * @param width
     * @param height
     */
    public void setSingleImageSize(int width, int height) {
        mSingleImageWidth = width;
        mSingleImageHeight = height;
    }

    /**
     * 设置最大图片数
     * @param maxSize
     */
    public void setMaxSize(int maxSize) {
        mMaxSize = maxSize;
    }

    public void setItemImageClickListener(ItemImageClickListener<T> itemImageListener) {
        mItemImageClickListener = itemImageListener;
    }
}
