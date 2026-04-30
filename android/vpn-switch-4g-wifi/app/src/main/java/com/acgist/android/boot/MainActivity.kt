package com.acgist.android.boot

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


class MainActivity : ComponentActivity() {

    val luncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
//          this.baseContext.startService(Intent(this, RelayVpnService::class.java))
            this.baseContext.startService(Intent(this, Tun2socksVpnService::class.java))
        } else {
            Log.w("route", "用户拒绝")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MainNative.initialize(this);
        val intent = VpnService.prepare(this)
        if (intent != null) {
            luncher.launch(intent)
        } else {
//          this.baseContext.startService(Intent(this, RelayVpnService::class.java))
            this.baseContext.startService(Intent(this, Tun2socksVpnService::class.java))
        }
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Greeting(
                    name = "Android VPN", modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name", modifier = modifier
    )
}
