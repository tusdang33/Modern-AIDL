package com.kaizm.serverbank.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kaizm.serverbank.DatabaseConst
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DatabaseConst.DATABASE_TABLE_NAME, indices = [Index("userId")])
data class UserAccount(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    val userId: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "balance")
    var balance: Double = 0.0

) : Parcelable