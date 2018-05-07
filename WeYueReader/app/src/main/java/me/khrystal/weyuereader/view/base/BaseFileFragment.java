package me.khrystal.weyuereader.view.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.khrystal.weyuereader.model.LocalFileBean;
import me.khrystal.weyuereader.view.adapter.LocalFileAdapter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BaseFileFragment extends BaseFragment {
    protected LocalFileAdapter mAdapter;
    protected OnFileCheckedListener mListener;
    protected boolean isCheckedAll;

    // 设置当前列表全选
    public void setCheckedAll(boolean isCheckedAll) {
        if (mAdapter == null)
            return;
        this.isCheckedAll = isCheckedAll;
        mAdapter.setCheckdAll(isCheckedAll);
    }

    public void setChecked(boolean checked) {
        isCheckedAll = checked;
    }

    // 当前fragment 是否全选
    public boolean isCheckedAll() {
        return isCheckedAll;
    }

    // 获取被选中数量
    public int getCheckedCount() {
        if (mAdapter == null)
            return 0;
        return mAdapter.getCheckedCount();
    }

    public List<File> getCheckedFiles() {
        List<File> files = new ArrayList<>();
        if (mAdapter != null) {
            for (LocalFileBean localFileBean : mAdapter.getCheckedFiles()) {
                files.add(localFileBean.getFile());
            }
        }
        return files;
    }

    // 获取文件总数
    public int getFileCount() {
        return mAdapter != null ? mAdapter.getItemCount() : null;
    }

    // 获取可点击的文件的数量
    public int getCheckableCount() {
        if (mAdapter == null)
            return 0;
        return mAdapter.getCheckableCount();
    }

    // 删除选中的文件
    public void deleteCheckedFiles() {
        // 删除选中的文件
        List<LocalFileBean> files = mAdapter.getCheckedFiles();
        // 删除显示的文件列表
        mAdapter.removeCheckedItems(files);
        // 删除选中的文件
        for (LocalFileBean localFileBean : files) {
            if (localFileBean.getFile().exists()) {
                localFileBean.getFile().delete();
            }
        }
    }

    //设置文件点击监听事件
    public void setOnFileCheckedListener(OnFileCheckedListener listener) {
        mListener = listener;
    }


    // 文件点击监听
    public interface OnFileCheckedListener {
        void onItemCheckedChange(boolean isChecked);

        void onCategoryChanged();
    }
}
