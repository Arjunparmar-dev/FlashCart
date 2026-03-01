package com.internshala.flash.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internshala.flash.data.InternetItem
import com.internshala.flash.network.FlashApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.firebase.database.ValueEventListener


class FlashViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

    private val _isVisible = MutableStateFlow(true)
    val isVisible = _isVisible

    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: MutableStateFlow<String> get() = _phoneNumber

    private val _cartItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cartItems: StateFlow<List<InternetItem>> get() = _cartItems.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId

    private val _ticks = MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long> get() = _ticks

    private lateinit var timerJob: Job

    val database = Firebase.database
    val myRef = database.getReference("users/${auth.currentUser?.uid}/cart")

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked: MutableStateFlow<Boolean> get() = _logoutClicked


    //private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cart")

    //private val context = application.applicationContext
    //private val cartItemsKey = stringPreferencesKey("cart_item")

    lateinit var internetJob: Job
    var screenJob: Job

    sealed interface ItemUiState {
        data class Success(val item: List<InternetItem>): ItemUiState
        object Loading: ItemUiState
        object Error: ItemUiState
    }

    fun setPhoneNumber(phoneNumber: String){
        _phoneNumber.value = phoneNumber
    }

    fun setOtp(otp: String){
        _otp.value = otp
    }

    fun setVerificationId(verificationId: String){
        _verificationId.value = verificationId
    }

    fun setUser(user: FirebaseUser){
        _user.value = user
    }

    fun clearData(){
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
        _cartItems.value = emptyList()
        resetTimer()
    }

    fun runTimer(){
        timerJob = viewModelScope.launch {
            while (_ticks.value > 0){
                delay(1000)
                _ticks.value -= 1
            }
        }
    }

    fun resetTimer(){
        try {
            timerJob.cancel()
        }catch (_: Exception){
        }finally {
            _ticks.value = 60
        }
    }

    fun setLoading(isLoading: Boolean){
        _loading.value = isLoading
    }

    fun setLogoutStatus(status: Boolean) {
        _logoutClicked.value = status
    }


    fun addToDatabase(item: InternetItem){
        myRef.push().setValue(item)
    }

    fun fillCartItems(){
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newCartItems = mutableListOf<InternetItem>()
                //_cartItems.value = emptyList()
                for (childSnapshot in dataSnapshot.children){
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        newCartItems.add(it)
                    }
                }
                _cartItems.value = newCartItems
            }

            override fun onCancelled(error: DatabaseError) {
                _cartItems.value = emptyList()
            }
        })
    }

    fun removeFromCart(oldItem: InternetItem){
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children){
                    var itemRemoved = false
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        if (oldItem.itemName == it.itemName && oldItem.itemPrice == it.itemPrice){
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }
                    }
                    if (itemRemoved) break
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
    }


    fun updateClickText(updatedText: String){
        _uiState.update {
            it.copy(
                clickStatus = updatedText
            )
        }
    }

    fun updateSelectedCategory(updatedCategory: Int) {
        _uiState.update {
            it.copy(
                selectedCategory = updatedCategory
            )
        }
    }

    fun toggleVisibility(){
        _isVisible.value = false
    }

    fun getFlashItems(){
        internetJob = viewModelScope.launch {
            try {
                val listResult = FlashApi.retrofitService.getItem()
                itemUiState = ItemUiState.Success(listResult)

            }
            catch (_: Exception){
                itemUiState = ItemUiState.Error
                toggleVisibility()
                screenJob.cancel()
            }
        }
    }

    init {
        screenJob = viewModelScope.launch(Dispatchers.Default) {
            delay(3000)
            toggleVisibility()
        }
        getFlashItems()
        fillCartItems()
    }
}