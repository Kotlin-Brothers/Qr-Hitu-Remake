package com.example.qr_hitu.data

//  Class de dados para mais facilmente aceder á informação
data class SelectedMalf(
    val name : String,
    val room : String,
    val block: String,
    val urgent : Boolean,
    val dateAdd : String
)

//  Classes de dados para mais facilmente aceder á informação
data class CreateQr1(
    val block : String,
    val room : String,
    val machine : String
)

data class CreateQr2(
    val name : String,
    val processor : String,
    val ram : String,
    val powerSupply : String
)