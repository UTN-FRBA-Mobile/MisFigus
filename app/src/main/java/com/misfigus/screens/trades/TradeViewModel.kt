package com.misfigus.screens.trades

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.misfigus.dto.PossibleTradeDto

class TradeViewModel : ViewModel() {
    var selectedTrade = mutableStateOf<PossibleTradeDto?>(null)
}