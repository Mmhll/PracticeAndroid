package com.mhl.practice.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhl.practice.R
import com.mhl.practice.model.Cell
import com.mhl.practice.model.CellState
import com.mhl.practice.model.Grid
import com.mhl.practice.model.LocalRepository
import com.mhl.practice.utils.GridEntity
import com.mhl.practice.utils.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class GameViewModel @Inject constructor(
    @Named("localRepository") private val localRepository: LocalRepository
) : ViewModel() {
    private var grid = Grid()
    val liveGrid = MutableLiveData(grid)
    val currentTurn = MutableLiveData<Int>()
    private var turn = 0
    private var _user = UserEntity()
    val user: MutableLiveData<UserEntity> = MutableLiveData<UserEntity>()

    init {
        getUser()
        getGrid()
    }

    private fun updateGrid() {
        liveGrid.value = grid
        saveGrid()
    }


    fun gridClicked(cell: Cell) {
        when (turn) {
            0 -> {
                if (grid.setCell(cell, CellState.Cross)) {
                    updateGrid()
                    turn = 1
                }
            }
            1 -> {
                if (grid.setCell(cell, CellState.Zero)) {
                    updateGrid()
                    turn = 0
                }
            }
        }
        currentTurn.value = turn
    }


    fun resetGrid() {
        grid.clearGrid()
        updateGrid()
        turn = 0
    }

    private fun getUser() {
        viewModelScope.launch {
            localRepository.getUser().collect {
                _user = it
                user.value = _user
            }
        }
    }

    fun saveGrid() {
        var gridString = ""
        gridString += "${grid.topLeft.res}|"
        gridString += "${grid.topCenter.res}|"
        gridString += "${grid.topRight.res}|"
        gridString += "${grid.centerLeft.res}|"
        gridString += "${grid.centerCenter.res}|"
        gridString += "${grid.centerRight.res}|"
        gridString += "${grid.bottomLeft.res}|"
        gridString += "${grid.bottomCenter.res}|"
        gridString += "${grid.bottomRight.res}"
        viewModelScope.launch {
            localRepository.saveGrid(GridEntity(0, gridString, turn))
        }

    }

    fun getGrid() {
        viewModelScope.launch {
            var gridString = ""
            var savedTurn = 0
            localRepository.getGrid().collect {

                try {
                    gridString = it.cells
                    savedTurn = it.turn
                } catch (e: NullPointerException){

                }
            }
            if (gridString.isNotEmpty()) {
                loadGrid(gridString)
                turn = savedTurn
            }
        }

    }

    private fun loadGrid(gridString: String) {
        val savedGrid = Grid()
        val gridArray = gridString.split("|")
        Log.d("TAG", gridArray.toString())
        for (i in 0 until Cell.values().size) {
            when (gridArray[i].toInt()) {
                R.drawable.cross ->
                    savedGrid.setCell(Cell.values()[i], CellState.Cross)
                R.drawable.zero ->
                    savedGrid.setCell(Cell.values()[i], CellState.Zero)
                R.drawable.players_and_grid_background -> {}
            }
        }
        grid = savedGrid
        currentTurn.value = turn
        updateGrid()
    }
}