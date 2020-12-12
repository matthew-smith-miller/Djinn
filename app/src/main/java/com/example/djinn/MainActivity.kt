package com.example.djinn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView : ListView = findViewById<ListView>(R.id.listview_all)
        val adapter = RivalryAdapter(this, Rivalry.rivalries)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val selectedRivalryId = view.tag.toString().toInt()
            val intent = Intent(this, RivalryActivity::class.java).apply {
                putExtra(RIVALRY, selectedRivalryId)
            }
            startActivity(intent)
        }
        makeData()
    }

    fun makeData() {
        val matt = Player.makePlayer("Matt Miller")
        val neil = Player.makePlayer("Neil Katuna")
        val scott = Player.makePlayer("Scott Liftman")
        val barack = Player.makePlayer("Barack Obama")
        val neilRivalry = Rivalry.makeRivalry(neil.id)
        val barackRivalry = Rivalry.makeRivalry(barack.id)
        val scottRivalry = Rivalry.makeRivalry(scott.id)

        for (Int in 1..8) {
            Game.makeGame(scottRivalry.id)
        }

        PartialGame.makePartialGame(0, scott.id, "Knock", 6)
        PartialGame.makePartialGame(0, scott.id, "Knock", 39)
        PartialGame.makePartialGame(0, scott.id, "Knock", 29)
        PartialGame.makePartialGame(0, scott.id, "Knock", 8)
        PartialGame.makePartialGame(0, scott.id, "Knock", 9)
        PartialGame.makePartialGame(0, matt.id, "Knock", 62)
        PartialGame.makePartialGame(0, matt.id, "Knock", 10)
        PartialGame.makePartialGame(0, matt.id, "Knock", 22)
        PartialGame.makePartialGame(0, matt.id, "Knock", 5)
        PartialGame.makePartialGame(0, scott.id, "Knock", 28)
        PartialGame.makePartialGame(1, matt.id, "Knock", 57)
        PartialGame.makePartialGame(1, matt.id, "Knock", 24)
        PartialGame.makePartialGame(1, matt.id, "Knock", 9)
        PartialGame.makePartialGame(1, matt.id, "Knock", 33)
        PartialGame.makePartialGame(2, matt.id, "Gin", 60)
        PartialGame.makePartialGame(2, scott.id, "Knock", 44)
        PartialGame.makePartialGame(2, matt.id, "Knock", 27)
        PartialGame.makePartialGame(3, matt.id, "Reverse", 2)
        PartialGame.makePartialGame(3, matt.id, "Knock", 40)
        PartialGame.makePartialGame(3, matt.id, "Knock", 13)
        PartialGame.makePartialGame(3, scott.id, "Knock", 5)
        PartialGame.makePartialGame(3, scott.id, "Gin", 24)
        PartialGame.makePartialGame(3, matt.id, "Knock", 13)
        PartialGame.makePartialGame(3, matt.id, "Knock", 20)
        PartialGame.makePartialGame(3, scott.id, "Knock", 12)
        PartialGame.makePartialGame(3, scott.id, "Knock", 28)
        PartialGame.makePartialGame(3, matt.id, "Knock", 14)
        PartialGame.makePartialGame(4, scott.id, "Knock", 24)
        PartialGame.makePartialGame(4, matt.id, "Knock", 14)
        PartialGame.makePartialGame(4, matt.id, "Knock", 11)
        PartialGame.makePartialGame(4, matt.id, "Knock", 58)
        PartialGame.makePartialGame(4, matt.id, "Knock", 41)
        PartialGame.makePartialGame(5, scott.id, "Knock", 30)
        PartialGame.makePartialGame(5, scott.id, "Knock", 41)
        PartialGame.makePartialGame(5, matt.id, "Knock", 17)
        PartialGame.makePartialGame(5, scott.id, "Knock", 19)
        PartialGame.makePartialGame(5, scott.id, "Knock", 12)
        PartialGame.makePartialGame(6, matt.id, "Knock", 34)
        PartialGame.makePartialGame(6, scott.id, "Knock", 17)
        PartialGame.makePartialGame(6, scott.id, "Reverse", 0)
        PartialGame.makePartialGame(6, matt.id, "Gin", 40)
        PartialGame.makePartialGame(6, scott.id, "Knock", 7)
        PartialGame.makePartialGame(6, matt.id, "Knock", 42)
        PartialGame.makePartialGame(7, scott.id, "Knock", 18)
        PartialGame.makePartialGame(7, scott.id, "Knock", 23)
        PartialGame.makePartialGame(7, scott.id, "Reverse", 3)
        PartialGame.makePartialGame(7, scott.id, "Knock", 45)
        PartialGame.makePartialGame(7, scott.id, "Knock", 10)
    }
}