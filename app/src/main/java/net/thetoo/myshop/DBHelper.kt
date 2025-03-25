package net.thetoo.myshop

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShopDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Items (id INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, name TEXT, cost TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Items")
        onCreate(db)
    }

    fun insertItem(number: Int, name: String, cost: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("number", number)
        values.put("name", name)
        values.put("cost", cost)
        db.insert("Items", null, values)
        db.close()
    }

    fun deleteItem(id: Int) {
        val db = writableDatabase
        db.delete("Items", "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun updateItem(id: Int, number: Int, name: String, cost: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("number", number)
        values.put("name", name)
        values.put("cost", cost)
        db.update("Items", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllItems(): MutableList<Quad<Int, Int, String, String>> {
        val list = mutableListOf<Quad<Int, Int, String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, number, name, cost FROM Items", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val number = cursor.getInt(1)
            val name = cursor.getString(2)
            val cost = cursor.getString(3)
            list.add(Quad(id, number, name, cost))
        }
        cursor.close()
        db.close()
        return list
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

