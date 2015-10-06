package by.kirich1409.grsuschedule.network

import by.kirich1409.grsuschedule.model.*
import retrofit.http.GET
import retrofit.http.Query

/**
 * Created by kirillrozov on 9/13/15.
 */
public interface ScheduleService {

    @GET("/getDepartments")
    public fun getDepartments(): Departments

    @GET("/getTeachers")
    public fun getTeachers(@Query("extended") extended: Boolean): Teachers

    @GET("/getFaculties")
    public fun getFaculties(): Faculties

    @GET("/getGroups")
    public fun getGroups(
            @Query("departmentId") departmentId: Int,
            @Query("facultyId") facultyId: Int,
            @Query("course") course: Int): Groups

    @GET("/getGroupSchedule")
    public fun getGroupSchedule(
            @Query("groupId") groupId: Int,
            @Query("dateStart") start: QueryDate? = null,
            @Query("dateEnd") end: QueryDate? = null): Schedule

    @GET("/getTeacherSchedule")
    public fun getTeacherSchedule(
            @Query("teacherId") teacherId: Int,
            @Query("dateStart") start: QueryDate? = null,
            @Query("dateEnd") end: QueryDate? = null): Schedule
}
