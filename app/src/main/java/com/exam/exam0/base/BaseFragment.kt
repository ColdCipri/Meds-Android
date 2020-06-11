package com.exam.exam0.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VIEW_MODEL : ViewModel> : Fragment() {

    protected abstract val viewModel: VIEW_MODEL
}