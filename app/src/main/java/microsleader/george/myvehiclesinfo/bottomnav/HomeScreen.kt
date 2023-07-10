package microsleader.george.myvehiclesinfo.bottomnav

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import microsleader.george.myvehiclesinfo.classes.Vehicle


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun HomeScreen() {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            //.background(MaterialTheme.colors.onSurface),
            .background(Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,

    ) {
        Text(
            text = "Home Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                // .fillMaxSize()
                .align(CenterHorizontally)
                .padding(top = 20.dp),
            color = MaterialTheme.colors.onSurface
        )




        val ctx = LocalContext.current;
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid
        val vehiclesArrayList = ArrayList<Vehicle>()
        val db = Firebase.firestore

        var vList = vehiclesArrayList.toList()

        val vehiclesLista = remember { mutableStateListOf<Vehicle>() }

        DropDownVehicles(vehiclesLista)

        db.collection("Vehicles")
            .whereEqualTo("userID", uid)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val v = document.toObject(Vehicle::class.java)
                    Log.d(TAG, "${document.id} => " + v)
                    vehiclesLista.add(v)
                }
                vList = vehiclesArrayList.toList()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(items = vehiclesLista, itemContent = { item ->
                Log.d("COMPOSE", "This get rendered $item")
                Text(text = item.brand!!)
            })
        }

        //FirebaseUI(LocalContext.current, courseList)
    }



}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownVehicles(vehiclesLista: SnapshotStateList<Vehicle>) {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(Vehicle("","",0,0,0)) }
    //var selectedOptionText = Vehicle("","",0,0,0)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText.brand!!,
            onValueChange = { },
            label = { Text("Vehicles") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            vehiclesLista.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ){
                    Text(text = selectionOption.brand!!)
                }
            }
        }
    }
}















