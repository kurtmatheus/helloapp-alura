package br.com.alura.helloapp.database.migrations

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@RenameColumn(
    "Usuario",
    "nomeUsuario",
    "idUsuario"
)
class Migration3To4: AutoMigrationSpec

val MIGRATION_5_6 = object : Migration(5,6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS ContatoCopia (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL, `sobrenome` TEXT NOT NULL, `telefone` TEXT NOT NULL, `fotoPerfil` TEXT NOT NULL, `aniversario` INTEGER, `idUsuario` TEXT NOT NULL DEFAULT '', FOREIGN KEY(`idUsuario`) REFERENCES `Usuario`(`idUsuario`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        database.execSQL( "INSERT INTO ContatoCopia SELECT * FROM Contato")
        database.execSQL("DROP TABLE Contato")
        database.execSQL("ALTER TABLE ContatoCopia RENAME TO Contato")
    }

}