package com.example.fitness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object (DAO) interface for accessing and managing user data in the database.
 * This interface provides methods for inserting and retrieving user information.
 */
@Dao
interface UserDao {

    /**
     * Inserts a new user into the database.
     *
     * @param user The User object to be inserted.
     * @return The primary key ID of the inserted user record.
     */
    @Insert
    fun insertUser(user: User): Long

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to be retrieved.
     * @return The User object corresponding to the email, or null if no user is found.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    @Query("""
        UPDATE users
        SET name = :name, age = :age, height = :height, weight = :weight, goal = :goal, bmi = :bmi
        WHERE email = :email
    """)
    fun updateUserProfile(
        email: String,
        name: String,
        age: Int,
        height: Float,
        weight: Float,
        goal: String,
        bmi: Float
    ): Int

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

}


