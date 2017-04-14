package me.khrystal.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.khrystal.widget.R;


/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/13
 * update time:
 * email: 723526676@qq.com
 */

public class WhiteBoardDialog extends DialogFragment {

    private static final String TAG = WhiteBoardDialog.class.getSimpleName();
    private static final String BOARD_TYPE = "board_type";
    private static final int DEFAULT_COLOR = 0xCCFFFFFF;

    private int type = -1;

    private WhiteBoardListener mListener;
    private RecyclerView recyclerView;
    private Dialog lDialog;
    private RelativeLayout container;
    private int padding;

    /**
     * 设置tag
     * @param type
     */
    public static WhiteBoardDialog getInstance(int type) {
        WhiteBoardDialog dialog = new WhiteBoardDialog();
        Bundle args = new Bundle();
        args.putInt(BOARD_TYPE, type);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * 设置监听器
     * @param listener
     */
    public WhiteBoardDialog setWhiteBoardListener(WhiteBoardListener listener) {
        mListener = listener;
        return this;
    }

    public WhiteBoardDialog setClipDistance(int padding) {
        this.padding = padding;
        return this;
    }


    @Deprecated
    public WhiteBoardDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        if (getArguments() != null) {
            type = getArguments().getInt(BOARD_TYPE, -1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e(TAG, "2");
        if (lDialog == null)
            lDialog = new Dialog(getActivity(), R.style.WhiteDialogStyle);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.getWindow().getAttributes().windowAnimations = R.style.WhiteBoardDialogAnim;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            lDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        else
            lDialog.getWindow().getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        lDialog.setContentView(R.layout.layout_white_board);
        // 关闭事件
        lDialog.findViewById(R.id.ivClose)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        lDialog.dismiss();
                    }
                });

        recyclerView = (RecyclerView) lDialog.findViewById(R.id.rV);
        if (padding != 0)
            recyclerView.setPadding(0, padding, 0, padding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WhiteAdapter());
        container = (RelativeLayout) lDialog.findViewById(R.id.rlContainer);
        container.setBackgroundColor(DEFAULT_COLOR);
//        lDialog.setCancelable(true);
        return lDialog;
    }

    public void show(FragmentManager manager) {
        show(manager, null);
    }

    class WhiteAdapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_white_board, parent, false);
            VH holder = new VH(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            String text = "";
            if (mListener != null) {
                text = mListener.fillText(position);
            }
            holder.textView.setText(text);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onItemClick(type, position);
                    if (lDialog != null)
                        lDialog.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mListener != null) {
                return mListener.getItemCount();
            }
            return 0;
        }
    }

    class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }

    public interface WhiteBoardListener {

        /**
         * 点击item调用 回传type与position 外部持有list 与position对应
         */
        public void onItemClick(int type, int position);

        /**
         * 获取外部list的数量
         */
        public int getItemCount();

        /**
         * 填充item中的textView 获取list的position的item
         */
        public String fillText(int position);
    }
}
