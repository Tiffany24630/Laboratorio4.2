/*
* Universidad del Valle de Guatemala
* PROGRAMACIÓN DE APLICACIONES MÓVILES
* Sección: 20
* Autora: Tiffany Salazar Suarez
* Carnét: 24630
* Link del video:
*/

package com.tiffany.salazar.laboratorio42jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tiffany.salazar.laboratorio42jetpack.ui.theme.Laboratorio42JetpackTheme

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            Laboratorio42JetpackTheme{
                Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)){
                        RecetaList()
                    }
                }
            }
        }
    }
}
    
data class Receta(val name: String, val imagenURL: String)

@Composable
fun RecetaForm(onReceta: (Receta) -> Unit, recetas: List<Receta>){
    var nombre by remember {mutableStateOf("")}
    var url by remember {mutableStateOf("")}
    var error by remember {mutableStateOf<String?>(null)}
    val puedeAgregar = nombre.isNotBlank() && url.isNotBlank()

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)){
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {Text("Nombre de la receta")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = url,
            onValueChange = {url = it},
            label = {Text("URL de la imagen")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                when{
                    !puedeAgregar -> error = "No puede haber campos vacíos"
                    recetas.any {it.name.equals(nombre, ignoreCase = true)} ->
                        error = "La receta ya existe"
                    else -> {
                        onReceta(Receta(nombre, url))
                        nombre = ""
                        url = ""
                        error = null
                    }
                }
            },
            enabled = puedeAgregar,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Agregar")
        }
        error?.let{
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun RecetaCard(receta: Receta, onEliminar: (Receta) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {onEliminar(receta)},
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = receta.imagenURL,
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(receta.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RecetaList(){
    var recetas = remember {mutableStateListOf<Receta>()}

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)){
        RecetaForm(
            onReceta = {recetas.add(it)},
            recetas = recetas
        )
        LazyColumn{
            items(recetas) {receta ->
                RecetaCard(
                    receta = receta,
                    onEliminar = {recetas.remove(it)}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecetaList(){
    Laboratorio42JetpackTheme{
        RecetaList()
    }
}