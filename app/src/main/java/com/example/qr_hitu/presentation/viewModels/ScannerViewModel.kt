package com.example.qr_hitu.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.qr_hitu.data.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject




//  ViewModel do Scanner
@HiltViewModel
class ScannerViewModel @Inject constructor(
    //  Variáveis que guardam a informação
    private val myRepository: ScannerRepository // Assuming you have a repository to interact with data
) : ViewModel() {

    // LiveData to observe data changes
    val myData: LiveData<String> = myRepository.myLiveData

    // Function to set data
    fun setMyData(code: String) {
        myRepository.liveData(code)
    }
}