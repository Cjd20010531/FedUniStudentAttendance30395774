package au.edu.federation.itech3107.studentattendance30395774;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.util.CheckInAdapter;
import au.edu.federation.itech3107.studentattendance30395774.databinding.ActivityCheckBinding;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.ClassBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.CourseBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import au.edu.federation.itech3107.studentattendance30395774.util.StringUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CheckInActivity extends AppCompatActivity {
    private ActivityCheckBinding inflate;
    private CheckInAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityCheckBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        initView();
    }

    private void initView() {
        inflate.rv.setLayoutManager(new LinearLayoutManager(this));
        inflate.rv.setNestedScrollingEnabled(false);

        mAdapter = new CheckInAdapter(R.layout.item_check, new ArrayList<>(),  CheckInActivity.this);
        inflate.rv.setAdapter(mAdapter);

        CourseBean bean = (CourseBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            DataBase.getInstance(this).getCourseDao().getCourseById(bean.getCouId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<CourseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(CourseBean list) {
                            //Query result
                            if (!StringUtil.isEmpty(list.getJoinClassId())) {
                                Constant.select_class = list.getJoinClassId();
                            }
                            if (!StringUtil.isEmpty(list.getCheckInStudentIds())) {
                                Constant.select_student = list.getCheckInStudentIds();
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

        inflate.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setJoinClassId(Constant.select_class);
                bean.setCheckInStudentIds(Constant.select_student);
                DataBase.getInstance(CheckInActivity.this).getCourseDao().update(bean);
                Toast.makeText(CheckInActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        DataBase.getInstance(this).getClassDao().getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<ClassBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ClassBean> list) {
                        //Query result
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (Constant.select_class.contains(list.get(i).getId() + ",")) {
                                    mAdapter.addData(list.get(i));
                                }
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
