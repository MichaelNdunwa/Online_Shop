package com.example.onlineshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.ItemsModel
import com.example.onlineshop.model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainViewModel() : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // Create Banner LiveData:
    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner

    // Create Category LiveData:
    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    val categories: LiveData<MutableList<CategoryModel>> = _category

    // Create Recommended LiveData:
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()
    val recommended: LiveData<MutableList<ItemsModel>> = _recommended

    // Fetch banners from Firebase Realtime Database
    fun loadBanners() {
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Fetch categories from Firebase Realtime Database
    fun loadCategories() {
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryList = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        categoryList.add(list)
                    }
                }
                _category.value = categoryList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    // Fetch filtered Items from Firebase Realtime Database
    fun loadFiltered(id: String) {
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("categoryId").equalTo(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null ) {
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Fetch recommended Items from Firebase Realtime Database
    fun loadRecommended() {
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("showRecommended").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null ) {
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}