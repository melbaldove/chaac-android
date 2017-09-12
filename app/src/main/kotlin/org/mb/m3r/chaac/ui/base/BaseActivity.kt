package org.mb.m3r.chaac.ui.base

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.di.ActivityComponent
import org.mb.m3r.chaac.di.DaggerActivityComponent

/**
 * @author Melby Baldove
 */
abstract class BaseActivity : AppCompatActivity() {

    open val layoutRes: Int = R.layout.activity_main
    lateinit var mToggle: ActionBarDrawerToggle

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
    }

    private fun initDrawer() {
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
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

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    val activityComponent: ActivityComponent
        get() {
            val applicationComponent = (application as ChaacApplication).applicationComponent
            return DaggerActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .build()
        }
}