package com.ainsln.binlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ainsln.binlist.ui.theme.BinListTheme
import com.ainsln.network.BinNetworkDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataSource: BinNetworkDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BinListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        dataSource,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, datasource: BinNetworkDataSource? = null, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    datasource?.let { source ->
        scope.launch {
            val response = source.get("45717360")
            Log.d("TAG", "$response")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BinListTheme {
        Greeting("Android")
    }
}
