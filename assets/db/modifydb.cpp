#include <iostream>
#include <vector>
#include <sqlite3.h>
#include <string>
#include <algorithm> // For std::shuffle
#include <random>    // For std::default_random_engine

// Function to execute a single SQL command and print success or error message
bool executeSQL(sqlite3* db, const std::string& sql) {
    char* errorMessage = nullptr;
    int rc = sqlite3_exec(db, sql.c_str(), nullptr, nullptr, &errorMessage);
    if (rc != SQLITE_OK) {
        std::cerr << "SQL error: " << errorMessage << std::endl;
        sqlite3_free(errorMessage);
        return false;
    }
    return true;
}

// Function to escape single quotes in a string
std::string escapeSingleQuotes(const std::string& input) {
    std::string output;
    for (char c : input) {
        if (c == '\'') {
            output += "''"; // Double up single quotes
        } else {
            output += c;
        }
    }
    return output;
}

int main() {
    sqlite3* db;
    int rc = sqlite3_open("aims.db", &db);  // Adjust the path as necessary
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        return rc;
    }

    sqlite3_stmt* stmt;
    std::vector<std::vector<std::string>> rows;
    const char* query = "SELECT * FROM media;";

    // Step 1: Read all rows from the media table
    rc = sqlite3_prepare_v2(db, query, -1, &stmt, nullptr);
    if (rc != SQLITE_OK) {
        std::cerr << "Failed to execute query: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return rc;
    }

    // Fetching all rows into a vector
    while (sqlite3_step(stmt) == SQLITE_ROW) {
        std::vector<std::string> row;
        for (int i = 0; i < sqlite3_column_count(stmt); ++i) {
            const char* colText = reinterpret_cast<const char*>(sqlite3_column_text(stmt, i));
            row.push_back(colText ? colText : "NULL");
        }
        rows.push_back(row);
    }

    // Finalize the statement
    sqlite3_finalize(stmt);

    // Step 2: Shuffle the rows
    std::random_device rd;
    std::default_random_engine rng(rd());
    std::shuffle(rows.begin(), rows.end(), rng);

    // Step 3: Delete existing rows and re-insert shuffled rows
    std::string deleteQuery = "DELETE FROM media;";
    if (executeSQL(db, deleteQuery)) {
        std::cout << "Deleted existing rows from media table." << std::endl;
    }

    std::string insertQueryBase = "INSERT INTO media (id, type, category, price, quantity, title, imageUrl) VALUES ";

    for (const auto& row : rows) {
        std::string insertQuery = insertQueryBase + "(";
        for (size_t i = 0; i < row.size(); ++i) {
            insertQuery += "'" + escapeSingleQuotes(row[i]) + "'";
            if (i < row.size() - 1) {
                insertQuery += ", ";
            }
        }
        insertQuery += ");";

        if (!executeSQL(db, insertQuery)) {
            std::cerr << "Failed to insert row: " << insertQuery << std::endl;
        }
    }

    std::cout << "Shuffled and re-inserted rows into media table." << std::endl;

    sqlite3_close(db);
    return 0;
}
