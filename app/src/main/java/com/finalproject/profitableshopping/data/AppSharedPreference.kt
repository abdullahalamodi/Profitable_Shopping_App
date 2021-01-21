package com.finalproject.profitableshopping.data

import android.content.Context
import android.content.SharedPreferences
import com.finalproject.profitableshopping.data.models.User

object AppSharedPreference {

    fun saveUserToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(tokenKey, token)
        editor.apply()
        editor.commit()
    }

    fun saveUserData(
        context: Context,
        user: User
    ) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(idKey, Integer.valueOf(user.id))
        editor.putString(emailKey, user.email)
        editor.putString(passwordKey, user.password)
        editor.putBoolean(isActiveKey, user.isActive)
        editor.apply()
        editor.commit()
    }

    fun getUserToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(tokenKey, "")
    }

    fun getUserData(context: Context): User? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )

        return User(
            id = sharedPreferences.getString(idKey, "")!!,
            email = sharedPreferences.getString(emailKey, "")!!,
            password = sharedPreferences.getString(passwordKey, "")!!,
            isActive = sharedPreferences.getBoolean(isActiveKey, false)
        )
    }

    fun isActive(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(isActiveKey, false)
    }

    fun getUserId(context: Context): Int {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(idKey, -1)
    }

    private const val sharedPrefFile = "user_pref";
    private const val tokenKey = "token_key";
    private const val idKey = "id_key";
    private const val emailKey = "email_key";
    private const val passwordKey = "password_key";
    private const val isActiveKey = "isActive_key";
}