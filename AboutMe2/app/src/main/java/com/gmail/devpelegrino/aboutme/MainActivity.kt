package com.gmail.devpelegrino.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.gmail.devpelegrino.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myName: MyName = MyName("Felipão")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.myName = myName
        binding.buttonDone.setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
        //function usada comumente para a configuração de propriedades, ela retorna uma referencia do próprio objeto
        //tornando o código mais consiso e de fácil leitura
        binding.apply {
            //atualizamos o binding, para garantir a integridade da UI
            invalidateAll()
            myName?.nickname = editNickname.text.toString()
            editNickname.visibility = View.GONE
            view.visibility = View.GONE
            textName.visibility = View.VISIBLE
        }

        //Esconde o teclado ao clicar no OK do teclado interno
        val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(view.windowToken, 0)
    }
}