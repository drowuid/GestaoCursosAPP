package com.example.projectocursoscesae.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.projectocursoscesae.AddEditCourseActivity
import com.example.projectocursoscesae.CourseDetailsActivity
import com.example.projectocursoscesae.R
import com.example.projectocursoscesae.data.CourseDatabaseHelper
import com.example.projectocursoscesae.databinding.ItemCourseBinding
import com.example.projectocursoscesae.model.Course

class CourseAdapter(private var courseList: MutableList<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        val context = holder.itemView.context

        // Set course image or default
        if (course.imagePath.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(course.imagePath)
            holder.binding.ivCourseImage.setImageBitmap(bitmap)
        } else {
            holder.binding.ivCourseImage.setImageResource(R.drawable.course)
        }

        // Bind all course data correctly
        holder.binding.tvCourseTitle.text = course.name
        holder.binding.tvCourseLocationDate.text = "${course.location} – ${course.startDate}"
        holder.binding.tvCourseInfo.text =
            "Edition: ${course.edition}, Duration: ${course.duration}, Price: ${course.price}"

        // Navigate to details on click
        holder.itemView.setOnClickListener {
            if (course.id <= 0) return@setOnClickListener
            val intent = Intent(context, AddEditCourseActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            intent.putExtra("VIEW_ONLY", true) // ✅ add this
            context.startActivity(intent)

        }

        // 🗑 Delete on long press
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete \"${course.name}\"?")
                .setPositiveButton("Yes") { _, _ ->
                    val dbHelper = CourseDatabaseHelper(context)
                    dbHelper.writableDatabase.delete(
                        CourseDatabaseHelper.TABLE_COURSES,
                        "${CourseDatabaseHelper.COLUMN_ID}=?",
                        arrayOf(course.id.toString())
                    )
                    courseList.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    override fun getItemCount() = courseList.size

    fun updateData(newList: List<Course>) {
        courseList.clear()
        courseList.addAll(newList)
        notifyDataSetChanged()
    }
}
