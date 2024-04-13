package com.example.qr_hitu.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.qr_hitu.data.MalfunctionRepository
import com.example.qr_hitu.data.SelectedMalf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//  Ficheiro com o viewModel para as avarias



//  ViewModel
@HiltViewModel
class MalfunctionViewModel @Inject constructor(
    private val selectedMalfRepository: MalfunctionRepository
) : ViewModel() {

    val myData: LiveData<SelectedMalf> = selectedMalfRepository.myData

    fun setSelectedMal(name: String, room: String, block: String, urgent: Boolean, dateAdd: String) {
        val selectedMalf = SelectedMalf(name, room, block, urgent, dateAdd)
        selectedMalfRepository.setSelectedMal(selectedMalf)
    }
}