package microsleader.george.myvehiclesinfo.bottomnav
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import microsleader.george.myvehiclesinfo.myTests.OverviewViewModel
import retrofit2.Response
import retrofit2.http.GET
import java.util.*
@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun CategoryScreen() {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            //.background(MaterialTheme.colors.onSurface),
            .background(Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "Home Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                // .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            color = MaterialTheme.colors.onSurface
        )


        val ctx = LocalContext.current
        val userName = remember {
            mutableStateOf(TextFieldValue())
        }
        val job = remember {
            mutableStateOf(TextFieldValue())
        }
        val response = remember {
            mutableStateOf("")
        }

        //ΜΕΘΟΔΟΣ ΠΟΥ ΒΑΖΩ ΤΑ ΜΟΝΤΕΛΑ ΚΑΙ ΤΙΣ ΜΑΡΚΕΣ ΣΤΗ ΒΑΣΗ
        Button(onClick ={        val viewModel = OverviewViewModel()
            // var x = viewModel.viewModelScope.launch {  };
        }) {
        }


        //FirebaseUI(LocalContext.current, courseList)
    }



}





data class DataModel(
    // on below line we are creating variables for name and job
    var year: Int,
    var id: Int,
    var horsepower: Int,
    var make: String,
    var model: String,
    var price: Int,
    var img_url: String
)
interface RetrofitAPI {
    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @GET("cars")
    fun  // on below line we are creating a method to post our data.
          //  getData(@Body dataModel: DataModel?): Call<DataModel?>?
            getData(): Response<String>?
}





