package com.tarento.analytics.repository.rowmapper;
 
import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
 
public class SqlDataMapper {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(SqlDataMapper.class);
 
	public class RoleDashboardMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("id", rs.getString("dashboard_id")); 
			node.put("name", rs.getString("title")); 
			return node;
		}
	}
	
	// Get Chart V2 // Dashboards
	public class VisualizationConfigurationMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("id", rs.getLong("id"));
			node.put("visualizationCode", rs.getString("visualization_code"));
			node.put("chartName", rs.getString("chart_name"));
			node.put("description", rs.getString("description"));
			node.put("chartType", rs.getString("chart_type"));
			node.put("isDecimal", rs.getBoolean("is_decimal"));
			node.put("createdBy", rs.getLong("created_by"));
			node.put("isPinned", rs.getBoolean("is_pinned"));
			ObjectMapper objectMapper = new ObjectMapper();
			/*
			 * try { String dashboardNode =
			 * objectMapper.writeValueAsString(rs.getString("dashboards"));
			 * node.put("dashboards", dashboardNode); } catch (JsonProcessingException e) {
			 * LOGGER.error(String.format("Exception while processing Json: %s ",
			 * e.getMessage())); }
			 */
			node.put("type", rs.getString("type"));
			node.put("timestamp", rs.getLong("timestamp"));
			try {
				node.put("queries", new ObjectMapper().readTree(rs.getString("queries")));
				ObjectNode additionalConfigurationNode = new ObjectMapper().readTree(rs.getString("config")).deepCopy();
				node.putAll(additionalConfigurationNode); 
			} catch (JsonMappingException e) {
				LOGGER.error(String.format("Exception while processing Json: %s ",e.getMessage()));
			} catch (JsonProcessingException e) {
				LOGGER.error(String.format("Exception while processing Json: %s ",e.getMessage()));
			} catch (SQLException e) {
				LOGGER.error(String.format("Exception while processing Json: %s ",e.getMessage()));
			} 
			return node;
		}
	}
	
	// Get Chart Configuration // Console
	public class VisualizationMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("id", rs.getLong("id"));
			node.put("visualizationCode", rs.getString("visualization_code"));
			node.put("chartName", rs.getString("chart_name"));
			node.put("description", rs.getString("description"));
			node.put("chartType", rs.getString("chart_type"));
			node.put("isDecimal", rs.getBoolean("is_decimal"));
			node.put("createdBy", rs.getLong("created_by"));
			node.put("isPinned", rs.getBoolean("is_pinned"));
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				String DashboardNode = objectMapper.writeValueAsString(rs.getString("dashboards"));
				node.put("dashboards", DashboardNode);
			} catch (JsonProcessingException e) {
				LOGGER.error(String.format("Exception while processing Json: %s ", e.getMessage()));
			}
			node.put("type", rs.getString("type"));
			node.put("queries", rs.getString("queries"));
			node.put("timestamp", rs.getLong("timestamp"));
			node.put("config", rs.getString("config"));

			return node;
		}
	}
	
	// Dashboards Configuration // Dashboards
	public class DashboardConfigurationMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("id", rs.getString("id")); 
			node.put("dashboardId", rs.getString("dashboard_id")); 
			node.put("title", rs.getString("title")); 
			node.put("name", rs.getString("title")); 
			node.put("description", rs.getString("description")); 
			node.put("isActive", rs.getBoolean("is_active"));
			node.put("style", rs.getString("style")); 
			node.put("widgetTitle", rs.getString("widget_title")); 
			node.put("showFilters", rs.getBoolean("show_filters")); 
			node.put("showWidgets", rs.getBoolean("show_widgets"));  
			node.put("showWidgetTitle", rs.getBoolean("show_widget_title")); 
			node.put("profile", rs.getString("profile")); 
			node.put("createdBy", rs.getString("created_by")); 
			node.put("isPinned", rs.getBoolean("is_pinned")); 
			node.put("type", rs.getString("type")); 
			try {
				node.put("visualizations", new ObjectMapper().readTree(rs.getString("visualizations")));
				node.put("widgetCharts", new ObjectMapper().readTree(rs.getString("widget_charts")));
				node.put("filters", new ObjectMapper().readTree(rs.getString("filters"))); 
			} catch (Exception e) {
				LOGGER.error(String.format("Exception while processing Json for Visualizations and Widget Configurations : %s ",e.getMessage()));
			} 
			node.put("showDateFilter", rs.getBoolean("show_date_filter"));
			node.put("dashboardConfig", rs.getString("dashboard_config")); 
			System.out.println(rs.getString("access"));
			node.put("access", rs.getString("access")); 
			node.put("timestamp", rs.getLong("timestamp")); 
			return node;
		}
	}
	
	// Dashboards Configuration // Console
	public class DashboardMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("id", rs.getString("id")); 
			node.put("dashboardId", rs.getString("dashboard_id")); 
			node.put("title", rs.getString("title")); 
			node.put("name", rs.getString("title"));
			node.put("description", rs.getString("description")); 
			node.put("isActive", rs.getBoolean("is_active"));
			node.put("style", rs.getString("style")); 
			node.put("widgetTitle", rs.getString("widget_title")); 
			node.put("showFilters", rs.getBoolean("show_filters")); 
			node.put("showWidgets", rs.getBoolean("show_widgets"));  
			node.put("showWidgetTitle", rs.getBoolean("show_widget_title")); 
			node.put("profile", rs.getString("profile")); 
			node.put("createdBy", rs.getString("created_by")); 
			node.put("isPinned", rs.getBoolean("is_pinned")); 
			node.put("type", rs.getString("type")); 
			node.put("widgetCharts", rs.getString("widget_charts")); 
			node.put("layoutConfig", rs.getString("layout_config")); 
			node.put("filters", rs.getString("filters")); 
			node.put("showDateFilter", rs.getBoolean("show_date_filter"));
			node.put("dashboardConfig", rs.getString("dashboard_config")); 
			node.put("access", rs.getString("access")); 
			node.put("timestamp", rs.getLong("timestamp")); 
			return node;
		}
	}
 
	public class DataSourceMapper implements RowMapper<JsonNode> {
		  public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
		   ObjectNode node = JsonNodeFactory.instance.objectNode();
		   node.put("id", rs.getString("id")); 
		   node.put("title", rs.getString("title")); 
		   node.put("srcType", rs.getString("src_type"));
		   node.put("createdBy", rs.getString("created_by")); 
		   node.put("timestamp", rs.getString("timestamp")); 
		   node.put("isPinned", rs.getBoolean("is_pinned")); 
		   node.put("description", rs.getString("description")); 
		   node.put("url", rs.getString("src_url")); 
		   node.put("port", rs.getString("port")); 
		   node.put("username", rs.getString("username")); 
		   node.put("password", rs.getString("password")); 
		   node.put("type", rs.getString("type")); 
		   node.put("timestamp", rs.getLong("timestamp")); 
		   return node;
		  }
		 }

	 
	public class ActionMapper implements RowMapper<JsonNode> {
		  public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
		   ObjectNode node = JsonNodeFactory.instance.objectNode();
		   node.put("id", rs.getLong("id")); 
		   node.put("name", rs.getString("name")); 
		   node.put("displayName", rs.getString("display_name"));
		   node.put("url", rs.getString("url")); 
		   node.put("serviceCode", rs.getString("service_code")); 
		   node.put("enabled", rs.getBoolean("enabled")); 
		   node.put("createdUser", rs.getLong("created_user")); 
		   node.put("createdDate", rs.getLong("created_date")); 
		   node.put("updatedUser", rs.getLong("update_user")); 
		   node.put("updatedDate", rs.getLong("update_date")); 
		   return node;
		  }
		 }
	
	public class VisualizationNameMapper implements RowMapper<JsonNode> {
		public JsonNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			node.put("name", rs.getString("chart_name")); 
			node.put("description", rs.getString("description")); 
			return node;
		}
	}
 
}