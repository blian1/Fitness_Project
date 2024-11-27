package com.example.fitness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room Database class for managing the local database of the application.
 * Defines the database configuration and provides an instance of the DAO.
 *
 * @property entities Specifies the entities included in the database.
 * @property version Defines the version of the database schema.
 * @property exportSchema Determines whether to export the database schema for documentation.
 */
@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the UserDao for performing database operations on the User table.
     *
     * @return An instance of UserDao.
     */
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retrieves a singleton instance of the AppDatabase.
         * Uses synchronized block to ensure thread safety during instance creation.
         *
         * @param context The application context.
         * @return The singleton instance of the AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database" // Database name
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN name TEXT")
                db.execSQL("ALTER TABLE users ADD COLUMN age INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN height REAL")
                db.execSQL("ALTER TABLE users ADD COLUMN weight REAL")
                db.execSQL("ALTER TABLE users ADD COLUMN goal TEXT")
                db.execSQL("ALTER TABLE users ADD COLUMN bmi REAL")
            }
        }
    }
}
