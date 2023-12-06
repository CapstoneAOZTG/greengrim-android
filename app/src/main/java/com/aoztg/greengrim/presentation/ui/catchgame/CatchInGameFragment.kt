package com.aoztg.greengrim.presentation.ui.catchgame

import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCatchIngameFragmentBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CatchInGameFragment :
    BaseFragment<FragmentCatchIngameFragmentBinding>(R.layout.fragment_catch_ingame_fragment) {

    private val viewModel: CatchInGameViewModel by viewModels()
    private lateinit var trashViews: Array<ImageButton>
    val showAnim = TranslateAnimation(0f,0f,30f,0f).apply { duration = 50 }
    val hideAnim = TranslateAnimation(0f,0f,30f,0f).apply { duration = 50 }

    companion object {
        const val EMPTY = 0
        const val FILL = 1

        const val RECYCLE_TRASH = 2
        const val RECYCLE_CAN = 3
        const val TRASH = 4
        const val FOOD_TRASH = 5
    }


    private var holeState: Array<Int> =
        arrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)

    // 쓰레기 상태. 재활용쓰봉 : 0 / 재활용캔 : 1 / 쓰봉 : 2 / 음식물 : 3
    private var trashState: Array<Int> = arrayOf(
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
        RECYCLE_TRASH,
    )

    private var time = 100
    private var score = 0
    private var life = 5

    private var sleepTime = 0L
    private var progressTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            trashViews = arrayOf(
                btnTrash1,
                btnTrash2,
                btnTrash3,
                btnTrash4,
                btnTrash5,
                btnTrash6,
                btnTrash7,
                btnTrash8,
                btnTrash9,
                btnTrash10,
                btnTrash11,
                btnTrash12,
            )
        }

        gameThread()

    }

    private fun gameThread() {
        binding.ivLevelupModal.visibility = View.INVISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            recycleTrashThread(1500)
            delay(1000)
            recycleTrashThread(1100)
            delay(1000)
            recycleTrashThread(800)
            delay(1000)
            recycleCanThread()
            delay(1000)
            trashThread()
            delay(1000)
            foodTrashThread()
            timeThread()
            levelUpThread()

            // 게임 종료조건 해당시 while문 탈출
            while (life > 0 && time > 0) {

            }

            // 게임 Over
        }

    }

    private fun recycleTrashThread(speed: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = RECYCLE_TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_recycle_bag)
                    .into(trashViews[index])
                trashViews[index].animation = showAnim
                trashViews[index].visibility = View.VISIBLE

                delay(speed - sleepTime)

                trashViews[index].animation = hideAnim
                trashViews[index].visibility = View.INVISIBLE
                holeState[index] = EMPTY

                delay(speed - sleepTime)
            }
        }
    }

    private fun recycleCanThread() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = RECYCLE_CAN
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_recycle_can)
                    .into(trashViews[index])
                trashViews[index].animation = showAnim
                trashViews[index].visibility = View.VISIBLE

                delay(1000 - sleepTime)

                trashViews[index].animation = hideAnim
                trashViews[index].visibility = View.INVISIBLE
                holeState[index] = EMPTY

                delay(7000)
            }
        }
    }

    private fun trashThread() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_trash_bag)
                    .into(trashViews[index])
                trashViews[index].animation = showAnim
                trashViews[index].visibility = View.VISIBLE

                delay(1500 - sleepTime)

                trashViews[index].animation = hideAnim
                trashViews[index].visibility = View.INVISIBLE
                holeState[index] = EMPTY

                delay(1500 - sleepTime)
            }
        }
    }

    private fun foodTrashThread() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = FOOD_TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_food_trash)
                    .into(trashViews[index])
                trashViews[index].animation = showAnim
                trashViews[index].visibility = View.VISIBLE

                delay(1500 - sleepTime)

                trashViews[index].animation = hideAnim
                trashViews[index].visibility = View.INVISIBLE
                holeState[index] = EMPTY

                delay(1500 - sleepTime)
            }
        }
    }

    private fun timeThread() {
        CoroutineScope(Dispatchers.Main).launch {
            while(time > 0 ){
                time -= 1
                binding.pbTimebar.progress = time
                delay(150 - progressTime)
            }
        }
    }

    private fun levelUpThread() {

    }


    private fun exitGame() {
        findNavController().navigateUp()
    }

}