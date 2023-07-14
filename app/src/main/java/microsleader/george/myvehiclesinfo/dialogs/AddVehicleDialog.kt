package microsleader.george.myvehiclesinfo.dialogs

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import microsleader.george.myvehiclesinfo.FireStoreMethods.Companion.addVehicle
import microsleader.george.myvehiclesinfo.R
import microsleader.george.myvehiclesinfo.classes.Vehicle
import microsleader.george.myvehiclesinfo.myTests.Brands
import microsleader.george.myvehiclesinfo.myTests.Models


@Composable
fun AddVehicleDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val brand = remember { mutableStateOf(value) }
    val model = remember { mutableStateOf(value) }
    val year = remember { mutableStateOf(value) }
    val kivika = remember { mutableStateOf(value) }
    val hp = remember { mutableStateOf(value) }
    val db = Firebase.firestore
    val brandsLista = remember { mutableStateListOf<Brands>() }
    val modelsLista = remember { mutableStateListOf<Models>() }

    var selectedBrand by remember { mutableStateOf("") }
    var selectedBrandText by remember { mutableStateOf(Brands(0,"")) }


    var showDialog by remember {mutableStateOf(true)}
    val openDialog = remember { mutableStateOf(true) }
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        DropDownBrands(brandsLista , selectedBrandText)
                        db.collection("Brands")
                            .orderBy("name")
                            .get()
                            .addOnSuccessListener { documents ->

                                for (document in documents) {
                                    val v = document.toObject(Brands::class.java)
                                    Log.d(ContentValues.TAG, "${document.id} => " + v)
                                    brandsLista.add(v)
                                }
                              //  vList = vehiclesArrayList.toList()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                            }





                        DropDownModels(modelsLista)
                        db.collection("Models")
                            .whereEqualTo("make",selectedBrand)
                            .orderBy("make")
                            .get()
                            .addOnSuccessListener { documents ->

                                for (document in documents) {
                                    val v = document.toObject(Models::class.java)
                                    Log.d(ContentValues.TAG, "${document.id} => " + v)
                                    modelsLista.add(v)
                                }
                                //  vList = vehiclesArrayList.toList()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                            }





//                        LazyColumn(modifier = Modifier.height(200.dp)) {
//                            items(items = brandsLista, itemContent = { item ->
//                               // Log.d("COMPOSE", "This get rendered $item")
//                                Text(text = item.name!!)
//                            })
//                        }
                        // GeneralTextField("Brand", brand, KeyboardOptions(keyboardType = KeyboardType.Text),200)
                        Spacer(modifier = Modifier.width(20.dp))
                        GeneralTextField("Model", model, KeyboardOptions(keyboardType = KeyboardType.Text),200)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        GeneralTextField("Year", year, KeyboardOptions(keyboardType = KeyboardType.Number),8)
                        Spacer(modifier = Modifier.width(20.dp))


                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){                  GeneralTextField("Kivika", kivika, KeyboardOptions(keyboardType = KeyboardType.Number),8)
                        Spacer(modifier = Modifier.width(20.dp))
                        GeneralTextField("HP", hp, KeyboardOptions(keyboardType = KeyboardType.Number),8)
                    }


                    Spacer(modifier = Modifier.height(20.dp))
                    val context = LocalContext.current;

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {

                                Toast.makeText(context, brand.value, Toast.LENGTH_LONG).show()
                                val v = Vehicle(
                                    brand.value ,
                                    model.value,
                                    year.value.toInt(),
                                    kivika.value.toInt(),
                                    hp.value.toInt()

                                );
                                val msg = addVehicle(context , v);
                                if (msg.isEmpty()) {
                                    Toast.makeText(context, R.string.success_vehicle_store, Toast.LENGTH_LONG).show();
                                    setShowDialog(false)
                                }
                                else
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()


                                //println(brand);
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }




}







@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownBrands(brandsLista: SnapshotStateList<Brands>,  selectedBrand: Brands) {

    var expanded by remember { mutableStateOf(false) }
    var modelsList = remember { mutableStateListOf<Models>()}
    var selectedBrand by remember { mutableStateOf(selectedBrand) }
    //var modelsList by remember { mutableStateOf(value )}
    //var selectedOptionText = Vehicle("","",0,0,0)
    val db = Firebase.firestore

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedBrand.name!!,
            onValueChange = {
               // xaxa = it
            },
            label = { Text("Brand") },
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
            brandsLista.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedBrand = selectionOption
                        expanded = false

                        //Log.d("xaxaxaxa",selectedBrand.name.toString())

                        var xaxa = getModelsList(selectedBrand.name.toString())
                        modelsList.clear()
                        xaxa.forEach{
                            modelsList.add(it)
                        }



                    }
                ){
                    Text(text = selectionOption.name!!)
                }
            }
        }
    }
}




fun getModelsList(selectedBrandName: String): List<Models> {

    val db = Firebase.firestore
    val modelsLista = ArrayList<Models>();
    db.collection("Models")
        .whereEqualTo("make",selectedBrandName)
        .orderBy("make")
        .get()
        .addOnSuccessListener { documents ->

            for (document in documents) {
                val v = document.toObject(Models::class.java)
                Log.d(ContentValues.TAG, "${document.id} => " + v)
                modelsLista.add(v)
            }

        }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }
    return   modelsLista.toList()
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownModels(modelsLista: SnapshotStateList<Models>) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(Models(0,"")) }
    //var selectedOptionText = Vehicle("","",0,0,0)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText.make!!,
            onValueChange = { },
            label = { Text("Model") },
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
            modelsLista.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ){
                    Text(text = selectionOption.make!!)
                }
            }
        }
    }
}





@Composable
fun GeneralTextField(
    label: String, mutValue: MutableState<String>, keyboardOptions: KeyboardOptions, textLength:Int
) {
    //val txtField = remember { mutableStateOf(value) }
    val txtFieldError = remember { mutableStateOf("") }


    TextField(
        label = { Text(label) },
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = if (txtFieldError.value.isEmpty()) R.color.purple_200 else R.color.teal_200)
                ),
                shape = RoundedCornerShape(100)
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                tint = colorResource(android.R.color.holo_green_light),
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        },
        placeholder = { Text(text = "") },
        value = mutValue.value,
        keyboardOptions = keyboardOptions,
        onValueChange = {
            if (keyboardOptions.keyboardType == KeyboardType.Number && it.isEmpty())
                mutValue.value = "0"
            else
                mutValue.value = it.take(textLength)
        })
}

