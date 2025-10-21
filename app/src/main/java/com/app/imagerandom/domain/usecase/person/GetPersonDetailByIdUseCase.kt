package com.app.imagerandom.domain.usecase.person

import com.app.imagerandom.data.repository.person.PersonRepository
import javax.inject.Inject

class GetPersonDetailByIdUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    fun getPersonDetailById(personId: Int) = repository.getPersonDetailById(personId)
}