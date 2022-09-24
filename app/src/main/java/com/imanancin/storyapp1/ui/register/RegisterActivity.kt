package com.imanancin.storyapp1.ui.register


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.databinding.ActivityRegisterBinding
import com.imanancin.storyapp1.ui.customviews.EditTextCustomView
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.ui.login.LoginActivity
import com.imanancin.storyapp1.ui.stories.StoriesActivity


class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(activity)
    }
    private lateinit var activity: RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        activity = this

        with(binding){
            loginHere.setOnClickListener(activity)
            edRegisterName.listener()
            edRegisterEmail.listener()
            edRegisterPassword.listener()
            btnRegister.setOnClickListener(activity)
        }

        playAnimation()

    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.rtlname, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.rtlemail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.rtlpass, View.ALPHA, 1f).setDuration(500)
        val reg = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val log = ObjectAnimator.ofFloat(binding.loginHere, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(name, email, pass, reg, log)
            start()
        }

    }


    private fun showLoading(loadingState: Boolean) {
        if(loadingState) {
            with(binding){
                btnRegister.isEnabled = false
                btnRegister.text = getString(R.string.loading)
            }
        }else {
            with(binding){
                btnRegister.isEnabled = true
                btnRegister.text = getString(R.string.register)
            }
        }
    }

    private fun EditTextCustomView.listener() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btnRegister.isEnabled = edRegisterEmail.isValid
                            && edRegisterPassword.isValid
                }
            }

        })
    }

    override fun onClick(p0: View) {
        when(p0.id) {
            R.id.login_here -> {
                Intent(activity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
            R.id.btn_register ->  {
                viewModel.doRegister(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                ).observe(activity) { result ->
                    if(result != null) {
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                Intent(activity, StoriesActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }
                            }
                            is Result.Error -> {
                                Toast.makeText(activity, "Invalid Login", Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }
}