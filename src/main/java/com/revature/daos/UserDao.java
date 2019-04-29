package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

import com.revature.AppLauncher;
import com.revature.beans.User;
import com.revature.util.ConnectionUtil;

public class UserDao {
	public User saveUser(User user) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO user_account_table (user_name, password) VALUES " + 
		"(?, ?) RETURNING id";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());		
			ps.executeQuery();

		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}
		return null;
	}

	public User getUserById(int id) {
		User user = null;

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT username, password FROM users WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				user = new User(username, password);
			}
		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}

		return user;
	}

	public List<User> getUsersByLastName(String lastName) {
		List<User> users = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT id, username, password FROM users WHERE last_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, lastName);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				users.add(new User(id, username, password));
			}
		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}
		return users;
	}
}
