package com.internshala.flash.data
import com.internshala.flash.R


object DataSource {
    fun loadCategories(): List<Categories> {
        return listOf(
            Categories(stringResourceId = R.string.fresh_fruits, imageResourceId = R.drawable.fruits),
            Categories(R.string.bath_and_body, R.drawable.bathbody),
            Categories(R.string.bread_and_biscuits, R.drawable.bread),
            Categories(R.string.kitchen_essentials, R.drawable.kitchen),
            Categories(R.string.Munchies, R.drawable.munchies),
            Categories(R.string.packaged_food, R.drawable.packaged),
            Categories(R.string.stationery, R.drawable.stationary),
            Categories(R.string.pet_food, R.drawable.pet),
            Categories(R.string.sweet_tooth, R.drawable.sweet),
            Categories(R.string.vegetables, R.drawable.vegetables),
            Categories(R.string.beverages, R.drawable.beverages)
        )
    }

}