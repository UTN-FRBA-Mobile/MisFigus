package com.misfigus.screens.trades

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.misfigus.dto.PossibleTradeDto
import com.misfigus.dto.TradeRequestDto

class TradeViewModel : ViewModel() {
    var selectedTrade = mutableStateOf<PossibleTradeDto?>(null)
    var selectedTradeRequest = mutableStateOf<TradeRequestDto?>(null)
}