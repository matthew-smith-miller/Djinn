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

        PartialGame.makePartialGame(0,scott.id,"K",6)
        PartialGame.makePartialGame(0,scott.id,"K",39)
        PartialGame.makePartialGame(0,scott.id,"K",29)
        PartialGame.makePartialGame(0,scott.id,"K",8)
        PartialGame.makePartialGame(0,scott.id,"K",9)
        PartialGame.makePartialGame(0,scott.id,"K",28)
        PartialGame.makePartialGame(0,matt.id,"K",62)
        PartialGame.makePartialGame(0,matt.id,"K",10)
        PartialGame.makePartialGame(0,matt.id,"K",22)
        PartialGame.makePartialGame(0,matt.id,"K",5)
        PartialGame.makePartialGame(1,matt.id,"K",57)
        PartialGame.makePartialGame(1,matt.id,"K",24)
        PartialGame.makePartialGame(1,matt.id,"K",9)
        PartialGame.makePartialGame(1,matt.id,"K",33)
        PartialGame.makePartialGame(2,matt.id,"G",60)
        PartialGame.makePartialGame(2,matt.id,"K",27)
        PartialGame.makePartialGame(2,scott.id,"K",44)
        PartialGame.makePartialGame(3,matt.id,"R",2)
        PartialGame.makePartialGame(3,matt.id,"K",40)
        PartialGame.makePartialGame(3,matt.id,"K",13)
        PartialGame.makePartialGame(3,matt.id,"K",20)
        PartialGame.makePartialGame(3,matt.id,"K",14)
        PartialGame.makePartialGame(3,scott.id,"K",12)
        PartialGame.makePartialGame(3,scott.id,"K",5)
        PartialGame.makePartialGame(3,scott.id,"G",24)
        PartialGame.makePartialGame(3,scott.id,"K",28)
        PartialGame.makePartialGame(4,scott.id,"K",24)
        PartialGame.makePartialGame(4,matt.id,"K",14)
        PartialGame.makePartialGame(4,matt.id,"K",11)
        PartialGame.makePartialGame(4,matt.id,"K",58)
        PartialGame.makePartialGame(4,matt.id,"K",41)
        PartialGame.makePartialGame(5,scott.id,"K",30)
        PartialGame.makePartialGame(5,scott.id,"K",41)
        PartialGame.makePartialGame(5,matt.id,"K",17)
        PartialGame.makePartialGame(5,scott.id,"K",19)
        PartialGame.makePartialGame(5,scott.id,"K",12)
        PartialGame.makePartialGame(6,matt.id,"K",34)
        PartialGame.makePartialGame(6,scott.id,"K",17)
        PartialGame.makePartialGame(6,scott.id,"R",0)
        PartialGame.makePartialGame(6,matt.id,"G",40)
        PartialGame.makePartialGame(6,scott.id,"K",7)
        PartialGame.makePartialGame(6,matt.id,"K",42)
        PartialGame.makePartialGame(7,scott.id,"K",18)
        PartialGame.makePartialGame(7,scott.id,"K",23)
        PartialGame.makePartialGame(7,scott.id,"R",3)
        PartialGame.makePartialGame(7,scott.id,"K",45)
        PartialGame.makePartialGame(7,scott.id,"K",10)
    }
}