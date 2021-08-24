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

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Para utlizar o moshi precisamos definir as propriedades com os mesmos nomes
 * dos valores do objeto JSON que queremos obter assim como utilizamos o GSON
 * Mas podemos fazer de uma forma mais amigavel aos padrões Kotlin
 * Definimos a propriedade json com a annotation @Json passando como parametro o atributo qu queremos referenciar
 * a essa propriedade criada em kotlin e em kotlin podemos somente manter seu padrão de convençãod e código
 *
 * Implementar o Parcelable dessa forma, é sucetivel a erros sempre que o código for atualizado
 * pois caso não se atualize os métodos (que acontecem de forma sequencial a leitura e escrita de um parcel)
 * a aplicação pode crashar
 */
data class MarsProperty(
    val id: String,
    @Json(name = "img_src")
    val imgSrcUrl: String,
    val type: String,
    val price: Double
):Parcelable() {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgSrcUrl)
        parcel.writeString(type)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarsProperty> {
        override fun createFromParcel(parcel: Parcel): MarsProperty {
            return MarsProperty(parcel)
        }

        override fun newArray(size: Int): Array<MarsProperty?> {
            return arrayOfNulls(size)
        }
    }

}
