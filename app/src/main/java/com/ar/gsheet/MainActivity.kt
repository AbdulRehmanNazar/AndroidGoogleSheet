package com.ar.gsheet

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ar.gsheet.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.progress)
        dialog = builder.create()

        viewBinding.getDataBtn.setOnClickListener {
            lifecycleScope.launch {
                setDialog(true)
                APIHandling().getFormData(object : APICallBack {
                    override fun getCallBack(data: Any?, statusSuccess: Boolean) {
                        setDialog(false)
                        if (statusSuccess) {
                            val notesListData = data as List<Notes>
                            viewBinding.getDataTV.text = notesListData.toString()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Something wrong happens",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                })
            }
        }

        viewBinding.addDataToSheet.setOnClickListener {
            lifecycleScope.launch {
                val title = viewBinding.titleET.text.toString()
                val description = viewBinding.descriptionET.text.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Title and description",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@launch
                }
                setDialog(true)
                APIHandling().insertDataIntoForm(
                    this@MainActivity,
                    title,
                    description,
                    object : APICallBack {
                        override fun getCallBack(data: Any?, statusSuccess: Boolean) {
                            setDialog(false)
                            if (statusSuccess) {
                                viewBinding.titleET.text.clear()
                                viewBinding.descriptionET.text.clear()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Notes Added Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Something wrong happens",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    })
            }
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog!!.show() else dialog!!.dismiss()
    }
}