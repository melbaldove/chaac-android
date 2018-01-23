package org.mb.m3r.chaac.ui.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.di.ActivityComponent
import org.mb.m3r.chaac.di.DaggerActivityComponent
import org.mb.m3r.chaac.ui.signin.SessionActionCreator
import org.mb.m3r.chaac.ui.signin.SigninActivity

/**
 * @author Melby Baldove
 */
abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    open val layoutRes: Int = R.layout.activity_main
    lateinit var mToggle: ActionBarDrawerToggle

    lateinit var activityComponent: ActivityComponent

    @BindView(R.id.drawer_layout)
    lateinit var mDrawerLayout: DrawerLayout

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)
        initDrawer()
        initActivityComponent()
    }

    private fun initDrawer() {
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        left_drawer.setNavigationItemSelectedListener(this)
    }

    private fun initActivityComponent() {
        activityComponent = (application as ChaacApplication).applicationComponent.let {
            DaggerActivityComponent.builder()
                    .applicationComponent(it)
                    .build()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mToggle.onConfigurationChanged(newConfig)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return if (mToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
        // Handle your other action bar items...

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_logout -> {
                SessionActionCreator.logout()
                navigateToSignInActivity()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }


    fun navigateToSignInActivity() {
        val intent = Intent(this, SigninActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}