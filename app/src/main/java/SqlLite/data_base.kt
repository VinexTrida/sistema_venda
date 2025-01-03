package SqlLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class data_base(context: Context):SQLiteOpenHelper(context, DataBaseName, null, DataBaseVersion){
    companion object{
        const val DataBaseName = "BDLocal.db"
        const val DataBaseVersion = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT UNIQUE  NOT NULL,
                caixa INTEGER,
                senha VARCHAR(15) NOT NULL,
                admin INTEGER DEFAULT 0 --usado no lugar do tinyint(1)
            )
        """)

        db?.execSQL("""
            CREATE TABLE produtos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT UNIQUE,
                preco REAL,
                quantidade INTEGER,
                inerente INTEGER, --usado no lugar do tinyint(1)
                emUso INTEGER, --usado no lugar do tinyint(1)
                posicao INTEGER,
                combo INTEGER DEFAULT 0, --usado no lugar do tinyint(1)
                caixas TEXT DEFAULT '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,'
            )
        """)

        db?.execSQL("""
            CREATE TABLE ralacao_combos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                combo_id INTEGER NOT NULL, --id do combo na tabela de produto
                produto_id INTEGER NOT NULL, --id do produto na tabela de produto
                quantidade INTEGER NOT NULL,
                FOREIGN KEY (combo_id) REFERENCES produtos(id),
                FOREIGN KEY (produto_id) REFERENCES produtos(id)
            )
        """)

        db?.execSQL("""
            CREATE TABLE log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                caixa INTEGER,
                pagamento TEXT,
                data TEXT,
                hora TEXT,
                info TEXT,
                valor REAL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aqui você atualiza o banco de dados se a versão mudar
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        db?.execSQL("DROP TABLE IF EXISTS produtos")
        db?.execSQL("DROP TABLE IF EXISTS ralacao_combos")
        db?.execSQL("DROP TABLE IF EXISTS log")
        onCreate(db)
    }
}