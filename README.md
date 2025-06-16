# 📚 CESAE Digital Courses App (Android – Kotlin)

An Android application developed in **Kotlin** that allows users to view, add, edit, courses offered by CESAE Digital. The app leverages **SQLite** for data persistence, **RecyclerView** for dynamic course listing, and **ViewBinding** for robust UI handling.

---

## ✨ Features

- ✅ **Main Menu** with background image and navigation buttons  
- 📋 **Course List** displayed using `RecyclerView`  
- ➕ **Add new courses** with all required fields  
- ✏️ **Edit or delete** existing courses  
- 🖼️ Optional **image selection** for each course  
- 📁 **SQLite database** with full CRUD support  
- 🔍 **Sorting options** by name or start date (asc/desc)  
- 🧭 **About screen** with creator details  
- 🧼 Clean and modern UI with Material Design  

---

## 📦 Tech Stack

| Tool/Framework | Usage |
|----------------|-------|
| **Kotlin**     | Main programming language |
| **SQLite**     | Local database for storing course data |
| **ViewBinding**| Type-safe view references |
| **RecyclerView** | Efficient list rendering |
| **Material Design** | UI components and styling |


---
## 🛠️ Setup Instructions

- Clone the repository:
git clone [https://github.com/drowuid/GestaoCursosAPP.git]
- Open in Android Studio Arctic Fox or later
- Sync Gradle & run the app on an emulator or real device
-Add or edit courses, and enjoy!


---

## 🧩 Project Structure

```text
📂 app
 ┣ 📁 data
 ┃ ┗ 📄 CourseDatabaseHelper.kt      # SQLite schema and DB helper
 ┣ 📁 model
 ┃ ┗ 📄 Course.kt                    # Data class for course structure
 ┣ 📁 adapter
 ┃ ┗ 📄 CourseAdapter.kt            # RecyclerView adapter
 ┣ 📄 MainActivity.kt               # Welcome screen
 ┣ 📄 CourseListActivity.kt         # List all courses
 ┣ 📄 AddEditCourseActivity.kt      # Add/Edit course form
 ┣ 📄 CourseDetailsActivity.kt      # Read-only course view
 ┣ 📄 AboutActivity.kt              # App creator info
 ┣ 📂 res/layout                    # All layout XMLs
 ┣ 📂 res/drawable                  # Images/icons
 ┣ 📄 AndroidManifest.xml



```
---
This app was created as a study project and training tool for Android app development using Kotlin. It demonstrates modern practices like ViewBinding, SQLite usage, UI state management, and app structure.

Developed by Pedro Rodrigues
