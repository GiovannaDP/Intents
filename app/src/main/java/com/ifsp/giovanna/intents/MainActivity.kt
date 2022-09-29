package com.ifsp.giovanna.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ifsp.giovanna.intents.Constant.URL
import com.ifsp.giovanna.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //acessar os arquivos da view do xml
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var urlArl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportActionBar?.subtitle = "MainActivity"

        urlArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                val urlRetornada = resultado.data?.getStringExtra(URL) ?: ""
                activityMainBinding.urlTv.text = urlRetornada
            }
        }

        activityMainBinding.entrarUrlBt.setOnClickListener {
            val urlActivityIntent = Intent(this, UrlActivity::class.java)
            urlActivityIntent.putExtra(URL, activityMainBinding.urlTv.text.toString())
            urlArl.launch(urlActivityIntent)
        }
    }
}