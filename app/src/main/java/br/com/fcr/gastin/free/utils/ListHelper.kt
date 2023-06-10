package br.com.fcr.gastin.free.ui.utils

import java.io.Serializable

fun <T> List<T>.partitionList(): List<List<T>> {
    val partitionSize = (this.size+9) / 10 // calcular o tamanho de cada parte
    val partitions = mutableListOf<List<T>>() // lista vazia para armazenar as partes
    var temp = mutableListOf<T>() // lista temporária para armazenar elementos

    for (element in this) {
        temp.add(element) // adicionar elemento à lista temporária
        if (temp.size == partitionSize) { // se a lista temporária atingir o tamanho da parte
            partitions.add(temp.toList()) // adicionar lista temporária à lista de partes
            temp = mutableListOf() // criar uma nova lista temporária vazia
        }
    }
    if (temp.isNotEmpty()) { // adicionar os elementos restantes à última parte
        partitions.add(temp.toList())
    }
    return partitions
}
public data class Tetra<out A, out B, out C,out D>(
    public var first: @UnsafeVariance A,
    public var second: @UnsafeVariance B,
    public var third: @UnsafeVariance C,
    public var tetra: @UnsafeVariance D
) : Serializable {
    public override fun toString(): String = "($first, $second, $third, $tetra)"
}