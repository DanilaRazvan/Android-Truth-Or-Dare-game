package com.example.adevarsiprovocare

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_prepare.*
import kotlinx.android.synthetic.main.content_prepare.*
import java.io.File
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class PrepareActivity : AppCompatActivity(), ExampleDialog.ExampleDialogListener,
    RecyclerViewAdapterPlayers.RecyclerViewListener {

    companion object {
        val names = ArrayList<String>()
        val images = ArrayList<String>()
        const val FILE_NAME = "test.txt"
        val lines = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare)
//        setSupportActionBar(toolbar)


        fab.setOnClickListener {
            openDialog()
        }

        loadChallenges()

        for(l in lines) {
            Log.i("TAG", l)
        }
    }

    private fun loadChallenges() {
        val file = File(getExternalFilesDir(null), "test.txt")

        file.bufferedReader().use {
            it.forEachLine { l ->
                lines.add(l)
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        initRecyclerView()
    }

    fun startButtonClicked(view: View) {
        if (names.size < 2) {
            Toast.makeText(
                applicationContext,
                "There must be at least 2 players",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("names", names)
        intent.putExtra("images", images)
        intent.putExtra("challenges", lines)
        startActivity(intent)
    }

    override fun saveChanges(name: String, imageName: String) {
        names.add(name)
        images.add(imageName)

        update()
    }

    override fun update() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = RecyclerViewAdapterPlayers(this, names, images)

        playersLayout.apply {
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(this@PrepareActivity)
        }
    }

    private fun openDialog() {
        val exampleDialog = ExampleDialog()
        exampleDialog.show(supportFragmentManager, "example dialog")
    }
}
