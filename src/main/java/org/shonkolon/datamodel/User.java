package org.shonkolon.datamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.shonkolon.orm.DBColumn;
import org.shonkolon.orm.DMBase;
import org.shonkolon.orm.annotation.Column;
import org.shonkolon.orm.annotation.DatabaseTable;
import org.shonkolon.orm.annotation.ID;
import org.shonkolon.orm.annotation.Nullable;
import org.shonkolon.orm.annotation.PK;
import org.shonkolon.orm.annotation.Type;

@DatabaseTable("user")
public class User extends DMBase {

	@Column("id")
	@PK
	@Type(DBColumn.TYPE_LONG)
	@Nullable(false)
	@ID
	public int id;

	@Column("user_name")
	@Type(DBColumn.TYPE_VARCHAR)
	@Nullable(false)
	public String userName;

	@Column("first_name")
	@Type(DBColumn.TYPE_VARCHAR)
	@Nullable(false)
	public String firstName;

	@Column("last_name")
	@Type(DBColumn.TYPE_VARCHAR)
	@Nullable(false)
	public String lastName;

	public void test() {
		// getAnnotatedFields();
		dump();
		// create();
	}

}
