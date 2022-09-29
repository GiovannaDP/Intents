package com.ifsp.giovanna.intents

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ifsp.giovanna.intents.Constant.URL
import com.ifsp.giovanna.intents.databinding.ActivityUrlBinding

class UrlActivity : AppCompatActivity() {

    private lateinit var aub: ActivityUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aub = ActivityUrlBinding.inflate(layoutInflater)
        setContentView(aub.root)

        supportActionBar?.subtitle = "URLActivity"

        val urlAnterior = intent.getStringExtra(URL) ?: ""

        //duas maneiras de fazer essa verificação
//        if(urlAnterior.isNotEmpty()) {
//            aub.urlEt.setText(urlAnterior)
//        }
        urlAnterior.takeIf { it.isNotEmpty() }.also { aub.urlEt.setText(it) }

        aub.entrarUrlBt.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?){
                val retornoIntent = Intent()
                retornoIntent.putExtra(URL, aub.urlEt.text.toString())
                //só irá retornar no clique do botao passado na funcao, nao irá retornar caso seja pelo botao back
                setResult(RESULT_OK, retornoIntent)
                //encerra o ciclo da view
                finish()
            }
        })
    }
}