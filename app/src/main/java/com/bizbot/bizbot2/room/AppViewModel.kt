package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val supports = repository.getAllSupports()
    private val likeList = repository.getLikeList()
    private val searchWords = repository.getAllSearch()
    private val users = repository.getAllUser()

    fun getAllSupport(): LiveData<List<SupportModel>>{
        return this.supports
    }

    fun insertSupport(support: SupportModel){
        repository.insertSupport(support)
    }

    fun setNew(check: Boolean, id: String){
        repository.setNew(check,id)
    }

    fun setLike(check: Boolean, id: String){
        repository.setLike(check,id)
    }

    fun getLikeList(): LiveData<List<SupportModel>>{
        return likeList
    }

    fun getAllSearch(): LiveData<List<String>>{
        return this.searchWords
    }

    fun getSearchItem(word: String): LiveData<String>{
        return repository.getItemSearch(word)
    }

    fun insertSearch(word: SearchWordModel){
        repository.insertSearch(word)
    }

    fun delSearchItem(word: String){
        repository.delSearchItem(word)
    }

    fun delSearchAll(){
        repository.delSearchAll()
    }

    fun getAllUser(): LiveData<UserModel>{
        return users
    }

    fun setUser(users: UserModel){
        repository.setUser(users)
    }

    fun insertUser(users: UserModel){
        repository.insertUser(users)
    }
}