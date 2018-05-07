package me.khrystal.weyuereader.view.fragment.impl;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.model.LocalFileBean;
import me.khrystal.weyuereader.utils.FileStack;
import me.khrystal.weyuereader.utils.FileUtils;
import me.khrystal.weyuereader.view.adapter.LocalFileAdapter;
import me.khrystal.weyuereader.view.base.BaseFileFragment;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;
import me.khrystal.weyuereader.widget.DividerItemDecoration;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class FileCategoryFragment extends BaseFileFragment {

    @BindView(R.id.file_category_tv_path)
    TextView fileCategoryTvPath;
    @BindView(R.id.file_category_tv_back_last)
    TextView fileCategoryTvBackLast;
    @BindView(R.id.rv_file_category)
    RecyclerView rvFileCategory;

    List<LocalFileBean> mFileBeans = new ArrayList<>();

    private FileStack mFileStack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_file_category, new BaseViewModel(mContext));
        ButterKnife.bind(this, view);
        return view;
    }

    public static FileCategoryFragment newInstance() {
        FileCategoryFragment fragment = new FileCategoryFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        mFileStack = new FileStack();
        mAdapter = new LocalFileAdapter(mFileBeans);
        rvFileCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFileCategory.addItemDecoration(new DividerItemDecoration(getContext()));
        rvFileCategory.setAdapter(mAdapter);

        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);

        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
            File file = mFileBeans.get(position).getFile();
            if (file.isDirectory()) {
                // 保存当前信息
                FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                snapshot.filePath = fileCategoryTvPath.getText().toString();
                snapshot.files = new ArrayList<>(mAdapter.getAllFiles());
                snapshot.scrollOffset = rvFileCategory.computeVerticalScrollOffset();
                mFileStack.push(snapshot);
                // 切换下一个文件
                toggleFileTree(file);
            } else {
                // 如果是已加载的文件, 则点击事件无效
                String id = file.getAbsolutePath();
                if (CollBookHelper.getsInstance().findBookById(id) != null) {
                    return;
                }
                // 点击选中
                mAdapter.setCheckedItem(position);
                // 反馈
                if (mListener != null) {
                    mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
                }
            }
        }));

        fileCategoryTvBackLast.setOnClickListener(v -> {
            FileStack.FileSnapshot snapshot = mFileStack.pop();
            int oldScrollOffset = rvFileCategory.computeHorizontalScrollOffset();
            if (snapshot == null) return;
            fileCategoryTvPath.setText(snapshot.filePath);
            addFiles(snapshot.files);
            rvFileCategory.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);
            //反馈
            if (mListener != null) {
                mListener.onCategoryChanged();
            }
        });

    }

    /**
     * 添加文件数据
     *
     * @param files
     */
    private void addFiles(List<File> files) {
        mFileBeans.clear();
        for (File file : files) {
            LocalFileBean localFileBean = new LocalFileBean();
            localFileBean.setSelect(false);
            localFileBean.setFile(file);
            mFileBeans.add(localFileBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void toggleFileTree(File file) {
        //路径名
        fileCategoryTvPath.setText(getString(R.string.wy_file_path, file.getPath()));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        //转换成List
        List<File> rootFiles = Arrays.asList(files);
        //排序
        Collections.sort(rootFiles, new FileComparator());
        //加入
        addFiles(rootFiles);
        //反馈
        if (mListener != null) {
            mListener.onCategoryChanged();
        }
    }

    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }
            //文件夹内部数量为0
            if (pathname.isDirectory() && pathname.list().length == 0) {
                return false;
            }

            /**
             * 现在只支持TXT文件的显示
             */
            //文件内容为空,或者不以txt为开头
            if (!pathname.isDirectory() &&
                    (pathname.length() == 0 || !pathname.getName().endsWith(FileUtils.SUFFIX_TXT))) {
                return false;
            }
            return true;
        }
    }
}
