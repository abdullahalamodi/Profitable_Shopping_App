package com.finalproject.profitableshopping.data

import android.content.Context
import android.content.SharedPreferences
import com.finalproject.profitableshopping.data.models.User

object AppSharedPreference {

    private fun saveUserToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(tokenKey, token)
        editor.apply()
        editor.commit()
    }

    private fun saveUserData(
        context: Context,
        user: User
    ) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(idKey, user.id)
        editor.putString(emailKey, user.email)
        editor.putString(passwordKey, user.password)
        editor.putBoolean(isActiveKey, user.isActive)
        editor.apply()
        editor.commit()
    }

    private fun getUserToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(tokenKey, null)
    }

    private fun getUserData(context: Context): User? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )

        return User(
            id = sharedPreferences.getString(idKey, null)!!,
            email = sharedPreferences.getString(emailKey, null)!!,
            password = sharedPreferences.getString(passwordKey, null)!!,
            isActive = sharedPreferences.getBoolean(isActiveKey, false)
        )
    }

    private fun isActive(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(isActiveKey, false)
    }

    private fun getUserId(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(idKey, null)!!
    }

    private const val sharedPrefFile = "user_pref";
    private const val tokenKey = "token_key";
    private const val idKey = "id_key";
    private const val emailKey = "email_key";
    private const val passwordKey = "password_key";
    private const val isActiveKey = "isActive_key";
}