package com.example.qr_hitu.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import com.example.qr_hitu.data.CreateQr1
import com.example.qr_hitu.data.CreateQr2
import com.example.qr_hitu.data.SelectedMalf

class ScannerRepository @Inject constructor() {
    private val _myLiveData = MutableLiveData<String>()
    val myLiveData: LiveData<String> = _myLiveData

    fun liveData(code: String) {
        _myLiveData.value = code
    }
}


class MalfunctionRepository @Inject constructor() {
    private val _myData = MutableLiveData<SelectedMalf>()
    val myData: LiveData<SelectedMalf> = _myData

    fun setSelectedMal(selectedMalf: SelectedMalf) {
        _myData.value = selectedMalf
    }
}

class CreateQr1Repository @Inject constructor() {
    private val _myData = MutableLiveData<CreateQr1>()
    val myData: LiveData<CreateQr1> = _myData

    fun liveData(data: CreateQr1) {
        _myData.value = data
    }
}

class CreateQr2Repository @Inject constructor() {
    private val _myData = MutableLiveData<CreateQr2>()
    val myData: LiveData<CreateQr2> = _myData

    fun liveData(data: CreateQr2) {
        _myData.value = data
    }
}