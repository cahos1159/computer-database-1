package com.excilys.cdb.dao;

import java.sql.*;
import java.util.*;

import com.excilys.cdb.exception.*;
import com.excilys.cdb.model.*;

// TODO: Throw les exceptions jusqu'au controller

public class ComputerDao extends Dao<Computer>{
	private final String SQL_SELECT_UPDATE_COMPANY = "UPDATE computer SET company_id=? WHERE id=?;";
	
	public ComputerDao() {
		super(
			"INSERT INTO computer VALUES (?,?,?,?,?);",
			"UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;",
			"DELETE FROM computer WHERE id=?;",
			"SELECT * FROM computer WHERE id=?;",
			"SELECT * FROM computer;",
			"SELECT * FROM computer LIMIT ?,?;"
		);
	}

	@Override
	public Computer create(Computer obj) throws Exception {
		int nbRow = 0;
		
		if(obj.getId() <= 0) {
			throw new InvalidIdException(obj.getId());
		}
		
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_CREATE);
			p.setInt(1,obj.getId());
			p.setString(2, obj.getName());
			p.setTimestamp(3, obj.getDateIntro());
			p.setTimestamp(4, obj.getDateDisc());
			// Help: https://stackoverflow.com/questions/14514589/inserting-null-to-an-integer-column-using-jdbc
			p.setNull(5, java.sql.Types.INTEGER);
			
			nbRow = p.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new PrimaryKeyViolationException(obj.getId());
		}
		
		if (obj.getManufacturer() != 0) {
			try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
				PreparedStatement p = conn.prepareStatement(this.SQL_SELECT_UPDATE_COMPANY);
				p.setInt(1, obj.getManufacturer());
				p.setInt(2, obj.getId());
				
				nbRow += p.executeUpdate();
				return (nbRow == 2) ? obj : null;
			} catch (SQLIntegrityConstraintViolationException e) {
				throw new ForeignKeyViolationException(obj.getManufacturer(), "company");
			}
		} else {
			return (nbRow == 1) ? obj : null;
		}
	}

	@Override
	public Computer update(Computer obj) throws Exception {
		// Read
		Computer c = this.read(obj.getId());
		if (c == null) {
			return null;
		}
		// Update name
		if (obj.getName().contentEquals("")) {
			c.setName(obj.getName());
		}
		// Update date1
		if (obj.getDateIntro() != null) {
			c.setDateIntro(obj.getDateIntro());
		}
		// Update date2
		if (obj.getDateDisc() != null) {
			c.setDateDisc(obj.getDateDisc());
		}
		// Update cid
		if (obj.getManufacturer() != -1) {
			c.setManufacturer(obj.getManufacturer());
		}
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_UPDATE);
			p.setString(1, c.getName());
			p.setTimestamp(2, c.getDateIntro());
			p.setTimestamp(3, obj.getDateDisc());
			if (c.getManufacturer() == 0) {
				p.setNull(4, java.sql.Types.INTEGER);
			} else {
				p.setInt(4, c.getManufacturer());
			}
			p.setInt(5, c.getId());
			
			return (p.executeUpdate() == 0) ? null : c;
		} catch (SQLException e) {
			throw new ForeignKeyViolationException(c.getManufacturer(), "company");
		}
	}

	@Override
	public Computer delete(Computer obj) throws Exception {
		return this.deleteById(obj.getId());
	}
	
	public Computer deleteById(int id) throws Exception {
		Computer c = this.read(id);
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_DELETE);
			p.setInt(1, id);
			
			int nbRow = p.executeUpdate();
			return (nbRow == 1) ? c : null;
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public Computer read(int id) throws Exception {
		if(id <= 0) {
			throw new InvalidIdException(id);
		}
		
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_SELECT);
			p.setInt(1, id);
			
			ResultSet r = p.executeQuery();
			if(r.first()) {
				return new Computer(id,r.getString("name"),r.getTimestamp("introduced"),r.getTimestamp("discontinued"), r.getInt("company_id"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public List<Computer> listAll() throws Exception {
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_LISTALL);
			
			ResultSet r = p.executeQuery();
			List<Computer> lst = new ArrayList<Computer>();
			while(r.next()) {
				lst.add(new Computer(r.getInt("id"),r.getString("name"),r.getTimestamp("introduced"),r.getTimestamp("discontinued"), r.getInt("company_id")));
			}
			return lst;
			
		} catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public List<Computer> list(int page, int size) throws Exception {
		if (size <= 0) {
			throw new InvalidPageSizeException(size);
		}
		if (page <= 0) {
			throw new InvalidPageValueException(page);
		}
		int offset = (page-1)*size;
		
		try (Connection conn = DriverManager.getConnection(this.DBACCESS, this.DBUSER, this.DBPASS)) {
			PreparedStatement p = conn.prepareStatement(this.SQL_LIST);
			p.setInt(1, offset);
			p.setInt(2, size);
			
			ResultSet r = p.executeQuery();
			List<Computer> lst = new ArrayList<Computer>();
			while(r.next()) {
				lst.add(new Computer(r.getInt("id"),r.getString("name"),r.getTimestamp("introduced"),r.getTimestamp("discontinued"), r.getInt("company_id")));
			}
			return lst;
			
		} catch (SQLException e) {
			throw e;
		}
	}

}
