package au.edu.federation.itech3107.studentattendance30395774;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.util.ClassCheAdapter;
import au.edu.federation.itech3107.studentattendance30395774.util.CourseBaseBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.ClassBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.CourseBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import au.edu.federation.itech3107.studentattendance30395774.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395774.util.ScreenUtils;
import au.edu.federation.itech3107.studentattendance30395774.util.StringUtil;
import au.edu.federation.itech3107.studentattendance30395774.view.EditTextLayout;
import au.edu.federation.itech3107.studentattendance30395774.view.PopupWindowDialog;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//添加课程
public class AddActivity extends AppCompatActivity{
    private boolean mAddNotion = true;

    private CourseBaseBean mCourse;
    private CourseBean mIntentBean;

    private ImageView mIvL;
    private LinearLayout mContainer;
    private ImageView mSubmit;
    private EditTextLayout mClassName;
    private EditTextLayout mTeacherName;
    private LinearLayout ll_class;
    private RecyclerView rv;
    private Button mAddButton;
    private ClassCheAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        mCourse = (CourseBaseBean) intent.getSerializableExtra(Constant.INTENT_ADD_COURSE_ANCESTOR);
        if (mCourse != null) {
            mAddNotion = true;
        } else {
            mIntentBean = (CourseBean) intent.getSerializableExtra(Constant.INTENT_EDIT_COURSE);
            if (mIntentBean != null) {
                mAddNotion = false;
                mIntentBean.init();
            }
        }
        initView();

    }

    private void initView() {
        mClassName = findViewById(R.id.etl_name);
        mTeacherName = findViewById(R.id.etl_teacher);

        mIvL = findViewById(R.id.iv_add_location);
        mContainer = findViewById(R.id.layout_location_container);
        mSubmit = findViewById(R.id.iv_submit);
        ll_class = findViewById(R.id.ll_class);
        mAddButton = findViewById(R.id.btn_add);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        mAdapter = new ClassCheAdapter(R.layout.item_check_course, new ArrayList<>());
        rv.setAdapter(mAdapter);
        mSubmit.setImageResource(R.drawable.ic_done_black_24dp);
        addLocation(false);

        mAddButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("type", 1));
        });

        mIvL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddActivity.this, CheckInActivity.class)
                        .putExtra("bean", mIntentBean));
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBase.getInstance(this).getClassDao().getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<ClassBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ClassBean> list) {
                        //查询到结果
                        if (list.size() != 0) {
                            mAdapter.setNewData(list);
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



    private void submit() {
        //name
        String name = mClassName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
            return;
        }

        //teacher
        String teacher = mTeacherName.getText().trim();
        //group

        long couCgId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);
        int childCount = mContainer.getChildCount();
        boolean hasLocation = false;
        for (int i = 0; i < childCount; i++) {
            View locationItem = mContainer.getChildAt(i);
            Object obj = locationItem.getTag();

            if (obj != null) {
                hasLocation = true;
                CourseBean courseBean = (CourseBean) obj;
                courseBean.setCouName(name);
                courseBean.setCouTeacher(teacher);
                courseBean.setGroupId(Constant.selectId);

                if (mAddNotion || courseBean.getCouId() == 0) {
                    courseBean.setCouCgId(couCgId);
                    courseBean.setJoinClassId(Constant.select_class);
                    courseBean.init();
                    //插入课程
                    DataBase.getInstance(this).getCourseDao().insert(courseBean);
                } else {
                    courseBean.setJoinClassId(Constant.select_class);
                    courseBean.init();
                    //更新课程
                    DataBase.getInstance(this).getCourseDao().update(courseBean);
                }

            }
        }
        if (!hasLocation) {
            Toast.makeText(AddActivity.this, "Time Is Null", Toast.LENGTH_SHORT).show();
        }

        if (mAddNotion) {
            Toast.makeText(AddActivity.this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void addLocation(boolean closeable) {
        final LinearLayout locationItem = (LinearLayout) View.inflate(this,
                R.layout.layout_location_item, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = ScreenUtils.dp2px(8);

        if (closeable) {
            locationItem.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContainer.removeView(locationItem);
                }
            });

            initEmptyLocation(locationItem);

        } else {// 建立默认的上课时间和上课地点
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);

            if (mCourse != null) {
                // 屏幕点击过来

                CourseBean defaultCourse = new CourseBean().setCouOnlyIdR(AppUtils.createUUID())
                        .setCouAllWeekR(Constant.DEFAULT_ALL_WEEK)
                        .setCouWeekR(mCourse.getRow())
                        .setCouStartNodeR(mCourse.getCol())
                        .setCouNodeCountR(mCourse.getRowNum())
                        .init();

                initNodeInfo(locationItem, defaultCourse);
            } else if (mIntentBean != null) {
                // 编辑过来
                initNodeInfo(locationItem, mIntentBean);

                mClassName.setText(mIntentBean.getCouName());
                mTeacherName.setText(mIntentBean.getCouTeacher());
                Constant.select_class = "";
                String joinClassId = mIntentBean.getJoinClassId();
                if (!StringUtil.isEmpty(joinClassId)) {
                    Constant.select_class = joinClassId;
                }
            } else {
                //
                initEmptyLocation(locationItem);
                mIvL.setVisibility(View.GONE);
            }
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mContainer.addView(locationItem, params);
    }

    private void initEmptyLocation(LinearLayout locationItem) {
        CourseBean defaultCourse = new CourseBean().setCouOnlyIdR(AppUtils.createUUID())
                .setCouAllWeekR(Constant.DEFAULT_ALL_WEEK)
                .setCouAllWeekR(1 + "")
                .setCouStartNodeR(1)
                .setCouNodeCountR(1);
        initNodeInfo(locationItem, defaultCourse);
    }

    private void initNodeInfo(LinearLayout locationItem, CourseBean courseBean) {
        TextView tvText = locationItem.findViewById(R.id.tv_text);
        String builder = Constant.WEEK_SINGLE[courseBean.getCouWeek() - 1] + "Week " +
                " Section" + courseBean.getCouStartNode() + "-" +
                (courseBean.getCouStartNode() + courseBean.getCouNodeCount() - 1);
        tvText.setText(builder);

        locationItem.setTag(courseBean);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();

        CourseBean courseBean = null;
        Object obj = locationItem.getTag();
        // has tag data
        if (obj != null && obj instanceof CourseBean) {
            courseBean = (CourseBean) obj;
        } else {
            throw new RuntimeException("Course data tag not be found");
        }

        dialog.showSelectTimeDialog(this, courseBean, new PopupWindowDialog.SelectTimeCallback() {
            @Override
            public void onSelected(CourseBean course) {
                StringBuilder builder = new StringBuilder();
                builder.append("周").append(Constant.WEEK_SINGLE[course.getCouWeek() - 1])
                        .append(" 第").append(course.getCouStartNode()).append("-")
                        .append(course.getCouStartNode() + course.getCouNodeCount() - 1).append("节");
                if (!TextUtils.isEmpty(course.getCouLocation())) {
                    builder.append("【").append(course.getCouLocation()).append("】");
                }

                ((TextView) locationItem.findViewById(R.id.tv_text))
                        .setText(builder.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
