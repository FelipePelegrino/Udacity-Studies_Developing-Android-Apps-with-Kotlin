package com.example.android.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    //Como as informações estão sendo atribuidas por DataBinding, preciso utilizalás no fragment pelo DataBinding tbm
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTitleBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_title,
                container,
                false
            )
        //função anonima para setar o clickListener
        binding.playButton.setOnClickListener {view: View ->
//            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)
//            view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
//            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_gameFragment)
            view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        this.findNavController().navigate(R.id.aboutFragment)
        return NavigationUI.onNavDestinationSelected(item!!, this.findNavController()) || super.onOptionsItemSelected(item)
    }
}