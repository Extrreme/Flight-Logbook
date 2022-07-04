package dev.extrreme.logbook.sql;

import dev.extrreme.logbook.utils.obj.SQLExecutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;

/**
 * 
 * Amazing class for dealing with SQL tasks programmatically
 *
 */
@SuppressWarnings("unused")
public class SQLiteManager implements SQLManager {

	private final SQLite sql;

	public SQLiteManager(SQLite sql){
		this.sql = sql;
	}

	@Nullable
	private synchronized Connection getConnection() {
		Connection c = sql.getConnection();
		try {
			c.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Error connecting to SQL database!");
			e.printStackTrace();
		}
		try {
			if (c.isClosed()){
				return getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return c;
		}
		return c;
	}

	@Nullable
	private <T> T doWhileConnected(SQLExecutable<T> exec) {
		Connection c = getConnection();
		if (c == null) {
			return null;
		}
		try {
			return exec.execute(c);
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				System.out.println("Error closing connection to SQL database!");
			}
		}
	}

	/**
	 * Creates a table, if it does not already exist, with the specified columns
	 *
	 * @param tableName The name of the table to be created
	 * @param columns The names of the columns for the created table to have
	 * @param columnTypes The SQL types of the columns
	 */
	@Override
	public synchronized boolean createTable(String tableName, String[] columns, String[] columnTypes) {
		return createTable(tableName, columns, columnTypes, null);
	}

	/**
	 * Creates a table, if it does not already exist, with the specified columns and extra SQL syntax
	 *
	 * @param tableName The table's name
	 * @param columns The columns of the table's names
	 * @param columnTypes The types of the columns
	 * @param extra The extra SQL syntax (e.g., "PRIMARY KEY (`column_name`)")
	 */
	@Override
	public synchronized boolean createTable(String tableName, String[] columns, String[] columnTypes, String extra) {
		Boolean res = doWhileConnected(conn -> {
			if (columns.length != columnTypes.length) {
				throw new IllegalArgumentException("Length of columns does not match the length of columnTypes");
			}
			StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");
			boolean first = true;
			for (int i = 0; i < columns.length; i++) {
				if (!first) {
					query.append(", ");
				}
				else {
					first = false;
				}
				String col = columns[i];
				String type = columnTypes[i];
				query.append(col).append(" ").append(type);
			}

			if (extra != null && !extra.trim().equals("")) {
				query.append(", ").append(extra);
			}
			query.append(");");

			// Query is assembled
			Statement statement = conn.createStatement();
			statement.executeUpdate(query.toString());
			statement.close();

			return true;
		});

		return res != null && res;
	}

	/**
	 * Deletes a table, if it exists already
	 * @param tableName the table's name
	 * @return TRUE if the table was successfully dated, FALSE otherwise
	 */
	@Override
	public boolean dropTable(String tableName) {
		Boolean res = doWhileConnected(conn -> {
			String query = "DROP TABLE IF EXISTS " + tableName + ";";

			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
			statement.close();

			return true;
		});

		return res != null && res;
	}

	/**
	 * Delete all data from the table
	 *
	 * @param tableName the table to delete all the data from
	 * @return TRUE if the table successfully had all data deleted, FALSE otherwise
	 */
	@Override
	public boolean truncateTable(String tableName) {
		Boolean res = doWhileConnected(conn -> {
			String query = "DELETE FROM " + tableName + ";";

			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
			statement.close();

			return true;
		});

		return res != null && res;
	}

	/**
	 * Set a cell value in an SQL table
	 *
	 * @param tableName The table to set the values in
	 * @param keyColumn The table's PRIMARY KEY
	 * @param keyValue The table's PRIMARY KEY's value to modify the row of
	 * @param vals all columns and associated values to set in the row
	 * @return TRUE if all desired cells were successfully edited, FALSE otherwise
	 */
	@Override
	public synchronized boolean setRowInTable(String tableName, String keyColumn, String keyValue, Map<String, Object> vals) {
		Boolean res = doWhileConnected(conn -> {
			List<String> columns = new ArrayList<>();
			columns.add(keyColumn);
			columns.addAll(vals.keySet());

			List<Object> values = new ArrayList<>();
			values.add(keyValue);

			for (String key : columns){
				if (key.equals(keyColumn)) {
					continue;
				}
				values.add(vals.get(key));
			}

			StringBuilder columnBuilder = new StringBuilder("(");
			boolean first = true;
			for (String key : columns){
				if (!first) {
					columnBuilder.append(", ");
				}
				else {
					first = false;
				}
				columnBuilder.append("`").append(key).append("`");
			}
			columnBuilder.append(")");

			StringBuilder valsBuilder = new StringBuilder("(");
			first = true;
			for (String ignored : columns){
				if (!first) {
					valsBuilder.append(", ");
				}
				else {
					first = false;
				}
				valsBuilder.append("?");
			}
			valsBuilder.append(")");

			StringBuilder updateBuilder = new StringBuilder();
			first = true;
			for (String valName : vals.keySet()){
				if (!first) {
					updateBuilder.append(", ");
				}
				else {
					first = false;
				}
				updateBuilder.append("`").append(valName).append("`=?");
			}

			String replace = "INSERT INTO "+tableName+" "+ columnBuilder +" VALUES "+ valsBuilder
					+ " ON CONFLICT(`"+keyColumn+"`) DO UPDATE SET "+ updateBuilder +";";
			PreparedStatement placeStatement = conn.prepareStatement(replace);
			int n = 1;
			for (Object val : values) {
				placeStatement.setObject(n, val);
				n++;
			}
			//Now to prepare the 'UPDATE' statement
			for (String valName : vals.keySet()){
				Object o = vals.get(valName);
				if (o instanceof Blob) {
					placeStatement.setBlob(n, (Blob) o);
				} else if (o instanceof byte[]) {
					placeStatement.setBytes(n, (byte[]) o);
				} else if (o instanceof String) {
					placeStatement.setString(n, (String) o);
				} else {
					placeStatement.setObject(n, o);
				}

				n++;
			}
			placeStatement.executeUpdate();
			placeStatement.close();

			return true;
		});

		return res != null && res;
	}

	/**
	 * Set a cell value in a SQL table
	 *
	 * @param tableName The table to set the value in
	 * @param keyColumn The table's PRIMARY KEY
	 * @param keyValue The value of the table's PRIMARY KEY to modify the row of
	 * @param valueColumn The column key to set the value of in the table
	 * @param value The value to set the target cell's value to
	 * @return TRUE if the cell's value was successfully edited, FALSE otherwise
	 */
	@Override
	public synchronized boolean setValInTable(String tableName, String keyColumn, String keyValue, String valueColumn, String value) {
		Boolean res = doWhileConnected(conn -> {
			String replace = "INSERT INTO "+tableName+" (`"+keyColumn+"`, `"+valueColumn+"`) VALUES (?, ?)"
					+ " ON DUPLICATE KEY UPDATE "+valueColumn+" = ?;";
			PreparedStatement placeStatement = conn.prepareStatement(replace);
			placeStatement.setString(1, keyValue);
			placeStatement.setString(2, value+"");
			placeStatement.setString(3, value+"");
			placeStatement.executeUpdate();
			placeStatement.close();
			return true;
		});

		return res != null && res;
	}

	/**
	 * Remove a row from the table
	 *
	 * @param tableName The table name
	 * @param keyColumn The name of the table's PRIMARY KEY
	 * @param keyValue The value of the table's PRIMARY KEY at desired row
	 */
	@Override
	public synchronized boolean deleteRowInTable(String tableName, String keyColumn, String keyValue) {
		Boolean res = doWhileConnected(conn -> {
			String query = "DELETE FROM "+tableName+" WHERE "+tableName+"."+keyColumn+"=?;";
			PreparedStatement placeStatement = conn.prepareStatement(query);

			placeStatement.setString(1, keyValue);
			placeStatement.executeUpdate();
			placeStatement.close();

			return true;
		});

		return res != null && res;
	}

	/**
	 * Find a specific cell value in a SQL table
	 *
	 * @param tableName The table to search in
	 * @param keyColumn The column key to search with in the table
	 * @param keyValue The value of the key to search with in the table
	 * @param valueColumn The column key to find the value of in the table
	 * @return The value of the found cell, or null if not found
	 */
	@Override @Nullable
	public synchronized Object getValInTable(String tableName, String keyColumn, String keyValue, String valueColumn) {
		return doWhileConnected(conn -> {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM "+tableName+" WHERE "+keyColumn+" = ?;");
			statement.setString(1, keyValue);
			ResultSet res = statement.executeQuery();
			res.next();
			Object found;
			try {
				if (res.getString(keyColumn) == null) {
					found = null;
				} else {
					found = res.getObject(valueColumn);
				}
			} catch (SQLException e) {
				found = null;
			}

			res.close();
			statement.close();

			return found;
		});
	}

	/**
	 * Gets a row from the table
	 *
	 * @param tableName The table to get the row from
	 * @param keyColumn The SQL table's PRIMARY KEY name
	 * @param keyValue The SQL table's PRIMARY KEY value at the desired row
	 * @param columns The columns you want to return the values for at that row
	 * @return A map containing the column names and their values
	 */
	@Override @NotNull
	public synchronized List<Map<Object, Object>> getRowsInTable(String tableName, String keyColumn, String keyValue, String... columns) {
		List<Map<Object, Object>> rows = doWhileConnected(conn -> {
			String query = "SELECT * FROM "+tableName+" WHERE "+keyColumn+"=?;";
			PreparedStatement placeStatement = conn.prepareStatement(query);
			placeStatement.setString(1, keyValue);
			ResultSet res = placeStatement.executeQuery();
			List<Map<Object, Object>> list = new ArrayList<>();

			while (res.next()) {
				Map<Object, Object> obs = new HashMap<>();
				for (String col : columns) {
					try {
						Object o = res.getObject(col);
						obs.put(col, o);
					} catch (SQLException ignored) {}
				}
				list.add(obs);

			}

			res.close();
			placeStatement.close();

			return list;
		});

		return rows == null ? new ArrayList<>() : rows;
	}

	/**
	 * Gets all rows from the table
	 *
	 * @param tableName The table to get the rows from
	 * @param columns The columns you want to return the values for at each row
	 * @return A map containing the column names and their values
	 */
	@Override @NotNull
	public synchronized List<Map<Object, Object>> getAllRowsInTable(String tableName, String... columns) {
		List<Map<Object, Object>> rows = doWhileConnected(conn -> {
			String query = "SELECT * FROM "+tableName+";";
			PreparedStatement placeStatement = conn.prepareStatement(query);
			ResultSet res = placeStatement.executeQuery();
			List<Map<Object, Object>> list = new ArrayList<>();

			while (res.next()) {
				Map<Object, Object> obs = new HashMap<>();
				for (String col : columns) {
					try {
						Object o = res.getObject(col);
						obs.put(col, o);
					} catch (SQLException ignored) {}
				}
				list.add(obs);

			}

			res.close();
			placeStatement.close();

			return list;
		});

		return rows == null ? new ArrayList<>() : rows;
	}

	/**
	 * Get all values of a column in a table
	 *
	 * @param tableName The table
	 * @param column The column name
	 * @return A list of the values
	 */
	@Override @NotNull
	public synchronized List<Object> getColumnInTable(String tableName, String column) {
		List<Object> columnVals = doWhileConnected(conn -> {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+tableName+";");
			List<Object> list = new ArrayList<>();

			while (res.next()) {
				try {
					Object o = res.getObject(column);
					list.add(o);
				} catch (SQLException ignored) {}
			}

			res.close();
			statement.close();

			return list;
		});

		return columnVals == null ? new ArrayList<>() : columnVals;
	}

	/**
	 * Get the number of rows present in a table
	 *
	 * @param tableName The table
	 * @return The number of rows (counted by taking max rowid)
	 */
	@Override
	public synchronized int getRowCount(String tableName) {
		Integer count = doWhileConnected(conn -> {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+tableName+" ORDER by rowid DESC;");
			return res.getInt("rowid");
		});

		return count == null ? -1 : count;
	}

	/**
	 * Execute an SQL statement on the database
	 * 
	 * @param statement The statement to execute
	 * @
	 */
	@Override
	public synchronized boolean execute(String statement) {
		return execute(statement, null);
	}
	
	/**
	 * Execute an SQL statement on the database
	 * 
	 * @param statement The SQL statement to execute
	 */
	@Override
	public synchronized boolean execute(String statement, Map<Integer, Object> params) {
		Boolean res = doWhileConnected(conn -> {
			PreparedStatement placeStatement = conn.prepareStatement(statement);
			if (params != null) {
				for (Integer i : params.keySet()) {
					Object o = params.get(i);
					if (o == null) {
						o = "null";
					}
					if (o instanceof byte[]) {
						placeStatement.setBytes(i, (byte[]) o);
					} else if (o instanceof Blob) {
						placeStatement.setBlob(i, (Blob) o);
					} else if (o instanceof String) {
						placeStatement.setString(i, (String) o);
					} else {
						placeStatement.setString(i, o.toString());
					}
				}
			}

			placeStatement.executeUpdate();
			placeStatement.close();

			return true;
		});

		return res != null && res;
	}
}