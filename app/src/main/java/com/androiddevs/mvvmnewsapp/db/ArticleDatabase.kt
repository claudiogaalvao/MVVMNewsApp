package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile //Other thread can immediately see when a thread changes this instance
        private var instance: ArticleDatabase? = null
        // We'll use that to synchronize setting that instance,
        // so that we really make sure that there is only a single instance of our database
        private val LOCK = Any()

        // invoke is called whenever we initialize or instantiate the ArticleDatabase class
        // this block of code can be accessed by other threads at the same time,
        // so we really make sure that won't set that there's not another thread that sets this instance
        // to something while we already said it
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}