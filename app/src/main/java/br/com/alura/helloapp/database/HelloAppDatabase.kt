package br.com.alura.helloapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.helloapp.data.Contato
import br.com.alura.helloapp.data.Usuario
import br.com.alura.helloapp.database.converters.*
import br.com.alura.helloapp.database.migrations.Migration3To4

@Database(
    entities = [Contato::class, Usuario::class],
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(2,3),
    AutoMigration(3,4, Migration3To4::class),
    AutoMigration(4,5)
    ]
)
@TypeConverters(Converters::class)
abstract class HelloAppDatabase : RoomDatabase() {
    abstract fun contatoDao(): ContatoDao
    abstract fun usuarioDao(): UsuarioDao
}