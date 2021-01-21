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
        editor.putString(idKey, user.id)
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
    fun checkHasCart(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(hasCart, false)
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(idKey, "-1")
    }
    fun getCartId(context: Context):Int{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(myCartId, -1)
    }
    fun setCartId(context: Context, cartId: Int?){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(myCartId, cartId!!)

        editor.apply()
        editor.commit()
    }
    fun getFavoriteId(context: Context):Int{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(myFavorite, -1)
    }
    fun setFavoriteId(context: Context, favoriteId: Int?){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(myFavorite, favoriteId!!)

        editor.apply()
        editor.commit()
    }
    fun setCartState(context: Context,isCart:Boolean){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(hasCart, isCart)

        editor.apply()
        editor.commit()
    }
    fun setOrderState(context: Context,isDone:Boolean){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(isBought, isDone)

        editor.apply()
        editor.commit()
    }
    fun getOrderState(context: Context):Boolean{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )

       return sharedPreferences.getBoolean(isBought, false)

    }




    const val sharedPrefFile = "user_pref";
    const val tokenKey = "token_key";
    private const val idKey = "id_key";
    const val emailKey = "email_key";
    const val passwordKey = "password_key";
     const val isActiveKey = "isActive_key";
     const val hasCart = "hasCart";
     const val myCartId = "myCartId";
     const val myFavorite = "myFavorite";
     const val isBought = "isBought";


}