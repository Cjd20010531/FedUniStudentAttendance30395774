package au.edu.federation.itech3107.studentattendance30395774;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


import au.edu.federation.itech3107.studentattendance30395774.databinding.ActivityLoginBinding;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.UserBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding inflate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        //Log in
        inflate.login.setOnClickListener(v -> {
            searchData();
        });

        //Sign in
        inflate.register.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void searchData() {
        DataBase.getInstance(this).getUserDao().getUserByName(inflate.name.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<UserBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<UserBean> list) {
                        //查询到结果
                       checkPwd(list);

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //查询不到结果
                        Toast.makeText(LoginActivity.this, getString(R.string.passwordNotice), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkPwd(List<UserBean> list) {
        if (list.size() != 0) {
            //Check account
            UserBean userBean = list.get(0);
            if (userBean.getPwd().equals(inflate.pwd.getText().toString())) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else {
                //Password error
                Toast.makeText(LoginActivity.this, getString(R .string.passwordNotice), Toast.LENGTH_SHORT).show();
            }

        } else {
            //Do not have this account
            Toast.makeText(LoginActivity.this, getString(R.string.passwordNotice), Toast.LENGTH_SHORT).show();
        }
    }

}
