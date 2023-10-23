package au.edu.federation.itech3107.studentattendance30395774.util;


import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.R;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.ClassBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.StudentBean;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * @author
 * @date 2023/10/19
 * @description
 */

public class CheckInAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {

    private Context mContext;

    public CheckInAdapter(int layoutResId, @Nullable List<ClassBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassBean item) {
        helper.setText(R.id.tv_item, "Class Name: " + item.getName());
        RecyclerView view = helper.getView(R.id.rv);
        view.setLayoutManager(new LinearLayoutManager(mContext));
        view.setNestedScrollingEnabled(false);
        StudentCheckAdapter mAdapter = new StudentCheckAdapter(R.layout.item_check_course, new ArrayList<>());
        view.setAdapter(mAdapter);
        DataBase.getInstance(mContext).getStudentDao().getAllUsers(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<StudentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<StudentBean> list) {
                        //查询到结果
                        if (list.size() != 0) {
                            mAdapter.setNewData(list);
                            for (StudentBean bean : list) {
                                Log.e("hao", "学生数据:" + bean.toString());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
