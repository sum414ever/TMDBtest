package com.better.tmdbtest.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.better.tmdbtest.R
import com.better.tmdbtest.databinding.ActivityMainBinding
import com.better.tmdbtest.domain.entity.UserToken
import com.better.tmdbtest.ui.auth.AuthActivity
import com.better.tmdbtest.ui.main.fragment.FilmsAllFragment
import com.better.tmdbtest.ui.main.fragment.FilmsFavoriteFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.facebook.login.LoginManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    val fragments = listOf(
        Pair("All Films", FilmsAllFragment.newInstance()),
        Pair("Favorite", FilmsFavoriteFragment.newInstance())
    )

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.saveLoginUser()
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                viewModel.saveAnonymousUser()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    askLogoutConfirmation()
                    true
                }
                else -> false
            }

        }

        observeViewModel()
        initView()
    }

    private fun askLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Confirm logout from Facebook?")
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                onUserPreferToLogout()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun onUserPreferToLogout() {
        LoginManager.getInstance().logOut()
        viewModel.saveNoLoginUser()
        val intent = Intent(this, AuthActivity::class.java)
        resultLauncher.launch(intent)
    }

    //Observe values from View Model
    private fun observeViewModel() {
        viewModel.tokenLiveData.observe(this) { accessToken ->
            if (!accessToken.isUserLogin) {
                val intent = Intent(this, AuthActivity::class.java)
                resultLauncher.launch(intent)
            }
            fillUserFields(accessToken)
        }
    }

    private fun fillUserFields(token: UserToken) {
        binding.toolbar.title = token.name

        Glide.with(this)
            .asBitmap()
            .circleCrop()
            .load(token.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val logoSize = resources.getDimension(R.dimen.toolbar_logo_size)
                    val scale = resource.scale(logoSize.toInt(), logoSize.toInt(), true)
                    binding.toolbar.logo = BitmapDrawable(resources, scale)
                }
            })
    }

    private fun initView() {
        with(binding.viewPager2) {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount() = fragments.size

                override fun createFragment(position: Int): Fragment {
                    return fragments[position].second as Fragment
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = fragments[position].first
        }.attach()
    }
}