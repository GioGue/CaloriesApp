package com.example.caloriesapp

import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.widget.NumberPicker.OnValueChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.caloriesapp.ui.theme.CaloriesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CalorieScreen()
                }
            }
        }
    }
}

@Composable
fun CalorieScreen() {
    var weightInput by remember { mutableStateOf("") }
    val weight : Int = weightInput.toIntOrNull() ?:0
    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf(1.3f) }
    var result by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement= Arrangement.spacedBy(8.dp),

    ){
        Heading(title = stringResource(R.string.titleCalories))
        WeightField(weightInput = weightInput, onValueChange = {weightInput = it} )
        GenderChoices(male = male, setGenderMale = {male = it} )
        IntensityList(onClick = {intensity = it})
        Text (text = result.toString(),
            color = MaterialTheme.colors.secondary,
            fontSize = 24.sp, // add by me
            fontWeight = FontWeight.Bold)
        Calculation(male = male, weight = weight , intensity = intensity, setResult = {result = it} )
    }

}

@Composable
fun Heading(title: String){
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold, //I've added this
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)

    )
    
}
@Composable
fun WeightField(weightInput: String, onValueChange: (String) -> Unit){
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = {Text(text= stringResource(R.string.enterWeight))},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
    )

}
@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit){
    Column(
        modifier = Modifier.selectableGroup(),
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = male,
                onClick = { setGenderMale(true)}
            )
            Text(text = stringResource(R.string.ButtonMale))
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = !male, //female
                onClick = { setGenderMale(false)}
            )
            Text(text = stringResource(R.string.ButtonFemale))
        }
    }
}

@Composable
fun IntensityList(onClick: (Float)-> Unit){
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Column(
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
            label = {Text(text = stringResource(R.string.selectIntensity))},
            trailingIcon = {Icon(icon, "content description",
                Modifier.clickable { expanded = !expanded })},
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = { selectedText = label
                    var intensity: Float = when (label) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very hard" -> 2.2f
                        else -> 0.0f
                    }
                    onClick(intensity)
                    expanded = false

                }) {
                    Text(text = label)
            }
            }
        }
    }
}
@Composable
fun Calculation(male: Boolean, weight: Int , intensity: Float, setResult:(Int)-> Unit ){
    Button(
        onClick = {
                  if (male) {
                      setResult(((879 + 10.2 * weight) * intensity).toInt())
                  } else {
                      setResult(((795 + 7.18 * weight) * intensity).toInt())
                  }

        } ,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = stringResource(R.string.calculate)
            )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CaloriesAppTheme {
        CalorieScreen()
    }
}