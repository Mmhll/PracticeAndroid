package com.mhl.practice.view

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mhl.practice.R
import com.mhl.practice.databinding.FragmentGameBinding
import com.mhl.practice.model.Cell
import com.mhl.practice.model.CellState
import com.mhl.practice.model.Grid
import com.mhl.practice.model.GridState
import com.mhl.practice.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()
    private var cross = 0
    private var zero = 0
    private var username: String = "Первый игрок"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater)
        binding.gameTimer.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        viewModel.getGrid()


        binding.gameMenu.setOnClickListener {
            val menu = PopupMenu(requireContext(), it)
            val inflaterMenu = menu.menuInflater
            inflaterMenu.inflate(R.menu.game_menu, menu.menu)

            menu.show()
            menu.setOnMenuItemClickListener { item ->
                if (item.title == "Выйти") {
                    findNavController().navigate(R.id.action_gameFragment_to_authorizationFragment)
                }
                return@setOnMenuItemClickListener true
            }
        }

        val updateGrid = Observer<Grid> { grid ->
            binding.gameGridItemImage1.setImageResource(grid.topLeft.res)
            binding.gameGridItemImage2.setImageResource(grid.topCenter.res)
            binding.gameGridItemImage3.setImageResource(grid.topRight.res)
            binding.gameGridItemImage4.setImageResource(grid.centerLeft.res)
            binding.gameGridItemImage5.setImageResource(grid.centerCenter.res)
            binding.gameGridItemImage6.setImageResource(grid.centerRight.res)
            binding.gameGridItemImage7.setImageResource(grid.bottomLeft.res)
            binding.gameGridItemImage8.setImageResource(grid.bottomCenter.res)
            binding.gameGridItemImage9.setImageResource(grid.bottomRight.res)



            when (grid.gridState) {
                GridState.CROSS_WON -> {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Победили крестики")
                        .setOnCancelListener {
                            viewModel.resetGrid()
                            firstTurn()
                        }
                        .show()
                }
                GridState.ZERO_WON -> {

                    AlertDialog.Builder(requireContext())
                        .setMessage("Победили нолики")
                        .setOnCancelListener {
                            viewModel.resetGrid()
                            firstTurn()
                        }
                        .show()
                }
                GridState.DRAW -> {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Ничья")
                        .setOnCancelListener {
                            viewModel.resetGrid()
                            firstTurn()
                        }
                        .show()

                }
                GridState.INCOMPLETE -> {}
            }
        }
        viewModel.user.observe(viewLifecycleOwner){
            binding.firstPlayerName.text = it.fullName
            username = it.fullName
        }

        viewModel.liveGrid.observe(viewLifecycleOwner, updateGrid)
        bindClickEvents()

        viewModel.currentTurn.observe(viewLifecycleOwner) {
            if (cross == it && cross == 0) {
                binding.movePlayerName.text = username
                binding.moveCharacterImage.setImageResource(R.drawable.cross)
            } else if (cross == it && cross == 1) {
                binding.movePlayerName.text = "Второй игрок"
                binding.moveCharacterImage.setImageResource(R.drawable.cross)
            } else if (cross != it && cross == 1) {
                binding.movePlayerName.text = username
                binding.moveCharacterImage.setImageResource(R.drawable.zero)
            } else {
                binding.movePlayerName.text = "Второй игрок"
                binding.moveCharacterImage.setImageResource(R.drawable.zero)
            }
        }


    }

    private fun bindClickEvents() {
        binding.gameGridItemImage1.setOnClickListener {
            viewModel.gridClicked(
                Cell.TOP_LEFT,
            )
        }
        binding.gameGridItemImage2.setOnClickListener {
            viewModel.gridClicked(
                Cell.TOP_CENTER,
            )
        }
        binding.gameGridItemImage3.setOnClickListener {
            viewModel.gridClicked(
                Cell.TOP_RIGHT,
            )
        }
        binding.gameGridItemImage4.setOnClickListener {
            viewModel.gridClicked(
                Cell.CENTER_LEFT,
            )
        }
        binding.gameGridItemImage5.setOnClickListener {
            viewModel.gridClicked(
                Cell.CENTER_CENTER,
            )
        }
        binding.gameGridItemImage6.setOnClickListener {
            viewModel.gridClicked(
                Cell.CENTER_RIGHT,
            )
        }
        binding.gameGridItemImage7.setOnClickListener {
            viewModel.gridClicked(
                Cell.BOTTOM_LEFT,
            )
        }
        binding.gameGridItemImage8.setOnClickListener {
            viewModel.gridClicked(
                Cell.BOTTOM_CENTER,
            )
        }
        binding.gameGridItemImage9.setOnClickListener {
            viewModel.gridClicked(
                Cell.BOTTOM_RIGHT,
            )
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.game_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.title.toString() == "Выйти") {
                    findNavController().navigate(R.id.action_gameFragment_to_authorizationFragment)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun firstTurn() {

        binding.gameTimer.base = SystemClock.elapsedRealtime()

        viewModel.resetGrid()
        cross = Random.nextInt(0, 2)

        when (cross) {
            0 -> {
                binding.movePlayerName.text = username
                zero = 1
            }
            1 -> binding.movePlayerName.text = "Второй игрок"
        }
        binding.moveCharacterImage.setImageResource(R.drawable.cross)
        Thread.sleep(1000)

        binding.gameTimer.start()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveGrid()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}