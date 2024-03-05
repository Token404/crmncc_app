package it.crmnccgroup.crmncc.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.NuovoInserimento
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.ui.auth.Login
import it.crmnccgroup.crmncc.ui.autisti.AutistiFragment
import it.crmnccgroup.crmncc.ui.clienti.ClientiFragment
import it.crmnccgroup.crmncc.ui.mezzi.MezziFragment
import it.crmnccgroup.crmncc.ui.servizi.ServiziFragment
import it.crmnccgroup.crmncc.util.UiState

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var drawerView: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private lateinit var auth: FirebaseAuth
    private var drawerToggle: ActionBarDrawerToggle? = null
    private val viewModel: UtenzaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //settare la toolbar al posto della actionbar di base

        drawerView = findViewById(R.id.drawer_layout) //trova il drawer
        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setItemTextAppearance(R.style.NavigationViewStyle)
        setDefaultFragment()
        setupDrawer(navView) //setta il drawer


        viewModel.getUtenza()
        viewModel.utenza.observe(this) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    navView.getHeaderView(0).findViewById<TextView>(R.id.headerNA).text = state.data.azienda
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }

        //serve a cambiare l'icona dell'hamburger menu
        val actionBar = supportActionBar
        actionBar?.apply {
            actionBar.setDisplayHomeAsUpEnabled(true)

            drawerToggle = setupDrawerToggle()

            (drawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
            drawerView!!.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
            (drawerToggle as ActionBarDrawerToggle).syncState()
        }
    }
    private fun setupDrawerToggle(): ActionBarDrawerToggle { //setta drawerToggle
        return ActionBarDrawerToggle(
            this,
            drawerView,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
    }
    //serve per il drawerToggle
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nuovoServizio) {
            startActivity(Intent(this@MainActivity, NuovoInserimento::class.java).putExtra("titolo", title))
            finish()
        }
        when (item.itemId) {
            //serve a far aprire/chiudere il drawer
            android.R.id.home -> {
                drawerView!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setDefaultFragment() {
        // Inserisco il fragment dei base
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMain, ServiziFragment::class.java.getDeclaredConstructor().newInstance() as Fragment).addToBackStack(null).commit()

        // la schermata home è servizi
        //item.isChecked = true
        title = "Servizi"
    }

    private fun setupDrawer(navView: NavigationView) {
        navView.setNavigationItemSelectedListener { menuItem ->
            selectOggettoDrawer(menuItem)
            true
        }
    }

    private fun selectOggettoDrawer(item: MenuItem) {
        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (item.itemId) {
            R.id.bottoneServizi -> ServiziFragment::class.java
            R.id.bottoneClienti -> ClientiFragment::class.java
            R.id.bottoneAutisti -> AutistiFragment::class.java
            R.id.bottoneMezzi -> MezziFragment::class.java
            else -> ServiziFragment::class.java
        }
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (item.itemId == R.id.bottoneLogout) {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        // Inserisco il fragment selezionato al posto di quello corrente
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMain, fragment).addToBackStack(null).commit()
        }

        // Highlight della voce selezionata nel menu
        item.isChecked = true
        // Setta il titolo della toolbar
        title = item.title
        // Chiude il drawer
        drawerView!!.closeDrawers()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}