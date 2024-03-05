package it.crmnccgroup.crmncc

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.model.Base
import it.crmnccgroup.crmncc.ui.servizi.ServiziFragment

@AndroidEntryPoint
class NuovoInserimento : AppCompatActivity() {
    private lateinit var titolo: String
    private var obj: Base? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_inserimento)
        val extra = intent.extras //per prendere valori dall'altra activity Login
        if (extra != null) { //controllo
            titolo = extra.getString("titolo").toString()

            if(extra.containsKey("object"))
                obj = extra.getSerializable("object") as Base
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = titolo
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (titolo) {
            "Servizi" -> InserimentoServiziFragment::class.java
            "Clienti" -> InserimentoClientiFragment::class.java
            "Autisti" -> InserimentoAutistiFragment::class.java
            "Mezzi" -> InserimentoMezziFragment::class.java
            else -> ServiziFragment::class.java//da rimuovere
        }
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerViewNuovoIns, fragment).addToBackStack(null).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_nuovoins, menu)
        return true
    }

    fun getObj(): Base? {
        return obj
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}