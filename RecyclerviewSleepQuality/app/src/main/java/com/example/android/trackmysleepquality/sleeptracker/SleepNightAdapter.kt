/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

/**
 * Mesma lógica como visto na matéria de DMO em Java
 * Só se atentar a quando for utilizar mais de 1 layout na view, fazer as devidas tratativas
 * Com viewHolder e viewType no onCreate
 * -------
 * Quando utilizamos a classe ListAdapter que recebe objetos genéricos, não precisamos sobreescrever getItenCount
 * E podemos remover a lista das variáveis locais, pois o ListAdapter, gerencia esses recursos para nós
 * A lista será atribuida através do método submitList() onde o tipo de lista é o primeiro parametro genérico que definimos
 * ao extender a classe.
 * Mas temos quie implementar a classe de callback com os métodos areItemsTheSame e areContentsTheSame
 */

class SleepNightAdapter : ListAdapter <SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * A intenção da classe ViewHolder, como o nome sugere, é gerenciar os processos relacionados a UI
     * Para isso, extraimos o código que estava no onBind, para a fun bind presente no view holder, que capta o dado
     * e acrescenta na View
     * Ao deixar o construtor privado e criar o companion object, dizemos que a instancia dessa classe só ocorre
     * através dela mesma, pela chamada da function static from
     * -----
     * A outras maneiras de lidar com o refatoramento de código como por exemplo o DataBinding
     * Se atentar em definir o val no binding do construtor
     */
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight) {
            binding.sleepData = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Utilizado para atualizar apenas um item caso ele seja alterado, para isso implementaremos os métodos da classe interna
 * ItemCallback da classe DiffUtil
 * Mas para essa implementação ser usada pelo meu RecycleView, preciso chamar a classe ListAdapter
 */
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}