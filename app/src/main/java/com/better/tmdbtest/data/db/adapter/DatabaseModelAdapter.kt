package com.better.tmdbtest.data.db.adapter

abstract class DatabaseModelAdapter<N, D> {

    abstract fun toDomainModel(networkEntity: N): D

    fun N.convertToDomainModel(): D = toDomainModel(this)

    fun List<N>.convertToDomainModel(): List<D> = ArrayList<D>(size).apply {
        this@convertToDomainModel.forEach { add(toDomainModel(it)) }
    }

    abstract fun fromDomainModel(domainEntity: D): N

    fun D.convertFromDomainModel(): N = fromDomainModel(this)

    fun List<D>.convertFromDomainModel(): List<N> = ArrayList<N>(size).apply {
        this@convertFromDomainModel.forEach { add(fromDomainModel(it)) }
    }

}