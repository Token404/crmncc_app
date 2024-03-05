package it.crmnccgroup.crmncc.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.crmnccgroup.crmncc.data.repository.autisti.AutistiRepository
import it.crmnccgroup.crmncc.data.repository.autisti.AutistiRepositoryImp
import it.crmnccgroup.crmncc.data.repository.clienti.ClientiRepository
import it.crmnccgroup.crmncc.data.repository.clienti.ClientiRepositoryImp
import it.crmnccgroup.crmncc.data.repository.mezzi.MezziRepository
import it.crmnccgroup.crmncc.data.repository.mezzi.MezziRepositoryImp
import it.crmnccgroup.crmncc.data.repository.servizi.ServiziRepository
import it.crmnccgroup.crmncc.data.repository.servizi.ServiziRepositoryImp
import it.crmnccgroup.crmncc.data.repository.utenza.UtenzaRepository
import it.crmnccgroup.crmncc.data.repository.utenza.UtenzaRepositoryImp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUtenzaRepository(
        database: FirebaseFirestore
    ): UtenzaRepository {
        return UtenzaRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideServiziRepository(
        database: FirebaseFirestore
    ): ServiziRepository {
        return ServiziRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideAutistiRepository(
        database: FirebaseFirestore
    ): AutistiRepository {
        return AutistiRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideMezziRepository(
        database: FirebaseFirestore
    ): MezziRepository {
        return MezziRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideClientiRepository(
        database: FirebaseFirestore
    ): ClientiRepository {
        return ClientiRepositoryImp(database)
    }
}