package com.fiap.edutrack.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fiap.edutrack.data.mock.MockDataProvider
import com.fiap.edutrack.repository.EduTrackRepository

/**
 * Injeção manual do [EduTrackViewModel], sem Hilt e sem Koin.
 *
 * A cadeia de criação inteira mora aqui, à vista:
 *
 * ```
 * MockDataProvider -> EduTrackRepository -> EduTrackViewModel
 * ```
 *
 * É o mesmo desenho do projeto de referência da disciplina, onde a cadeia é
 * `Context -> Database -> Dao -> Repository -> ViewModel`. Como o enunciado proíbe banco
 * local, o `MockDataProvider` ocupa a ponta que lá é o Room.
 */
object EduTrackViewModelFactory {

    val Fabrica: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val fonteDeDados = MockDataProvider
            val repositorio = EduTrackRepository(fonteDeDados)
            EduTrackViewModel(repositorio)
        }
    }
}
