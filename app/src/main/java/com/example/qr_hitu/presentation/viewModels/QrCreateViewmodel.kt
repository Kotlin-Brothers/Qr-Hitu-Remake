package com.example.qr_hitu.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.qr_hitu.data.CreateQr1
import com.example.qr_hitu.data.CreateQr1Repository
import com.example.qr_hitu.data.CreateQr2
import com.example.qr_hitu.data.CreateQr2Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//  Ficheiro com os viewModels para a criação de um QR Code

//  ViewModel 1
@HiltViewModel
class ViewModel1 @Inject constructor(
    private val myRepository: CreateQr1Repository
) : ViewModel() {

    val myData: LiveData<CreateQr1> = myRepository.myData

    fun setMyData1(block: String, room: String, machine: String) {
        val data = CreateQr1(block, room, machine)
        myRepository.liveData(data)
    }
}

//  ViewModel 2
@HiltViewModel
class ViewModel2 @Inject constructor(
    private val createQrRepository: CreateQr2Repository
) : ViewModel() {

    val myData: LiveData<CreateQr2> = createQrRepository.myData

    fun setMyData2(name: String, processor: String, ram: String, powerSupply: String) {
        val data = CreateQr2(name, processor, ram, powerSupply)
        createQrRepository.liveData(data)
    }
}