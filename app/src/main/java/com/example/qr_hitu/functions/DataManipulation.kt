package com.example.qr_hitu.functions

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.qr_hitu.components.Loading
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.components.UserChoices
import com.example.qr_hitu.data.Constants.ADMIN_COLLECTION
import com.example.qr_hitu.data.Constants.DEFAULT_COLLECTION
import com.example.qr_hitu.data.Constants.DONE_MALF_COLLECTION
import com.example.qr_hitu.data.Constants.FEEDBACK_COLLECTION
import com.example.qr_hitu.data.Constants.INVENTORY_COLLECTION
import com.example.qr_hitu.data.Constants.MALFUNCTIONS_COLLECTION
import com.example.qr_hitu.data.Constants.MISSING_COLLECTION
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//Neste ficheiro estão as funções relacionadas com a comunicação com a base de dados, como por exemplo colocar algo novo na base de dados ou remover algo

//Constante chamada db para evitar sempre que quisermos fazer acesso á firestore ter de escrever Firebase.firestore
@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore //  Variável que chama o serviço da firestore

//Procedimento para adicionar novo dispositivo
fun addDispositivo(
    block: String, //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String,
    map: HashMap<String, String>  //  A variável map passa as informações a inserir
) {

    //  .collection() identifica a coleção, .document() identifica o documento
    //  .set() define a informação do documento atual
    db.collection(INVENTORY_COLLECTION)
        .document(block)
        .collection(room)
        .document(machine)
        .set(map)

}

//Procedimento para apagar dispositivo
fun delDispositivo(
    block: String, //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String
) {

    //  .collection() identifica a coleção, .document() identifica o documento
    //  .delete() apaga o documento atual
    db.collection(INVENTORY_COLLECTION)
        .document(block)
        .collection(room)
        .document(machine)
        .delete()

}

//Função para buscar informação na firestore e guardar num mapa
@Composable
fun seeDispositivo(
    block: String, //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String
): Map<String, Any> {

    //  A variável collectionReference foi criada para não deixar o código muito denso
    //  .collection() identifica a coleção, .document() identifica o documento
    val collectionReference = db.collection(INVENTORY_COLLECTION)
        .document(block)
        .collection(room)
        .document(machine)

    //  Variáveis para guardar os valores que vêm da firestore
    val (data, setData) = remember { mutableStateOf(mapOf<String, Any>()) }

    //  Abre coroutine
    LaunchedEffect(collectionReference) {
        try {
            //  .get() consegue o documentSnapshot, .await() espera que se recupere o documentSnapshot
            val documentSnapshot = collectionReference.get().await()
            //  Define o valor de setData para os valores no documentSnapshot, se for nulo define para nulo
            setData(documentSnapshot.data ?: mapOf())
        } catch (e: Exception) {
            //  Log em caso de erro
            Log.e("Firestore", "Error retrieving data", e)
        }
    }

    //  Retorna a data
    return data

}

//Procedimento para adicionar alerta de falta de QR na base de dados
fun addMissQR(
    block: String,  //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String,
) {
    //  Cria um map com as informações
    val data = hashMapOf(
        "Bloco" to block,
        "Sala" to room,
        "Dispositivo" to machine
    )


    //  .collection() identifica a coleção, .document() identifica o documento
    //  .set() define a informação do documento atual
    db.collection(MISSING_COLLECTION)
        .document("$room $machine")
        .set(data)
}

//Procedimento para adicionar novo alerta de avaria á base de dados
fun addMalfunction(
    block: String,  //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String,
    malfunction: String,    //  As variáveis malfunction, urgent e email têm as informações sobra a avaria, se é urgente e quem a alertou
    urgent: Boolean,
    email: String,
    dateAdd: String
) {
    //  Cria um map com as informações
    val data = hashMapOf(
        "Bloco" to block,
        "Sala" to room,
        "Dispositivo" to machine,
        "Avaria" to malfunction,
        "Urgente" to urgent,
        "Email" to email,
        "Data Adicionado" to dateAdd
    )


    //  .collection() identifica a coleção, .document() identifica o documento
    //  .set() define a informação do documento atual
    db.collection(MALFUNCTIONS_COLLECTION)
        .document("$room $machine")
        .set(data)
}

//Função para ver uma avaria
@Composable
fun seeMalfunction(
    name: String,   //  As variáveis name e room identificão a máquina
    room: String,
): Map<String, Any> {
    //  collectionReference foi criada para ser menos denso o código
    val collectionReference = db.collection(MALFUNCTIONS_COLLECTION)
        .document("$room $name")

    //  Variáveis para guardar os valores que vêm da firestore
    val (data, setData) = remember { mutableStateOf(mapOf<String, Any>()) }

    //  Abre coroutine
    LaunchedEffect(collectionReference) {
        try {
            //  .get() consegue o documentSnapshot, .await() espera que se recupere o documentSnapshot
            val documentSnapshot = collectionReference.get().await()
            //  Define o valor de setData para os valores no documentSnapshot, se for nulo define para nulo
            setData(documentSnapshot.data ?: mapOf())
        } catch (e: Exception) {
            //  Log em caso de erro
            Log.e("Firestore", "Error retrieving data", e)
        }
    }

    //  Retorna a data
    return data

}

//Procedimento para apagar aviso de Falta de Qr
fun delMissing(
    room: String,   //  As variáveis room e ident fazem referência á máquina
    machine: String
) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .delete() apaga o documento atual
    db.collection(MISSING_COLLECTION)
        .document("$room $machine")
        .delete()
}

//Procedimento para completar uma avaria e guarda-la numa coleção separada para com a data em que foi completa para registo
fun completeMalfunction(
    block: String,  //  As variáveis block, room e machine são o caminho para o documento na firestore
    room: String,
    machine: String,
    malfunction: String,    //  As variáveis malfunction, urgent e email têm as informações sobra a avaria, se é urgente e quem a alertou
    urgent: Boolean,
    email: String,
    dateAdd: String
) {
    //  Variável que consegue a data e hora local
    val dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val formattedDateTime = dateTime.format(formatter);

    //  Variável que cria um map das informações
    val data = hashMapOf(
        "Bloco" to block,
        "Sala" to room,
        "Dispositivo" to machine,
        "Avaria" to malfunction,
        "Urgente" to urgent,
        "Email" to email,
        "Data Adicionado" to dateAdd,
        "Data de conclusão" to formattedDateTime.toString()
    )



    //  .collection() identifica a coleção, .document() identifica o documento
    //  .set() define a informação do documento atual
    db.collection(DONE_MALF_COLLECTION)
        .document()
        .set(data)

    //  .collection() identifica a coleção, .document() identifica o documento
    //  .delete() apaga o documento atual
    db.collection(MALFUNCTIONS_COLLECTION).document("$room $machine").delete()
}


//  Verifica se a máquina que o utilizador está a tentar criar já existe na firestore
fun qrExists(block: String, room: String, machine: String, onComplete: (Boolean) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(INVENTORY_COLLECTION)
        .document(block)
        .collection(room)
        .document(machine)
        .get()
            //  Caso encontre correspondência
        .addOnSuccessListener { documentSnapshot ->
            onComplete(documentSnapshot.exists())
        }
            //  Caso não encontre nada
        .addOnFailureListener {
            onComplete(false)
        }
}


//  Class de dados para aceder a cada campo mais facilmente. Usado na função fetchMalfList
data class MalfunctionDocs(
    val machine: String,
    val room: String,
    val block: String,
    val urgent: Boolean,
    val dateAdd: String
)
//  Procedimento para ir buscar todos os alertas de avaria á firestore
fun fetchMalfList(setList: (List<MalfunctionDocs>) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(MALFUNCTIONS_COLLECTION)
        .get()
        .addOnSuccessListener { documents ->
            //  Cria um map de todos os campos do documento excluindo os nulos
            val itemList = documents.mapNotNull { document ->
                val machine = document.getString("Dispositivo")
                val room = document.getString("Sala")
                val block = document.getString("Bloco")
                val urgent = document.getBoolean("Urgente")
                val dateAdd = document.getString("Data Adicionada")

                //  Verifica se todos os campos necessários estão preenchidos
                if (machine != null && room != null && block != null && urgent != null && dateAdd != null) {
                    MalfunctionDocs(machine, room, block, urgent, dateAdd)
                } else {
                    null
                }
            }

            //  Dá sort na list para aparecer em cima os urgentes
            val sortedList = itemList.sortedByDescending { it.urgent }
            setList(sortedList)
        }
        .addOnFailureListener { exception ->
            //  Log caso exista erro
            Log.e("Firestore", "Error getting documents: ", exception)
        }
}


//  Class de dados para aceder a cada campo mais facilmente. Usado na função getMissingQR
data class MissingQrDocs(
    val machine: String,
    val room: String,
    val block: String,
)
//  Procedimento para ir buscar todos os alertas de falta de QR á firestore
fun getMissingQR(setList: (List<MissingQrDocs>) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(MISSING_COLLECTION).get()
        .addOnSuccessListener {
            //  Cria um map de todos os campos do documento excluindo os nulos
            val itemList = it.mapNotNull { document ->
                val machine = document.getString("Dispositivo")
                val room = document.getString("Sala")
                val block = document.getString("Bloco")

                //  Verifica se todos os campos necessários estão preenchidos
                if (machine != null && room != null && block != null) {
                    MissingQrDocs(machine, room, block)
                } else {
                    null
                }
            }

            setList(itemList)
        }
        .addOnFailureListener { exception ->
            //  Log caso exista erro
            Log.e("Firestore", "Error getting documents: ", exception)
        }
}


//  Procedimento para ir buscar quais computadores a sala tem
fun existentPcs(
    block: String,
    room: String,
    setMachines: (List<String>) -> Unit
) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(INVENTORY_COLLECTION)
        .document(block)
        .collection(room)
        .get()
        .addOnCompleteListener {
            var data = ""
            //  Pega no nome dos resultados e guarda na variável data
            it.result.documents.forEach { doc ->
                data = if (data.isBlank()) doc.id else data + "," + doc.id
            }
            //  Define o estado = data separada por ,
            setMachines(data.split(","))
        }
}


//  Vai buscar o documento com o email passado para a função
suspend fun performVerification(email: String?): DocumentSnapshot {
    return withContext(Dispatchers.IO) {
        //  .collection() identifica a coleção, .document() identifica o documento
        //  .get() consegue o documentSnapshot
        return@withContext db.collection(ADMIN_COLLECTION).document("$email").get().await()
    }
}
//  Verifica se o utilizador que está tentar fazer login é um admin ou não
fun loginVerify(navController: NavController, email: String?, settingsManager: SettingsManager) {
    CoroutineScope(Dispatchers.Main).launch {
        navController.navigate(Loading.route)
        delay(1600)
        val result = performVerification(email)
        if (result.exists()) {
            settingsManager.saveSetting("Admin", "Admin")
            navController.navigate(TabScreen.route)
        } else {
            navController.navigate(UserChoices.route)
        }
    }
}



//  Procedimento para verificar se existe aviso de falta de QR Code
fun missQrExists(room: String, machine: String, onComplete: (Boolean) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(MISSING_COLLECTION)
        .document("$room $machine")
        .get()
        .addOnSuccessListener { documentSnapshot ->
            val exists = documentSnapshot.exists()
            onComplete(exists)
        }
        .addOnFailureListener {
            onComplete(false)
        }
}


//  Procedimento para ir buscar á firestore quais os padrões de avaria existentes
fun getOptions(completion: (List<String>) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(DEFAULT_COLLECTION)
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result
                //  Cria map das opções
                val optionsFromFirestore = documents?.mapNotNull { document ->
                    document.id
                }
                completion(optionsFromFirestore.orEmpty())
            } else {
                // Caso dê erro envia nulo
                completion(emptyList())
            }
    }
}
//  Procedimento para verificar se existe alerta de avaria
fun malfunctionExists(room: String, machine: String, onComplete: (Boolean) -> Unit) {
    //  .collection() identifica a coleção, .document() identifica o documento
    //  .get() consegue o documentSnapshot
    db.collection(MALFUNCTIONS_COLLECTION)
        .document("$room $machine")
        .get()
        .addOnSuccessListener { documentSnapshot ->
            val exists = documentSnapshot.exists()
            onComplete(exists)
        }
        .addOnFailureListener {
            onComplete(false)
        }
}


fun saveFeedback(feedback: String) {
    val data = hashMapOf(
        "Feedback" to feedback,
    )

    db.collection(FEEDBACK_COLLECTION)
        .document(LocalDateTime.now().toString())
        .set(data)
}