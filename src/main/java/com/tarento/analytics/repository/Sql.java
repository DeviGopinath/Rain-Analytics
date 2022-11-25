package com.tarento.analytics.repository;

/**
 * This interface will hold all the SQL Quries which are being used by the
 * application Internally, the inner interface will have the queries separated
 * based on the functionalities that they are associated with
 * 
 * @author Darshan Nagesh
 *
 */
public interface Sql {

	final String ID = "id";

	/**
	 * All the queries associated with the Common activities or transactions will be
	 * placed here
	 * 
	 * @author Darshan Nagesh
	 *
	 */

	public interface ConfigQueries {
		final String GET_DASHBOARDS_FOR_PROFILE = "select dash.id, dash.dashboard_id, dash.title from dashboards dash LEFT JOIN role_dashboards rd ON dash.dashboard_id = rd.dashboard_id "
				+ " LEFT JOIN role r ON r.id = rd.role_id WHERE dash.profile = ? and r.role_name = ? ";
		final String GET_DASHBOARD_CONFIG = "select id,dashboard_id,title,description,is_active,style,widget_title,show_filters,show_widgets,show_widget_title,filters,widget_charts,visualizations,profile,created_by,is_pinned,layout_config,type,show_date_filter,dashboard_config,access,timestamp from dashboards where dashboard_id = ? and profile = ?"; 
		final String GET_VISUALIZATION_BY_CODE = "select id, visualization_code, chart_name, description, queries, chart_type, is_decimal, created_by, is_pinned, dashboards, type, timestamp, config from visualizations where visualization_code = ?";
		final String SAVE_VISUALIZATIONS = "INSERT INTO visualizations (visualization_code, chart_name, description, chart_type, is_decimal,   created_by, is_pinned, dashboards, type, queries, timestamp, config) "  +
			    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		final String UPDATE_QUERIES = "UPDATE visualizations set queries = ? "  + 
				" WHERE id = ?  ";
		final String UPDATE_AGGREGATION_PATH= "UPDATE visualizations set aggregation_paths = ? "  + 
				" WHERE id = ?  ";
		final String SAVE_DASHBOARDS = "INSERT INTO dashboards (dashboard_id,title,description,is_active,style,widget_title,show_filters,show_widgets,show_widget_title,filters,widget_charts,visualizations,profile,created_by,is_pinned,layout_config,type,show_date_filter,dashboard_config,access,timestamp)" + 
			       " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final String UPDATE_FILTERS = "UPDATE dashboards set filters = ? " + 
			    "WHERE dashboard_id = ? ";
		final String UPDATE_WIDGET_CHARTS = "UPDATE dashboards set widget_charts = ? " + 
			    "WHERE dashboard_id = ? ";
		final String UPDATE_VISUALIZATIONS = "UPDATE dashboards set visualizations = ? " + 
			    "WHERE dashboard_id = ? ";
		final String SAVE_ROLE_DASHBOARDS = "INSERT INTO role_dashboards (role_id, dashboard_id, org_id) VALUES (?,?,?)";
		final String FIND_ROLE_DASHBOARDS = "SELECT dashboard_id from role_dashboards where role_id = ? and dashboard_id = ? ";
		final String SAVE_USER_ROLES = "INSERT INTO user_role (user_id, role_id, org_id) VALUES (?,?,?)";
		final String UPDATE_VISUALIZATION = "UPDATE visualizations SET visualization_code = ?, chart_name = ?, description = ?, chart_type = ?, is_decimal = ?, created_by = ?, is_pinned = ?, dashboards = ?, type = ?, queries = ?, timestamp = ?, config = ?" + 
			    " WHERE id = ?";	
		final String UPDATE_DASHBOARDS = "UPDATE dashboards SET dashboard_id = ?, title = ?, description = ?, is_active = ?, style = ?, widget_title = ?, show_filters = ?, show_widgets = ?, show_widget_title = ?, filters = ?, widget_charts = ?, visualizations = ?, profile = ?, created_by = ?, is_pinned = ?, layout_config = ?, type = ?, show_date_filter = ? ,dashboard_config = ?, access = ?, timestamp= ? " + 
			    " WHERE id = ?";
		final String UPDATE_ROLE_DASHBOARD = "UPDATE role_dashboards set role_id = ? , dashboard_id = ? , org_id = ? " + 
			    "WHERE role_id = ? AND dashboard_id = ? ";
		final String GET_ALL_DATA_SOURCES = "select * from data_source";
		final String GET_CHART_CONFIG = "select id, visualization_code, chart_name, description, chart_type, is_decimal,   created_by, is_pinned, dashboards, type, queries, timestamp, config from visualizations where visualization_code = ?";
		final String SAVE_DATA_SOURCE = "INSERT INTO data_source (title,src_type,created_by,is_pinned,description,src_url,port,username,password,type,timestamp) " + 
			       "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		final String GET_DATA_SOURCE = "select id,title,src_type,created_by,is_pinned,description,src_url,port,username,password,type, timestamp from data_source where id = ?";
		final String DELETE_ROLES_FOR_ACTION = " DELETE from role_actions where action_id = ? ";
		final String SAVE_ROLE_ACTIONS = "INSERT INTO role_actions (role_id, action_id) VALUES (?,?)";
		final String DELETE_ACTIONS_FOR_ROLE = " DELETE from role_actions where role_id = ? ";
		final String SAVE_ACTION = "INSERT INTO actions (name, display_name, url, service_code, enabled, created_user, created_date, update_user, update_date) VALUES (?,?,?,?,?,?,?,?,?)";
		final String UPDATE_ACTION = "UPDATE actions SET name = ?, display_name = ?, url = ?, service_code = ?, enabled = ?, created_user = ?, created_date = ?, update_user = ?, update_date = ? " + 
			    "WHERE id = ?";
		final String DELETE_ACTION = "DELETE from actions where id = ?";
		final String GET_ACTION = "select id, name, display_name, url, service_code, enabled, created_user, created_date, update_user, update_date from actions where id = ?";
		final String UPDATE_DATA_SOURCE = "UPDATE data_source SET title = ?,src_type = ?,created_by = ?,is_pinned = ?,description = ?,src_url = ?,port = ?,username = ?,password = ?,type = ?, timestamp=? WHERE id = ?";
		final String GET_ALL_VISUALIZATIONS = "SELECT id, visualization_code, chart_name, description, chart_type, is_decimal,   created_by, is_pinned, dashboards, type, queries, timestamp, config FROM visualizations";
		final String GET_ALL_DASHBOARDS = "SELECT id,dashboard_id,title,description,is_active,style,widget_title,show_filters,show_widgets,show_widget_title,filters,widget_charts,visualizations,profile,created_by,is_pinned,layout_config,type,show_date_filter,dashboard_config,access, timestamp FROM dashboards";
		final String DELETE_ROLES_FOR_USER = " DELETE from user_role where user_id = ? ";
		final String DELETE_USERS_FOR_ROLE = " DELETE from user_role where role_id = ? ";
		final String GET_VISUALIZATION_NAME = "SELECT chart_name, description FROM visualizations WHERE visualization_code = ?";
	}


}
