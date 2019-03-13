package com.example.administrator.demoall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.demoall.dialog.LoadingDialog;
import com.example.administrator.demoall.fqladapter.BaseTypeFooterAdapter;
import com.example.administrator.demoall.fqladapter.test.CouponItemAdapter;
import com.example.administrator.demoall.myadapter.test.TestBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tab_coupon);
        for (int i = 0; i <3 ; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("哈哈哈"+i));
        }



        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TestBean> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(new TestBean().setType(i % 2));
        }
//        final BaseAdapter adapter;
//        recyclerView.setAdapter(adapter = new TestAdapter(lists));
//        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseAdapter adapter, View view, int position) {
//                Toast.makeText(MainActivity.this, "当前位置：" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        CouponItemAdapter adapter;
        recyclerView.setAdapter(adapter = new CouponItemAdapter(this, lists, new BaseTypeFooterAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(MainActivity.this, "当前位置：" + position, Toast.LENGTH_SHORT).show();
            }
        }));
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseTypeFooterAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
            }
        },recyclerView);


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == SCROLL_STATE_DRAGGING ) {
//                    if (recyclerView.computeVerticalScrollOffset() > 0) {// 有滚动距离，说明可以加载更多，解决了 items 不能充满 RecyclerView
//
//                        boolean isBottom = false ;
//
//                        // 也可以使用 方法2
//                         isBottom = !recyclerView.canScrollVertically(1) ;
//                        if (isBottom) {
//                            Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });




    }

    public void test(View view) {

        LoadingDialog.get(this).show();
    }
}

