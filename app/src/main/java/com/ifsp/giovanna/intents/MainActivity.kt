package com.ifsp.giovanna.intents

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
            // chamada de outra tela
            // val urlActivityIntent = Intent(this, UrlActivity::class.java)
            val urlActivityIntent = Intent("URL_ACTIVITY")
            urlActivityIntent.putExtra(URL, activityMainBinding.urlTv.text)
            //espera o retorno da outra tela
            urlArl.launch(urlActivityIntent)
        }
    }

    // coloca o menu na actionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // trata das escolhas das opcoes do meny
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.viewMi -> {
                //abrir navegador na url digitada pelo usuario
                val url = Uri.parse(activityMainBinding.urlTv.text.toString())
                val navegadorIntent = Intent(ACTION_VIEW, url)
                startActivity(navegadorIntent)
                true
            }
            else -> { false }
        }
    }
}