package com.mhl.practice.model

import androidx.annotation.DrawableRes
import com.mhl.practice.R

sealed class CellState(@DrawableRes val res: Int) {
    object Cross: CellState(R.drawable.cross)
    object Zero: CellState(R.drawable.zero)
    object Empty: CellState(R.drawable.players_and_grid_background)
}
