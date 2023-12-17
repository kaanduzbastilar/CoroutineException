package com.kaanduzbastilar.coroutineexception

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("Exception : ${throwable.localizedMessage}")
        }

        lifecycleScope.launch(handler) {
            throw Exception("Error")
        }
/*
        lifecycleScope.launch(handler) {
            launch {
                throw Exception("Error 2")//biri hata verdiğinde bütün coroutine çalışmıyor ama çökmüyorda
            }
            launch {
                delay(500L)
                println("this is executed")//çalışmaz
            }
        }

 */


        lifecycleScope.launch(handler) {
            supervisorScope {// bu sorunu çözüyor
                launch {
                    throw Exception("Error 2")//biri hata verdiğinde bütün coroutine çalışmıyor ama çökmüyorda
                }
                launch {
                    delay(500L)
                    println("this is executed")//çalışmaz
                }
            }
        }

        CoroutineScope(Dispatchers.Main + handler).launch {
            launch {
                throw Exception("Error 3")
            }
        }


        /*
        lifecycleScope.launch {
            try {
                throw Exception("error") //buraya launch açsaydık throw'u içine koysaydık çökerdi scope lar yukarı doğru çıkar
                                        //launch içine try atabilirdik ama her zaman olmuyor kullanmak mantıklı değil
                                        //launch yukarı doğru propogade eder - yukarı doğru gitme
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

         */

    }
}