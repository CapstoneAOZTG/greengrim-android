package com.aoztg.greengrim.presentation.ui.catchgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCatchGameLobbyBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatchGameLobbyFragment: BaseFragment<FragmentCatchGameLobbyBinding>(R.layout.fragment_catch_game_lobby) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtnListener()
        Glide.with(requireContext())
            .load(R.raw.catch_game_announce_black)
            .into(binding.ivGif)
    }

    private fun setBtnListener(){
        binding.btnExit.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.btnStart.setOnClickListener {
            findNavController().toInGame()
        }
    }

    private fun NavController.toInGame(){
        val action = CatchGameLobbyFragmentDirections.actionLobbyFragmentToIngameFragment()
        navigate(action)
    }


}