package de.crazything.sql;

//CHECKSTYLE:OFF
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.crazything.sql.annotation.Column;
import de.crazything.sql.annotation.NotPersistent;
import de.crazything.sql.annotation.Serial;
import de.crazything.sql.annotation.Table;

/**
 * Just for testing.
 * 
 * @author roger
 * 
 */
public class SqlExecuterPrep {
    /*
     * Don't try to persist Strings. :D You may want to use Hibernate instead...
     * :D:D:D
     */
    static int persist(final Connection con, final Connectionable myCon, final Object obj) {
	final Class<?> clazz = obj.getClass();
	final String tableName = (clazz.getAnnotation(Table.class)).value();
	final Field[] fields = clazz.getDeclaredFields();
	int c = 0;
	final StringBuilder names = new StringBuilder();
	final StringBuilder values = new StringBuilder();
	final StringBuilder query = new StringBuilder();
	query.append("insert into " + tableName + " ");
	names.append('(');
	values.append('(');
	for (final Field field : fields) {

	    field.setAccessible(true);
	    if (field.isAnnotationPresent(NotPersistent.class) || field.isAnnotationPresent(Serial.class)) {
		continue;
	    }
	    if (c != 0) {
		names.append(',');
		values.append(',');
	    }
	    String name = field.getName();
	    if (field.isAnnotationPresent(Column.class)) {
		name = field.getAnnotation(Column.class).name();
	    }
	    names.append(name);
	    try {
		final Object value = field.get(obj);
		if (field.getType() == String.class) {
		    values.append('\'').append(value).append('\'');
		} else {
		    values.append(value);
		}
	    } catch (final IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (final IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    c++;
	}
	names.append(')');
	values.append(')');
	query.append(names).append(" values ").append(values);
	// System.out.println(query.toString());
	return executeQuery(query.toString(), con, myCon);
    }

    static int executeQuery(final String query, final Connection con, final Connectionable myCon,
	    final Object... params) {
	int rows = -1;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	try {
	    if (params != null) {
		int c = 1;
		pstmt = con.prepareStatement(query);
		for (final Object p : params) {
		    pstmt.setObject(c++, p);
		}
		rows = pstmt.executeUpdate();
	    } else {
		stmt = con.createStatement();
		rows = stmt.executeUpdate(query);
	    }
	} catch (final SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {

		if (stmt != null) {
		    stmt.close();
		} else if (pstmt != null) {
		    pstmt.close();
		}
	    } catch (final Exception e) {
		System.err.println("FEHLER in getTable processing " + query
			+ " : konnte res oder stmt nicht schliessen : " + e.getMessage());
	    }
	    myCon.setFree();
	}
	return rows;
    }

    static <T> List<T> fetchObjectResultset(final Class<T> clazz, final String sql, final Connection con,
	    final Connectionable myCon, final Object... params) {
	final List<T> result = new ArrayList<T>();
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet res = null;
	final Field[] fields = clazz.getDeclaredFields();
	try {
	    if (params != null) {
		pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		int c = 1;
		for (final Object p : params) {
		    pstmt.setObject(c++, p);
		}
		res = pstmt.executeQuery();
	    } else {
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		res = stmt.executeQuery(sql);
	    }
	    while (res.next()) {
		final T item = clazz.newInstance();
		for (final Field f : fields) {
		    String name = f.getName();
		    if (f.isAnnotationPresent(Column.class)) {
			name = f.getAnnotation(Column.class).name();
		    } else if (f.isAnnotationPresent(NotPersistent.class)) {
			continue;
		    }
		    f.setAccessible(true);
		    f.set(item, res.getObject(name));
		}
		result.add(item);
	    }
	} catch (final SQLException sqle) {
	    throw new RuntimeException(sqle.getMessage(), sqle.getCause());
	} catch (final IllegalArgumentException e) {
	    throw new RuntimeException(e.getMessage(), e.getCause());
	} catch (final IllegalAccessException e) {
	    throw new RuntimeException(e.getMessage(), e.getCause());
	} catch (final InstantiationException e) {
	    throw new RuntimeException(e.getMessage(), e.getCause());
	} finally {
	    try {
		if (res != null) {
		    res.close();
		}
		if (stmt != null) {
		    stmt.close();
		} else if (pstmt != null) {
		    pstmt.close();
		}
	    } catch (final Exception e) {
		System.err.println("FEHLER in getTable processing " + sql
			+ " : konnte res oder stmt nicht schliessen : " + e.getMessage());
	    }
	    myCon.setFree();
	}
	return result;
    }
}
// CHECKSTYLE:ON