package com.exam.exam0.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VIEW_MODEL : ViewModel> : AppCompatActivity() {

    protected abstract val viewModel: VIEW_MODEL
}