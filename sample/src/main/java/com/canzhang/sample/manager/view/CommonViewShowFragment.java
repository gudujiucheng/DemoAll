package com.canzhang.sample.manager.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.voteview.VoteListener;
import com.canzhang.sample.manager.view.voteview.VoteView;
import com.canzhang.sample.manager.view.voteview.myvoteview.MyVoteAdapter;
import com.canzhang.sample.manager.view.voteview.myvoteview.VoteBean;
import com.canzhang.sample.manager.view.voteview.myvoteview.VoteDataBiz;
import com.canzhang.sample.manager.view.voteview.myvoteview.VoteListInfoBean;
import com.example.base.base.BaseFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 通用展示类型的view展示
 */
public class CommonViewShowFragment extends BaseFragment {
    private static final String TYPE_KEY = "type_key";
    public static final int DASH_LINE = 1;
    public static final int VOTE_VIEW = 2;


    @IntDef({DASH_LINE, VOTE_VIEW})
    @Retention(RetentionPolicy.SOURCE)
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(View view) {
        //这种可以快速实现view的裁剪 参考：https://developer.android.com/training/material/shadows-clipping?hl=zh-cn
//        view.findViewById(R.id.ll_test).setOutlineProvider(new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                 outline.setRoundRect(0,0,300,400,50);
//            }
//        });
//        view.findViewById(R.id.ll_test).setClipToOutline(true);
        switch (mType) {
            case DASH_LINE:
                view.findViewById(R.id.ll_dash_line).setVisibility(View.VISIBLE);
                break;
            case VOTE_VIEW:
                view.findViewById(R.id.ll_vote).setVisibility(View.VISIBLE);
                VoteView voteView = view.findViewById(R.id.vote_view);
                LinkedHashMap<String, Integer> voteData = new LinkedHashMap<>();
                //造数据源
                voteData.put("美国", 0);
                voteData.put("英国", 1);
                voteData.put("中国", 1);


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
                break;
        }

        //测试外层嵌套rv逻辑
        RecyclerView outRecyclerView = view.findViewById(R.id.rv_app);
        outRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Pair<VoteListInfoBean, List<VoteBean>>> mDatas = new ArrayList<>();

        for (int i = 0; i <20 ; i++) {
            List<VoteBean> voteBeans = new ArrayList<>();
            voteBeans.add(new VoteBean(VoteBean.VOTE_TYPE).setTitle("好吃"+i).setCurrentItemVoteNum(1));

            voteBeans.add(new VoteBean(VoteBean.VOTE_TYPE).setTitle("不好吃"+i).setCurrentItemVoteNum(2).setCheckedOnAfterVote(i%2==0));
            voteBeans.add(new VoteBean(VoteBean.VOTE_TYPE).setTitle("还行吧"+i).setCurrentItemVoteNum(3));
            voteBeans.add(new VoteBean(VoteBean.VOTE_TYPE).setTitle("中立"+i).setCurrentItemVoteNum(3));
            mDatas.add(new Pair<>(new VoteListInfoBean(i%3, i%2==0, i%2==0?2:20),voteBeans));
        }

        outRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_temp_out_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Pair<VoteListInfoBean, List<VoteBean>> voteListInfoBeanListPair = mDatas.get(position);
                ((TestHolder)holder).inRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ((TestHolder)holder).inRecyclerView.setAdapter(new MyVoteAdapter(new VoteDataBiz(voteListInfoBeanListPair)));

            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }
        });


    }

    class TestHolder extends RecyclerView.ViewHolder {

        RecyclerView inRecyclerView;


        public TestHolder(View itemView) {
            super(itemView);
            inRecyclerView = itemView.findViewById(R.id.rv_in);



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
