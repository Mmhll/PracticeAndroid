package com.mhl.practice.model

import android.util.Log
import javax.inject.Inject

class Grid(private val grid: MutableMap<Cell, CellState> = mutableMapOf()) {

    val topLeft: CellState
        get() = grid[Cell.TOP_LEFT] ?: CellState.Empty
    val topCenter: CellState
        get() = grid[Cell.TOP_CENTER] ?: CellState.Empty
    val topRight: CellState
        get() = grid[Cell.TOP_RIGHT] ?: CellState.Empty
    val centerLeft: CellState
        get() = grid[Cell.CENTER_LEFT] ?: CellState.Empty
    val centerCenter: CellState
        get() = grid[Cell.CENTER_CENTER] ?: CellState.Empty
    val centerRight: CellState
        get() = grid[Cell.CENTER_RIGHT] ?: CellState.Empty
    val bottomLeft: CellState
        get() = grid[Cell.BOTTOM_LEFT] ?: CellState.Empty
    val bottomCenter: CellState
        get() = grid[Cell.BOTTOM_CENTER] ?: CellState.Empty
    val bottomRight: CellState
        get() = grid[Cell.BOTTOM_RIGHT] ?: CellState.Empty


    fun setCell(cell: Cell, state: CellState): Boolean {
        if (grid.containsKey(cell)) {
            return false
        }
        grid[cell] = state
        return true
    }

    fun clearGrid() {
        grid.clear()
    }

    val gridState: GridState
        get() {
            return when {
                stateWon(CellState.Cross) -> GridState.CROSS_WON
                stateWon(CellState.Zero) -> GridState.ZERO_WON
                grid.size < 9 -> GridState.INCOMPLETE
                else -> {
                    GridState.DRAW
                }
            }
        }

    private fun stateWon(state: CellState): Boolean {
        fun testState(vararg cells: Cell) = cells.all { cell ->
            grid[cell] == state
        }

        return testState(Cell.TOP_LEFT, Cell.CENTER_LEFT, Cell.BOTTOM_LEFT) ||
                testState(Cell.TOP_CENTER, Cell.CENTER_CENTER, Cell.BOTTOM_CENTER) ||
                testState(Cell.TOP_RIGHT, Cell.CENTER_RIGHT, Cell.BOTTOM_RIGHT) ||
                testState(Cell.TOP_LEFT, Cell.TOP_CENTER, Cell.TOP_RIGHT) ||
                testState(Cell.CENTER_LEFT, Cell.CENTER_CENTER, Cell.CENTER_RIGHT) ||
                testState(Cell.BOTTOM_LEFT, Cell.BOTTOM_CENTER, Cell.BOTTOM_RIGHT) ||
                testState(Cell.TOP_LEFT, Cell.CENTER_CENTER, Cell.BOTTOM_RIGHT) ||
                testState(Cell.BOTTOM_LEFT, Cell.CENTER_CENTER, Cell.TOP_RIGHT)
    }

}