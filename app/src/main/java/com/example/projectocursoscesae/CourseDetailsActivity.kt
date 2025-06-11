package com.example.projectocursoscesae

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectocursoscesae.data.CourseDatabaseHelper
import com.example.projectocursoscesae.databinding.ActivityCourseDetailsBinding
import android.database.Cursor


class CourseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailsBinding
    private lateinit var dbHelper: CourseDatabaseHelper
    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = CourseDatabaseHelper(this)
        courseId = intent.getIntExtra("COURSE_ID", -1)
        if (courseId == -1) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Voltar (always visible)
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Editar
        binding.btnEdit.setOnClickListener {
            toggleEditing(true)
        }

        // Guardar changes
        binding.btnSave.setOnClickListener {
            saveChanges()
        }

        // Apagar course
        binding.btnDelete.setOnClickListener {
            deleteCourse()
        }

        toggleEditing(false) // Start with read-only view
    }

    private fun toggleEditing(enable: Boolean) {
        binding.etName.isEnabled = enable
        binding.etLocation.isEnabled = enable
        binding.etStartDate.isEnabled = enable
        binding.etEndDate.isEnabled = enable
        binding.etPrice.isEnabled = enable
        binding.etDuration.isEnabled = enable
        binding.etEdition.isEnabled = enable

        binding.btnEdit.visibility = if (enable) View.GONE else View.VISIBLE
        binding.btnSave.visibility = if (enable) View.VISIBLE else View.GONE
        binding.btnDelete.visibility = if (enable) View.VISIBLE else View.GONE
    }

    private fun loadCourseData(id: Int) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            CourseDatabaseHelper.TABLE_COURSES,
            null,
            "${CourseDatabaseHelper.COLUMN_ID}=?",
            arrayOf(courseId.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            binding.etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_NAME)))
            binding.etLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_LOCATION)))
            binding.etStartDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_START_DATE)))
            binding.etEndDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_END_DATE)))
            binding.etPrice.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_PRICE)))
            binding.etDuration.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_DURATION)))
            binding.etEdition.setText(cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_EDITION)))
        }
        cursor.close()
    }

    private fun saveChanges() {
        val name = binding.etName.text.toString()
        val location = binding.etLocation.text.toString()
        val start = binding.etStartDate.text.toString()
        val end = binding.etEndDate.text.toString()
        val price = binding.etPrice.text.toString()
        val duration = binding.etDuration.text.toString()
        val edition = binding.etEdition.text.toString()

        if (name.isNotEmpty() && location.isNotEmpty()) {
            val values = ContentValues().apply {
                put(CourseDatabaseHelper.COLUMN_NAME, name)
                put(CourseDatabaseHelper.COLUMN_LOCATION, location)
                put(CourseDatabaseHelper.COLUMN_START_DATE, start)
                put(CourseDatabaseHelper.COLUMN_END_DATE, end)
                put(CourseDatabaseHelper.COLUMN_PRICE, price)
                put(CourseDatabaseHelper.COLUMN_DURATION, duration)
                put(CourseDatabaseHelper.COLUMN_EDITION, edition)
            }

            val db = dbHelper.writableDatabase
            val rows = db.update(
                CourseDatabaseHelper.TABLE_COURSES,
                values,
                "${CourseDatabaseHelper.COLUMN_ID} = ?",
                arrayOf(courseId.toString())
            )

            if (rows > 0) {
                Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                toggleEditing(false)
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Name and Location required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCourse() {
        val db = dbHelper.writableDatabase
        db.delete(
            CourseDatabaseHelper.TABLE_COURSES,
            "${CourseDatabaseHelper.COLUMN_ID}=?",
            arrayOf(courseId.toString())
        )
        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
        finish()
    }
}
