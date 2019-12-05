package com.example.adevarsiprovocare

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import java.lang.Exception

class ExampleDialog : AppCompatDialogFragment() {

    interface ExampleDialogListener {
        fun saveChanges(name: String, image: String)
    }

    private lateinit var listener: ExampleDialogListener
    private lateinit var name: EditText

    private lateinit var image: ImageView
    private var imageNames = ArrayList<String>()

    private val names = PrepareActivity.names

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_layout, null)

        name = view!!.findViewById(R.id.nameEditText)
        image = view.findViewById(R.id.playerImage)
        var imageName = "1"

        builder.apply {
            setView(view)
            setTitle("Add new player")

            setNegativeButton("CANCEL") { _, _ ->

            }
            setPositiveButton("ADD") { _, _ ->
                var playerName = name.text.toString()

                if (playerName == "") {
                    playerName = (names.size + 1).toString()
                }

                listener.saveChanges(playerName, imageName)
            }
        }
        val dialog = builder.create()

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val s = text.toString()
                when {
                    s == "" -> {
                        name.error = "Name must not be empty"
                        dialog.getButton(Dialog.BUTTON_POSITIVE).visibility = View.INVISIBLE
                    }

                    names.contains(s) -> {
                        name.error = "Name already used"
                        dialog.getButton(Dialog.BUTTON_POSITIVE).visibility = View.INVISIBLE
                    }

                    else -> dialog.getButton(Dialog.BUTTON_POSITIVE).visibility = View.VISIBLE
                }
            }

        })

        //setImages
        val array = getImages()
        val res = Array(array.size) { 0 }
        array.toArray(res)
        val imagesGV = view.findViewById<GridView>(R.id.imagesView)
        imagesGV.adapter = ImageAdapter(context!!, res)

        imagesGV.setOnItemClickListener { _, _, position, _ ->
            image.setImageResource(res[position])
            imageName = imageNames[position]
        }

        return dialog
    }

    private fun getImages(): ArrayList<Int> {
        val fields = R.drawable::class.java.fields
        val resArray = ArrayList<Int>()

        for (i in fields.indices) {
            try {
                val name = fields[i].name
                if (name.matches("^a[0-9]*\$".toRegex())) {
                    resArray.add(fields[i].getInt(null))
                    val imgName = fields[i].name
                    imageNames.add(imgName.substring(1, imgName.length))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return resArray
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as ExampleDialogListener
        } catch (e: Exception) {
            Log.i("TAG", e.toString())
        }
    }


    //adapter for displaying images in the dialog
    class ImageAdapter(
        private val context: Context,
        private val res: Array<Int>
    ) : BaseAdapter() {
        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            lateinit var imageView: ImageView
            if (view == null) {
                imageView = ImageView(context)
                imageView.layoutParams = ViewGroup.LayoutParams(200, 200)
                imageView.setPadding(8, 8, 8, 8)
            } else {
                imageView = view as ImageView
            }

            imageView.setImageResource(res[position])
            return imageView
        }

        override fun getItem(p0: Int): Any {
            return res[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return res.size
        }

    }
}