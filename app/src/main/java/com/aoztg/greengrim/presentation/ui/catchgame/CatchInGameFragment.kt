package com.aoztg.greengrim.presentation.ui.catchgame

import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCatchIngameFragmentBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.CatchGameOverDialog
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

    private val showLevelUpAnim = TranslateAnimation(0f, 0f, 200f, 0f).apply {
        duration = 400
        fillAfter = true
    }

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
    private var level = 1

    private var sleepTime = 0L
    private var progressTime = 0L
    private var pauseState = false
    private var musicstate = true

    private lateinit var soundPool: SoundPool
    private lateinit var mediaPlayer: MediaPlayer


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

        binding.btnPause.setOnClickListener {
            pauseState = true
            showPauseDialog()
        }

        soundPool = SoundPool.Builder().build()


        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.bgm)

        binding.btnMusic.setOnClickListener {
            if(musicstate){
                Glide.with(this)
                    .load(R.drawable.catch_game_sound_off)
                    .into(binding.btnMusic)
                mediaPlayer.pause()
                musicstate = false
            }else{
                Glide.with(this)
                    .load(R.drawable.catch_game_sound_on)
                    .into(binding.btnMusic)
                mediaPlayer.start()

                musicstate =  true
            }
        }

        setTrashBtnListener()
        gameThread()
    }

    private fun setTrashBtnListener() {

        val hitsound = soundPool.load(requireContext(), R.raw.hitsound,1)
        trashViews.forEachIndexed { i, btn ->
            btn.setOnClickListener {
                it.isClickable = false
                soundPool.play(hitsound, 1.0f, 1.0f, 0,0,1.0f)

                when (trashState[i]) {
                    RECYCLE_TRASH -> {

                        showHit()

                        Glide.with(this)
                            .load(R.drawable.catch_game_smile_earth)
                            .into(btn)
                        if (time < 90) {
                            time += 10
                        } else if (time > 90) {
                            time = 100
                        }

                        when (score) {
                            in (0..39) -> score++
                            in (40..1500) -> score += 2
                        }
                        binding.tvScore.text = score.toString()

                        when (score) {
                            10 -> {
                                showLevelUp()
                                progressTime += 30
                            }

                            20 -> {
                                showLevelUp()
                                progressTime += 30
                            }

                            40 -> {
                                showLevelUp()

                                sleepTime += 200
                                progressTime += 20
                            }

                            80 -> {
                                showLevelUp()

                                sleepTime += 200
                                progressTime += 20
                            }

                            160 -> {
                                showLevelUp()

                                sleepTime += 200
                                progressTime += 20
                            }

                            320 -> {
                                showLevelUp()
                                progressTime += 20
                            }
                        }
                    }

                    RECYCLE_CAN -> {
                        showHit()

                        Glide.with(this)
                            .load(R.drawable.catch_game_smile_earth)
                            .into(btn)
                        life += 1
                        binding.tvLife.text = life.toString()
                    }

                    TRASH -> {
                        showMiss()

                        Glide.with(this)
                            .load(R.drawable.catch_game_cry_earth)
                            .into(btn)
                        time -= 5
                    }

                    FOOD_TRASH -> {
                        showLifeMinus()

                        Glide.with(this)
                            .load(R.drawable.catch_game_cry_earth)
                            .into(btn)
                        life -= 1
                        binding.tvLife.text = life.toString()
                    }
                }
            }
        }
    }

    private fun gameThread() {
        mediaPlayer.start()
        Thread {
            recycleTrashThread(1500)
            recycleTrashThread(1100)
            recycleTrashThread(800)
            recycleCanThread()
            trashThread()
            foodTrashThread()
            timeThread()

            // 게임 종료조건 해당시 while문 탈출
            while (life > 0 && time > 0) {
            }

            // 게임 Over
            Handler(Looper.getMainLooper()).post {
                showGameOverDialog()
            }

        }.start()
    }

    private fun recycleTrashThread(speed: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            while (life > 0 && time > 0) {

                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = RECYCLE_TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_recycle_bag)
                    .into(trashViews[index])
                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.VISIBLE

                delay(speed - sleepTime)

                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.INVISIBLE
                trashViews[index].isClickable = true
                holeState[index] = EMPTY

                delay(speed - sleepTime)
            }
        }
    }

    private fun recycleCanThread() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            while (life > 0 && time > 0) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = RECYCLE_CAN
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_recycle_can)
                    .into(trashViews[index])
                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.VISIBLE

                delay(1000 - sleepTime)

                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.INVISIBLE
                trashViews[index].isClickable = true
                holeState[index] = EMPTY

                delay(7000)
            }
        }
    }

    private fun trashThread() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            while (life > 0 && time > 0) {
                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_trash_bag)
                    .into(trashViews[index])
                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.VISIBLE

                delay(1500 - sleepTime)

                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.INVISIBLE
                trashViews[index].isClickable = true
                holeState[index] = EMPTY

                delay(1500 - sleepTime)
            }
        }
    }

    private fun foodTrashThread() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            while (life > 0 && time > 0) {

                var index = (0..11).random()

                while (holeState[index] == 1) {
                    index = (0..11).random()
                }

                trashState[index] = FOOD_TRASH
                holeState[index] = FILL

                Glide.with(requireContext())
                    .load(R.drawable.catch_game_food_trash)
                    .into(trashViews[index])
                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.VISIBLE

                delay(1500 - sleepTime)

                trashViews[index].animation =
                    TranslateAnimation(0f, 0f, 30f, 0f).apply { duration = 50 }
                trashViews[index].visibility = View.INVISIBLE
                trashViews[index].isClickable = true
                holeState[index] = EMPTY

                delay(1500 - sleepTime)
            }
        }
    }

    private fun timeThread() {
        CoroutineScope(Dispatchers.Main).launch {
            while (life > 0 && time > 0) {
                time -= 1
                binding.pbTimebar.progress = time

                delay(150 - progressTime)
            }
        }
    }

    private fun showLevelUp() {
        CoroutineScope(Dispatchers.Main).launch {
            val levelupsound = soundPool.load(requireContext(), R.raw.levelupsound, 1)
            soundPool.play(levelupsound, 1.0f, 1.0f, 0,0,1.0f)
            level++
            binding.tvLevel.text = level.toString()
            binding.ivLevelupModal.animation = showLevelUpAnim
            binding.ivLevelupModal.visibility = View.VISIBLE

            delay(1000)

            val hideLevelUpAnim = TranslateAnimation(
                0f,
                0f,
                0f,
                binding.ivLevelupModal.width.toFloat()
            ).apply { duration = 400 }
            binding.ivLevelupModal.animation = hideLevelUpAnim
            binding.ivLevelupModal.visibility = View.GONE
        }
    }

    private fun showLifeMinus() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.ivLifeMinusModal.animation = AlphaAnimation(0f, 1f).apply { duration = 500 }
            binding.ivLifeMinusModal.visibility = View.VISIBLE

            delay(500)

            binding.ivLifeMinusModal.animation = AlphaAnimation(1f, 0f).apply { duration = 500 }
            binding.ivLifeMinusModal.visibility = View.GONE
        }
    }

    private fun showHit() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.ivHitModal.animation = AlphaAnimation(0f, 1f).apply { duration = 100 }
            binding.ivHitModal.visibility = View.VISIBLE

            delay(500)

            binding.ivHitModal.animation = AlphaAnimation(1f, 0f).apply { duration = 100 }
            binding.ivHitModal.visibility = View.GONE
        }
    }

    private fun showMiss() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.ivMissModal.animation = AlphaAnimation(0f, 1f).apply { duration = 100 }
            binding.ivMissModal.visibility = View.VISIBLE

            delay(500)

            binding.ivMissModal.animation = AlphaAnimation(1f, 0f).apply { duration = 100 }
            binding.ivMissModal.visibility = View.GONE
        }
    }

    private fun showGameOverDialog() {
        CatchGameOverDialog(requireContext(), ::exitGame, ::reStartGame).show()
    }

    private fun showPauseDialog() {
//        CatchGamePauseDialog(requireContext(), ::exitGame, ::resumeGame).show()
    }

    private fun exitGame() {
        findNavController().navigateUp()
    }

    private fun reStartGame() {
        score = 0
        level = 1
        life = 5
        time = 100
        sleepTime = 0L
        progressTime = 0L
        binding.tvScore.text = score.toString()
        binding.tvLevel.text = level.toString()
        binding.pbTimebar.progress = time
        gameThread()
    }

    private fun resumeGame() {
        pauseState = false
    }

}