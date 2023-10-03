package com.sangam.todoapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sangam.todoapp.RecyclerViewFiles.DataClassTask
import com.sangam.todoapp.RecyclerViewFiles.DataObject
import com.sangam.todoapp.RecyclerViewFiles.MyAdapter
import com.sangam.todoapp.RoomDB.EntityTask
import com.sangam.todoapp.RoomDB.TaskDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    private val calendar = Calendar.getInstance()
    lateinit var spinner: Spinner
    lateinit var formatteddate: String
    lateinit var selectedItem: String
    lateinit var myAdapter: MyAdapter

    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(100)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.TRANSPARENT
        database = TaskDatabase.getDatabase(this)

        fab = findViewById(R.id.floatingActionButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        getData()
        fab.setOnClickListener {
            newTaskDialog()
        }

        myAdapter = MyAdapter(DataObject.getData())
        recyclerView.adapter = myAdapter
        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClick(task: String?, position: Int) {
                openUpdateDialog(task!!, position)
            }
        })
        swipeToDelete()

    }


    fun newTaskDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = layoutInflater.inflate(R.layout.dialog_add_new_task, null)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(view)
        dialog.show()

//        calendar= Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        spinner = view.findViewById(R.id.spinnerPriority)
        val datePicker = view.findViewById<ExtendedFloatingActionButton>(R.id.btnDueDate)
        val addTask = view.findViewById<ExtendedFloatingActionButton>(R.id.btnAddtask)
        val edittextTask = view.findViewById<EditText>(R.id.etNewTask)
        choosePrioritySpinner()
        datePicker.setOnClickListener {
            datePickerDialog()
        }
        addTask.setOnClickListener {
            val task = edittextTask.text.toString()


            if (task.trim().isNotEmpty() && selectedItem != "Choose Priority") {
                DataObject.setData(
                    task, dateFormat, formatteddate,
                    selectedItem
                )
                GlobalScope.launch {
                    database.taskdao().insertTask(
                        EntityTask(
                            0, task, dateFormat, formatteddate,
                            selectedItem
                        )
                    )
                }
                myAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun openUpdateDialog(task: String, pos: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.update_dialog, null)
        dialog.setContentView(view)
        spinner = view.findViewById(R.id.spinnerUpdatePriority)
        choosePrioritySpinner()
        val datePicker = view.findViewById<ExtendedFloatingActionButton>(R.id.btnUpdateDueDate)
        val addTask = view.findViewById<ExtendedFloatingActionButton>(R.id.btnUpdateTask)
        val edittextTask = view.findViewById<EditText>(R.id.etUpdateTask)
        edittextTask.setText(task)
        datePicker.setOnClickListener {
            datePickerDialog()
        }
        addTask.setOnClickListener {
            val newTask = edittextTask.text.toString()
            DataObject.updateDate(pos, newTask, formatteddate, selectedItem)
            myAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun datePickerDialog() {
        val currentDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formatteddate = dateFormat.format(selectedDate.time)
                // Set the formatted date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = currentDate.timeInMillis
        datePickerDialog.show()
    }

    private fun choosePrioritySpinner() {
        ArrayAdapter.createFromResource(
            this, R.array.spinnerArray, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                // Do Whatever With SelectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Nothing Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(2, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deleteTask: DataClassTask =
                    DataObject.listData.get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                DataObject.listData.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                myAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.

                Snackbar.make(recyclerView, "Deleted " + deleteTask.task, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.parseColor("#E266F7"))
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            DataObject.listData.add(position, deleteTask)

                            // below line is to notify item is
                            // added to our adapter class.
                            myAdapter.notifyItemInserted(position)
                        }).show()
            }
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                // Draw your background drawable here
//                // You can use the dX parameter to determine how far the item is swiped
//                // and draw your background accordingly.
//
//                // Example:
//                val itemView = viewHolder.itemView
//                val background = ContextCompat.getDrawable(
//                    recyclerView.context,
//                    R.drawable.ic_baseline_archive_24
//                )// Replace with your custom drawable
//                val left = itemView.left
//                val right = itemView.right
//                val top = itemView.top
//                val bottom = itemView.bottom
//
//                if (dX > 0) { // Swiping to the right
//                    background?.setBounds(left, top, left + dX.toInt(), bottom)
//                } else if (dX < 0) { // Swiping to the left
//                    background?.setBounds(right + dX.toInt(), top, right, bottom)
//                } else {
//                    background?.setBounds(0, 0, 0, 0)
//                }
//
//                background?.draw(c)
//
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView)
    }

    fun getData() {
        if (DataObject.listData.isNotEmpty()) {
            GlobalScope.launch {
                val tasks = database.taskdao().getTask() as MutableList<DataClassTask>
                DataObject.listData.clear()
                DataObject.listData.addAll(tasks)
                myAdapter.notifyDataSetChanged()

            }

        } else {
            Toast.makeText(this, "Write Something", Toast.LENGTH_SHORT).show()
        }
    }


}