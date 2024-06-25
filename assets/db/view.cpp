#include <iostream>
#include <sqlite3.h>

void printTableSchema(sqlite3* db, const char* tableName) {
    sqlite3_stmt* stmt;
    std::string query = "PRAGMA table_info(" + std::string(tableName) + ");";

    int rc = sqlite3_prepare_v2(db, query.c_str(), -1, &stmt, nullptr);
    if (rc != SQLITE_OK) {
        std::cerr << "Failed to execute query: " << sqlite3_errmsg(db) << std::endl;
        return;
    }

    std::cout << "Schema of the '" << tableName << "' table:" << std::endl;
    std::cout << "cid | name | type | notnull | dflt_value | pk" << std::endl;
    std::cout << "-------------------------------------------" << std::endl;

    while (sqlite3_step(stmt) == SQLITE_ROW) {
        int cid = sqlite3_column_int(stmt, 0);
        const char* name = reinterpret_cast<const char*>(sqlite3_column_text(stmt, 1));
        const char* type = reinterpret_cast<const char*>(sqlite3_column_text(stmt, 2));
        int notnull = sqlite3_column_int(stmt, 3);
        const char* dflt_value = reinterpret_cast<const char*>(sqlite3_column_text(stmt, 4));
        int pk = sqlite3_column_int(stmt, 5);

        std::cout << cid << " | " << (name ? name : "NULL") << " | " 
                  << (type ? type : "NULL") << " | " 
                  << notnull << " | " 
                  << (dflt_value ? dflt_value : "NULL") << " | " 
                  << pk << std::endl;
    }

    sqlite3_finalize(stmt);
}

void printTableData(sqlite3* db, const char* tableName) {
    sqlite3_stmt* stmt;
    std::string query = "SELECT * FROM " + std::string(tableName) + ";";

    int rc = sqlite3_prepare_v2(db, query.c_str(), -1, &stmt, nullptr);
    if (rc != SQLITE_OK) {
        std::cerr << "Failed to execute query: " << sqlite3_errmsg(db) << std::endl;
        return;
    }

    int columnCount = sqlite3_column_count(stmt);

    std::cout << "\nData in the '" << tableName << "' table:" << std::endl;
    for (int i = 0; i < columnCount; ++i) {
        const char* colName = sqlite3_column_name(stmt, i);
        std::cout << (colName ? colName : "NULL") << "\t";
    }
    std::cout << std::endl;

    while (sqlite3_step(stmt) == SQLITE_ROW) {
        for (int i = 0; i < columnCount; ++i) {
            const char* colText = reinterpret_cast<const char*>(sqlite3_column_text(stmt, i));
            std::cout << (colText ? colText : "NULL") << "\t";
        }
        std::cout << std::endl;
    }

    sqlite3_finalize(stmt);
}

int main() {
    sqlite3* db;
    const char* dbName = "aims.db";
    const char* tableName = "media";

    // Open the database
    int rc = sqlite3_open(dbName, &db);
    if (rc != SQLITE_OK) {
        std::cerr << "Cannot open database: " << sqlite3_errmsg(db) << std::endl;
        return rc;
    }
    printTableSchema(db, tableName);

    printTableData(db, tableName);
    sqlite3_close(db);

    return 0;
}
