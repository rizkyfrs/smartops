package com.smartfren.smartops.data.models

data class CellInfo(
    val cellinfotype: String,
    val provider: String,
    val band: String,
    val EARFCN: String,
    val eNB: String,
    val mcc: String,
    val mnc: String,
    val tac: String,
    val cellId: String,
    val pci: String,
    val rsrp: String,
    val rsrq: String,
    val sinr: String,
    val rssi: String
)