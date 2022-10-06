package com.ifsp.giovanna.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
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
    private lateinit var permissaoChamadaArl: ActivityResultLauncher<String>
    private lateinit var pegarImagemArl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportActionBar?.subtitle = "MainActivity"

        urlArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            resultado: ActivityResult ->
            if(resultado.resultCode == RESULT_OK) {
                val urlRetornada = resultado.data?.getStringExtra(URL) ?: ""
                activityMainBinding.urlTv.text = urlRetornada
            }
        }

        permissaoChamadaArl = registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { permissaoConcedida ->
            //unit é o mesmo que void
            if (permissaoConcedida!!) {
                chamarNumero((true))
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Permissão necessária para execução",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        pegarImagemArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultado: ActivityResult ->
            if(resultado.resultCode == RESULT_OK){
                //recebendo o path da imagem
                val imagemRetornada = resultado.data?.data
                imagemRetornada?.let {
                    activityMainBinding.urlTv.text = it.toString()
                }

                //abrindo um visualizador
                val visualizarImagemIntent = Intent(ACTION_VIEW, imagemRetornada)
                startActivity(visualizarImagemIntent)
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
        //when similar ao switch case
        return when (item.itemId) {
            R.id.viewMi -> {
                //abrir navegador na url digitada pelo usuario
                val url = activityMainBinding.urlTv.text.toString()

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    Toast.makeText(this, "Url inválida", Toast.LENGTH_SHORT).show()
                    false
                }else{
                    val uri = Uri.parse(url)
                    val navegadorIntent = Intent(ACTION_VIEW, uri)
                    startActivity(navegadorIntent)
                    true
                }
            }

            R.id.dialMi -> {
                chamarNumero(false)
                true
            }

            R.id.callMi -> {
                //verificar a versao do android
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //se superior ou igual Marshmallow
                        //verificar se tem permissao e solicitar se for necessario
                    if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED){
                        //fazer a chamada
                        chamarNumero(true)
                    } else {
                        // solicitar permissao
                        permissaoChamadaArl.launch(CALL_PHONE)
                    }
                } else{
                    chamarNumero(true)
                }
                true
            }

            R.id.pickMi -> {
                val pegarImagemIntent = Intent(ACTION_PICK)
                val diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
                pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*")
                pegarImagemArl.launch(pegarImagemIntent)
                true
            }

            R.id.chooserMi -> {
                val escolherAppIntent = Intent(ACTION_CHOOSER)
                val informacoesIntent = Intent(ACTION_VIEW, Uri.parse(activityMainBinding.urlTv.text.toString()))
                escolherAppIntent.putExtra(EXTRA_TITLE, "Escolha seu navegador")
                escolherAppIntent.putExtra(EXTRA_INTENT, informacoesIntent)
                startActivity(escolherAppIntent)
                true
            }
            else -> { false }
        }
    }

    private fun chamarNumero(chamar: Boolean){
        val uri = Uri.parse("tel: ${activityMainBinding.urlTv.text}")
        val intent = Intent(if (chamar) ACTION_CALL else ACTION_DIAL)
        intent.data = uri
        startActivity(intent)
    }
}