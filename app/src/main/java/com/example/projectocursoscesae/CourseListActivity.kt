package com.example.projectocursoscesae

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectocursoscesae.adapter.CourseAdapter
import com.example.projectocursoscesae.data.CourseDatabaseHelper
import com.example.projectocursoscesae.databinding.ActivityCourseListBinding
import com.example.projectocursoscesae.model.Course

class CourseListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseListBinding
    private lateinit var courseAdapter: CourseAdapter
    private val courseList = mutableListOf<Course>()
    private lateinit var dbHelper: CourseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseAdapter = CourseAdapter(courseList)
        binding.rvCourses.layoutManager = LinearLayoutManager(this)
        binding.rvCourses.adapter = courseAdapter

        dbHelper = CourseDatabaseHelper(this)

        binding.fabAddCourse.setOnClickListener {
            startActivity(Intent(this, AddEditCourseActivity::class.java))
        }

        binding.btnExit.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCoursesFromDB()
    }

    private fun loadCoursesFromDB() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            CourseDatabaseHelper.TABLE_COURSES, null, null, null, null, null, null
        )

        courseList.clear() // ✅ Clear the existing list

        with(cursor) {
            while (moveToNext()) {
                val course = Course(
                    id = getInt(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_NAME)),
                    location = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_LOCATION)),
                    startDate = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_START_DATE)),
                    endDate = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_END_DATE)),
                    price = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_PRICE)),
                    duration = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_DURATION)),
                    edition = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_EDITION)),
                    imagePath = getString(getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_IMAGE_PATH))
                )
                courseList.add(course) // ✅ Add to original list (not a new one)
            }
        }
        cursor.close()
        courseAdapter.notifyDataSetChanged() // ✅ Notify adapter to refresh the list
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_course_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sorted = when (item.itemId) {
            R.id.sort_name_asc -> courseList.sortedBy { it.name }
            R.id.sort_name_desc -> courseList.sortedByDescending { it.name }
            R.id.sort_date_asc -> courseList.sortedBy { it.startDate }
            R.id.sort_date_desc -> courseList.sortedByDescending { it.startDate }
            else -> return super.onOptionsItemSelected(item)
        }

        courseAdapter.updateData(sorted)
        return true
    }
}
