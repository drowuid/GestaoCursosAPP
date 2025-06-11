package com.example.projectocursoscesae

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectocursoscesae.data.CourseDatabaseHelper
import com.example.projectocursoscesae.databinding.ActivityAddEditCourseBinding
import java.io.File

class AddEditCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditCourseBinding
    private lateinit var dbHelper: CourseDatabaseHelper
    private var courseId: Int = -1
    private var selectedImagePath: String = ""
    private val PICK_IMAGE_REQUEST = 1
    private var isViewOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = CourseDatabaseHelper(this)

        isViewOnly = intent.getBooleanExtra("VIEW_ONLY", false)
        courseId = intent.getIntExtra("COURSE_ID", -1)

        // Load course data if editing or viewing
        if (courseId != -1) {
            loadCourseData(courseId)
        }

        // Image picker
        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Save or update course
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val location = binding.etLocation.text.toString()
            val start = binding.etStartDate.text.toString()
            val end = binding.etEndDate.text.toString()
            val price = binding.etPrice.text.toString()
            val duration = binding.etDuration.text.toString()
            val edition = binding.etEdition.text.toString()

            if (name.isNotEmpty() && location.isNotEmpty()) {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put(CourseDatabaseHelper.COLUMN_NAME, name)
                    put(CourseDatabaseHelper.COLUMN_LOCATION, location)
                    put(CourseDatabaseHelper.COLUMN_START_DATE, start)
                    put(CourseDatabaseHelper.COLUMN_END_DATE, end)
                    put(CourseDatabaseHelper.COLUMN_PRICE, price)
                    put(CourseDatabaseHelper.COLUMN_DURATION, duration)
                    put(CourseDatabaseHelper.COLUMN_EDITION, edition)
                    put(CourseDatabaseHelper.COLUMN_IMAGE_PATH, selectedImagePath)
                }

                if (courseId != -1) {
                    val rowsAffected = db.update(
                        CourseDatabaseHelper.TABLE_COURSES,
                        values,
                        "${CourseDatabaseHelper.COLUMN_ID} = ?",
                        arrayOf(courseId.toString())
                    )
                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result = db.insert(CourseDatabaseHelper.TABLE_COURSES, null, values)
                    if (result != -1L) {
                        Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
                    }
                }

                finish()
            } else {
                Toast.makeText(this, "Name and Location are required", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete course
        binding.btnDelete.setOnClickListener {
            if (courseId != -1) {
                val db = dbHelper.writableDatabase
                db.delete(
                    CourseDatabaseHelper.TABLE_COURSES,
                    "${CourseDatabaseHelper.COLUMN_ID} = ?",
                    arrayOf(courseId.toString())
                )
                Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Handle view-only logic
        if (isViewOnly) {
            setFieldsEnabled(false)
            binding.btnSave.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnCancel.visibility = View.VISIBLE

            binding.btnEdit.setOnClickListener {
                setFieldsEnabled(true)
                binding.btnSave.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnEdit.visibility = View.GONE
                binding.btnCancel.visibility = View.GONE
            }

            binding.btnCancel.setOnClickListener {
                finish()
            }
        }
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        binding.etName.isEnabled = enabled
        binding.etLocation.isEnabled = enabled
        binding.etStartDate.isEnabled = enabled
        binding.etEndDate.isEnabled = enabled
        binding.etPrice.isEnabled = enabled
        binding.etDuration.isEnabled = enabled
        binding.etEdition.isEnabled = enabled
        binding.btnSelectImage.isEnabled = enabled
    }

    private fun loadCourseData(id: Int) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            CourseDatabaseHelper.TABLE_COURSES,
            null,
            "${CourseDatabaseHelper.COLUMN_ID}=?",
            arrayOf(id.toString()),
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
            selectedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(CourseDatabaseHelper.COLUMN_IMAGE_PATH)) ?: ""

            if (selectedImagePath.isNotEmpty()) {
                val file = File(selectedImagePath)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    binding.ivPreviewImage.setImageBitmap(bitmap)
                }
            }
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            uri?.let {
                val filePath = getRealPathFromURI(it)
                if (filePath != null) {
                    selectedImagePath = filePath
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    binding.ivPreviewImage.setImageBitmap(bitmap)
                    Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            val fileName = if (index != -1) cursor.getString(index) else "temp_image.jpg"
            cursor.close()
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, fileName)
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } else null
    }
}
