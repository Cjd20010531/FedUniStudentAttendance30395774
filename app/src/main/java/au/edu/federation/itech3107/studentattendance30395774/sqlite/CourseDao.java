package au.edu.federation.itech3107.studentattendance30395774.sqlite;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface CourseDao {
    @Query("SELECT * FROM CourseBean where groupId = :id")
    Maybe<List<CourseBean>> getAllUsers(int id);

    @Query("SELECT * FROM CourseBean where couId = :id")
    Maybe<CourseBean> getCourseById(int id);

    @Insert()
    void insert(CourseBean... users);

    @Update
    void update(CourseBean... users);

    @Delete
    void delete(CourseBean... users);

}
