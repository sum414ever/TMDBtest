package com.better.tmdbtest.data.net.adapter

abstract class NetworkModelAdapter<in N, out D> {

    abstract fun toDBModel(networkEntity: N): D

    fun N.convertToDBModel(): D = toDBModel(this)

    fun List<N>.convertToDBModel(): List<D> = ArrayList<D>(size).apply {
        this@convertToDBModel.forEach { add(toDBModel(it)) }
    }
}