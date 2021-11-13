package br.com.lodjinha.utils

import android.view.View

fun View.toggleVisibilty(status: Boolean) {
    if (status) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}