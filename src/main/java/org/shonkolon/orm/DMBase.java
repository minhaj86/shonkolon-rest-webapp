package org.shonkolon.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.shonkolon.orm.annotation.Column;
import org.shonkolon.orm.annotation.DatabaseTable;
import org.shonkolon.orm.annotation.ID;
import org.shonkolon.orm.annotation.Nullable;
import org.shonkolon.orm.annotation.PK;
import org.shonkolon.orm.annotation.Type;

import com.mysql.jdbc.Driver;

public class DMBase {
	String dbUrl = "jdbc:mysql://localhost/simple_shiro_web_app";
	String dbUserName = "root";
	String dbPassword = "123qwe";
	int dbPort;
	String dbType;
	String dbDriverClass = "com.mysql.jdbc.Driver";
	String dbTableName;
	String dbTableIdColumnName;
	HashMap<String, HashMap<String, Object>> columnMap;

	public DMBase() {
		this.columnMap = new HashMap<String, HashMap<String, Object>>();
		getAnnotatedFields();
	}

	public void getAnnotatedFields() {
		Annotation[] anns = this.getClass().getAnnotations();
		for (Annotation ann : anns) {
			System.out.println(ann.annotationType().getSimpleName() + " Annotation found for Class "
					+ this.getClass().getSimpleName());
			switch (ann.annotationType().getSimpleName()) {
			case "DatabaseTable":
				DatabaseTable aDatabaseTable = (DatabaseTable) ann;
				this.dbTableName = aDatabaseTable.value();
				System.out.println(aDatabaseTable.value());
				break;
			default:
				throw new IllegalArgumentException("Invalid Class annotation");
			}
		}
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println("Field name: " + field.getName());
			Annotation[] annsf = field.getAnnotations();
			HashMap<String, Object> columnAttributes = new HashMap<String, Object>();
			for (Annotation ann : annsf) {
				System.out.println(
						ann.annotationType().getSimpleName() + " Annotation found for Field " + field.getName());
				switch (ann.annotationType().getSimpleName()) {
				case "Column":
					Column aColumn = (Column) ann;
					System.out.println(aColumn.value());
					columnAttributes.put("columnName", aColumn.value());
					break;
				case "PK":
					PK aPK = (PK) ann;
					columnAttributes.put("isPK", true);
					break;
				case "Type":
					Type aType = (Type) ann;
					System.out.println(aType.value());
					columnAttributes.put("columnType", aType.value());
					break;
				case "Nullable":
					Nullable aNullable = (Nullable) ann;
					System.out.println(aNullable.value());
					columnAttributes.put("isNullable", aNullable.value());
					break;
				case "ID":
					ID aID = (ID) ann;
					columnAttributes.put("isID", true);
					this.dbTableIdColumnName = field.getName();
					break;
				default:
					throw new IllegalArgumentException("Invalid Field annotation");
				}
			}
			columnAttributes.put("field", field);
			this.columnMap.put(field.getName(), columnAttributes);
		}
	}

	public void dump() {
		System.out.println("HASHMAP====\n" + hashPP((Map) columnMap, "==========="));
	}

	private String hashPP(final Map<String, Object> pHashMap, String... offset) {
		String retval = "";
		String delta = offset.length == 0 ? "" : offset[0];
		for (Map.Entry<String, Object> entry : pHashMap.entrySet()) {
			retval += delta + "[" + entry.getKey() + "] -> ";
			Object value = entry.getValue();
			if (value instanceof Map) {
				retval += "(Hash)\n" + hashPP((Map<String, Object>) value, delta + "  ");
			} else if (value instanceof List) {
				retval += "{";
				for (Object element : (List) value) {
					retval += element + ", ";
				}
				retval += "}\n";
			} else {
				retval += "[" + value.toString() + "]\n";
			}
		}
		return retval + "\n";
	}
	
	public void loadById(Object id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(dbDriverClass);
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql = "SELECT * FROM "+dbTableName+" WHERE "+dbTableIdColumnName+" = "+id;
			System.out.println("executing sql: "+sql);
//			sql = "INSERT INTO "+dbTableName+" ("+columnListSQL+") VALUES("+valuesListSQL+")";
//			stmt.execute(sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				for (Entry<String, HashMap<String, Object>> entry : columnMap.entrySet()) {
					Field field = (Field) entry.getValue().get("field");
					String colName = (String) entry.getValue().get("columnName");
					DBColumn colType = (DBColumn) entry.getValue().get("columnType");
					switch (colType) {
					case TYPE_VARCHAR:
						field.set(this, rs.getString(colName));
						break;
					case TYPE_LONG:
						field.set(this, rs.getInt(colName));
						break;
					default:
					}
				}
			} else {
				System.out.println("Entity not found in database");
//				throw new IllegalArgumentException("Entity not found in database");
			}
			if(rs.next()) {
				System.out.println("Multiple Entity found in database for id");
//				throw new IllegalArgumentException("Entity not found in database");
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
	}

	public void create() {
		Connection conn = null;
		Statement stmt = null;
		String columnListSQL=null;
		String valuesListSQL=null;
		for (Entry<String, HashMap<String, Object>> entry : columnMap.entrySet()) {
			Field field = (Field) entry.getValue().get("field");
			try {
				if(field.get(this) != null) {
					String val="";
					DBColumn colType = (DBColumn)entry.getValue().get("columnType");
					switch (colType) {
					case TYPE_VARCHAR:
						val = "'"+field.get(this)+"'";
						break;
					case TYPE_LONG:
						val = ""+field.get(this);
						break;
					default:
							
					}
					
					if(columnListSQL==null) {
						columnListSQL = (String) entry.getValue().get("columnName");
					} else {
						columnListSQL += ", " + (String) entry.getValue().get("columnName");
					}
					if(valuesListSQL==null) {
						valuesListSQL = val;
					} else {
						valuesListSQL += ", "+val;
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("aaaa: " + columnListSQL + "    ssss: " + valuesListSQL);
		try {
			Class.forName(dbDriverClass);
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "INSERT INTO "+dbTableName+" ("+columnListSQL+") VALUES("+valuesListSQL+")";
			stmt.execute(sql);
//			ResultSet rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String user = rs.getString("user_name");
//				String first = rs.getString("first_name");
//				String last = rs.getString("last_name");
//
//				System.out.print("ID: " + id);
//				System.out.print(", Age: " + user);
//				System.out.print(", First: " + first);
//				System.out.println(", Last: " + last);
//			}
//			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}
