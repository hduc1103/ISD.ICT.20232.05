#include <iostream>
#include <sqlite3.h>
#include <vector>

// Function to execute a query and retrieve results in a vector
int executeQuery(sqlite3* db, const std::string& query, std::vector<std::vector<std::string>>& results) {
    sqlite3_stmt* stmt;
    int rc = sqlite3_prepare_v2(db, query.c_str(), -1, &stmt, nullptr);
    if (rc != SQLITE_OK) {
        std::cerr << "Failed to prepare statement: " << sqlite3_errmsg(db) << std::endl;
        return rc;
    }

    int columnCount = sqlite3_column_count(stmt);
    while (sqlite3_step(stmt) == SQLITE_ROW) {
        std::vector<std::string> row;
        for (int col = 0; col < columnCount; ++col) {
            const char* text = reinterpret_cast<const char*>(sqlite3_column_text(stmt, col));
            row.push_back(text ? text : "NULL");
        }
        results.push_back(row);
    }

    sqlite3_finalize(stmt);
    return SQLITE_OK;
}

// Function to list all tables in the database
std::vector<std::string> listTables(sqlite3* db) {
    std::vector<std::string> tables;
    std::vector<std::vector<std::string>> results;
    std::string query = "SELECT name FROM sqlite_master WHERE type='table';";

    if (executeQuery(db, query, results) == SQLITE_OK) {
        for (const auto& row : results) {
            if (!row.empty()) {
                tables.push_back(row[0]);
            }
        }
    }
    return tables;
}

// Function to display the data of a specific table
void displayTableData(sqlite3* db, const std::string& tableName) {
    std::vector<std::vector<std::string>> results;
    std::string query = "SELECT * FROM " + tableName + ";";

    if (executeQuery(db, query, results) == SQLITE_OK) {
        std::cout << "Table: " << tableName << std::endl;
        if (!results.empty()) {
            // Print column names
            for (size_t col = 0; col < results[0].size(); ++col) {
                std::cout << (col > 0 ? " | " : "") << "Column " << col + 1;
            }
            std::cout << std::endl;

            // Print row data
            for (const auto& row : results) {
                for (size_t col = 0; col < row.size(); ++col) {
                    std::cout << (col > 0 ? " | " : "") << row[col];
                }
                std::cout << std::endl;
            }
        } else {
            std::cout << "No data in this table." << std::endl;
        }
    }
}

int main() {
    sqlite3* db;
    int rc = sqlite3_open("aims.db", &db);
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        return rc;
    }

    std::vector<std::string> tables = listTables(db);
    for (const auto& table : tables) {
        displayTableData(db, table);
        std::cout << "----------------------------------------" << std::endl;
    }

    sqlite3_close(db);
    return 0;
}
