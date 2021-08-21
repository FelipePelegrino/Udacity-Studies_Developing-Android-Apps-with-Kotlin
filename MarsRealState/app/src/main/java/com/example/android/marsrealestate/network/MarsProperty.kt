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
 *
 */

package com.example.android.marsrealestate.network

import com.squareup.moshi.Json

/**
 * Para utlizar o moshi precisamos definir as propriedades com os mesmos nomes
 * dos valores do objeto JSON que queremos obter assim como utilizamos o GSON
 * Mas podemos fazer de uma forma mais amigavel aos padrões Kotlin
 * Definimos a propriedade json com a annotation @Json passando como parametro o atributo qu queremos referenciar
 * a essa propriedade criada em kotlin e em kotlin podemos somente manter seu padrão de convençãod e código
 */
data class MarsProperty(
    val id: String,
    @Json(name = "img_src")
    val imgSrcUrl: String,
    val type: String,
    val price: Double
)
