package com.imanancin.storyapp1.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.databinding.ActivityLoginBinding
import com.imanancin.storyapp1.ui.customviews.EditTextCustomView
import com.imanancin.storyapp1.ui.register.RegisterActivity
import com.imanancin.storyapp1.ui.stories.StoriesActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: LoginActivity
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(activity)
    }


    private fun EditTextCustomView.listener() {
        this.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btnLogin.isEnabled = edLoginEmail.isValid && edLoginPassword.isValid
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        activity = this

        setupUi()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.tl1, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.tl2, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val reg = ObjectAnimator.ofFloat(binding.registerHere, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(email, pass, login, reg)
            start()
        }

    }

    private fun setupUi() {

        with(binding) {
            registerHere!!.setOnClickListener(activity)
            edLoginEmail.listener()
            edLoginPassword.listener()
            btnLogin.setOnClickListener(activity)
        }
    }

    private fun showLoading(loadingState: Boolean) {
        if(loadingState) {
            with(binding){
                btnLogin.isEnabled = false
                btnLogin.text = getString(R.string.loading)
            }
        }else {
            with(binding){
                btnLogin.isEnabled = true
                btnLogin.text = getString(R.string.login)
            }
        }
    }

    override fun onClick(p0: View) {
        when(p0.id) {
            R.id.register_here -> {
                Intent(this, RegisterActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                }
            }
            R.id.btn_login -> {
                viewModel.authenticateLogin(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString()
                ).observe(activity) { result ->
                    if(result != null) {
                        when(result) {
                            is Results.Loading -> {
                                showLoading(true)
                            }
                            is Results.Success -> {
                                Intent(activity, StoriesActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }
                            }
                            is Results.Error -> {
                                Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }
}