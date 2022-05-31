package com.bushelpowered.pokedex.service

import com.bushelpowered.pokedex.service.entities.Pokemon
import com.bushelpowered.pokedex.service.entities.Trainer
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import java.util.Optional

@Service

class TrainerService(val trainerDB: TrainerRepository, db: PokemonRepository) : PokemonService(db) {

    fun getAllTrainers(): MutableIterable<Trainer> = trainerDB.findAll()

    fun getTrainerById(trainerId: Long): Optional<Trainer> = trainerDB.findById(trainerId)

    fun addTrainer(trainer: Trainer) = trainerDB.save(trainer)

    fun addPokemon(pokeId: String, trainer: Optional<Trainer>): Trainer {
        return if (trainer.isPresent) {
            trainerDB.save(Trainer(
                email = trainer.get().email,
                password = trainer.get().password,
                capturedPokemon = trainer.get().capturedPokemon + " " + pokeId,
                id = trainer.get().id
            ))
        } else throw NotFoundException()
    }

    fun findTrainersPokemon(trainer: Optional<Trainer>) : Any {
        return if(trainer.isPresent){
            val listOfPokeID = trainer.get().capturedPokemon.split(" ")
            val listOfCapturedPokemon: MutableList<Pokemon> = mutableListOf()
            listOfPokeID.forEach { pokemon -> if(findPokemonById(pokemon).isPresent){
                    listOfCapturedPokemon.add(findPokemonById(pokemon).get())
                } else throw  NotFoundException()
                return listOfCapturedPokemon
            }
        } else throw NotFoundException()
    }
}

