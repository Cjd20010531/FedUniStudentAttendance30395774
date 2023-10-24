package au.edu.federation.itech3107.studentattendance30395774.sqlite;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {UserBean.class, CourseBean.class,CourseGroupBean.class, ClassBean.class,StudentBean.class}, version = 10, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static final String DB_NAME = "DataBase.db";
    private static volatile DataBase instance;

    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static DataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                DataBase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()//数据库更新时删除数据重新创建
                .build();
    }

    public abstract UserDao getUserDao();
    public abstract CourseDao getCourseDao();
    public abstract CourseGroupDao getCourseGroupDao();
    public abstract ClassDao getClassDao();
    public abstract StudentDao getStudentDao();
}
