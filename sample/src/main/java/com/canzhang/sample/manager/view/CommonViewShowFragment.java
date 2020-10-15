package com.canzhang.sample.manager.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.voteview.VoteListener;
import com.canzhang.sample.manager.view.voteview.VoteView;
import com.example.base.base.BaseFragment;

import java.util.LinkedHashMap;


/**
 * 通用展示类型的view展示
 */
public class CommonViewShowFragment extends BaseFragment {
    private static final String TYPE_KEY = "type_key";
    public static final int DASH_LINE = 1;
    public static final int VOTE_VIEW = 2;


    @IntDef({DASH_LINE,VOTE_VIEW})
    public @interface Type {

    }

    private int mType;

    public static Fragment newInstance(@Type int type) {
        Fragment fragment = new CommonViewShowFragment();
        Bundle info = new Bundle();
        info.putInt(TYPE_KEY, type);
        fragment.setArguments(info);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(TYPE_KEY);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_common_view_show_fragment, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        switch (mType) {
            case DASH_LINE:
                view.findViewById(R.id.ll_dash_line).setVisibility(View.VISIBLE);
                break;
                case VOTE_VIEW:
                    VoteView voteView = view.findViewById(R.id.vote_view);
                    voteView.setVisibility(View.VISIBLE);

                    LinkedHashMap<String, Integer> voteData = new LinkedHashMap<>();
                    //造数据源
                    voteData.put("美国", 0);
                    voteData.put("英国", 15);
                    voteData.put("中国", 3);
                    voteData.put("俄罗斯", 33);
                    voteData.put("日本", 99);

                    voteView.initVote(voteData);
                    voteView.setAnimationRate(600);
                    voteView.setVoteListener(new VoteListener() {
                        @Override
                        public boolean onItemClick(View view, int index, boolean status) {
                            if (!status) {
                                showDialog(voteView, view);
                            } else {
                                voteView.notifyUpdateChildren(view, true);
                            }
                            return true;
                        }
                    });
                view.findViewById(R.id.ll_dash_line).setVisibility(View.VISIBLE);
                break;
        }

    }
    /**
     * 取消投票的 dialog
     */
    public void showDialog(final VoteView voteView, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonViewShowFragment.this.mContext)
                .setTitle("是否取消投票？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        voteView.resetNumbers(); // 恢复初始投票数据
                        voteView.notifyUpdateChildren(view, false);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
