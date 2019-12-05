package com.example.adevarsiprovocare

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.content_game.*
import java.lang.Exception
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var names: ArrayList<String>
    private lateinit var imageNames: ArrayList<String>
    private lateinit var challenges: ArrayList<String>

    private var index1 = -1
    private var index2 = -1
    private var index3 = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        setSupportActionBar(toolbar)

        fab.setOnClickListener {

            for (l in challenges) {
                Log.i("TAG", l)
            }


            //PICK 2 RANDOM PLAYERS
            var newIndex1: Int
            var newIndex2: Int
            do {
                newIndex1 = Random.nextInt(names.size)
                newIndex2 = Random.nextInt(names.size)
            } while (newIndex1 == newIndex2 || newIndex1 == index1 || newIndex2 == index2)

            setPlayer1(newIndex1)
            setPlayer2(newIndex2)

            index1 = newIndex1
            index2 = newIndex2

            //PICK A RANDOM CHALLENGE
            var newIndex3: Int
            do {
                newIndex3 = Random.nextInt(challenges.size)
            } while (newIndex3 == index3)
            challengeTextView.text = challenges[newIndex3]
        }

        names = intent.getStringArrayListExtra("names")
        imageNames = intent.getStringArrayListExtra("images")
        challenges = intent.getStringArrayListExtra("challenges")

    }

    private fun setPlayer1(index: Int) {
        player1Name.text = names[index]
        player1Image.setImageResource(getImageID(imageNames[index]))
    }

    private fun setPlayer2(index: Int) {
        player2Name.text = names[index]
        player2Image.setImageResource(getImageID(imageNames[index]))
    }


    private fun getImageID(imageName: String): Int {
        val fields = R.drawable::class.java.fields

        for (i in fields.indices) {
            try {
                val name = fields[i].name
                if (name.matches("^a[0-9]*\$".toRegex()) && fields[i].name == "a${imageName}") {
                    return fields[i].getInt(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return -1
    }
}
