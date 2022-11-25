package com.tarento.analytics.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.tarento.analytics.dto.DataSourceRequest;
import com.tarento.analytics.model.ChartData;
import com.tarento.analytics.model.Columns;
import com.tarento.analytics.model.DataSource;
import com.tarento.analytics.model.LayoutDetail;
import com.tarento.analytics.model.dashboardconfig.Actions;
import com.tarento.analytics.model.dashboardconfig.Dashboards;
import com.tarento.analytics.model.dashboardconfig.DataSources;
import com.tarento.analytics.model.dashboardconfig.RoleActions;
import com.tarento.analytics.model.dashboardconfig.RoleDashboard;
import com.tarento.analytics.model.dashboardconfig.UserRole;
import com.tarento.analytics.model.dashboardconfig.Visualizations;
import com.tarento.analytics.repository.rowmapper.DataSourceRowMapper;
import com.tarento.analytics.repository.rowmapper.SqlDataMapper;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class MetadataRepository {

	public static final Logger logger = LoggerFactory.getLogger(MetadataRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryBuilder queryBuilder;

	@Autowired
	private DataSourceRowMapper dataSourceRowMapper;

	SqlDataMapper sqlDataMapper = new SqlDataMapper();

	ObjectMapper objectMapper = new ObjectMapper();

	public List<DataSource> findForCriteria(final DataSourceRequest dataSourceRequest) {
		final Map<String, Object> preparedStatementValues = new HashMap<>();
		final String queryStr = queryBuilder.getQuery(dataSourceRequest, preparedStatementValues);
		return jdbcTemplate.query(queryStr, new Object[] { dataSourceRequest.getId() }, dataSourceRowMapper);
	}

	public List<JsonNode> getDashboardsForProfile(String profile, String roleName) {
		logger.info(String.format("Profile : %s", profile));
		logger.info(String.format("Role Name : %s", roleName));
		List<JsonNode> dashboards = new ArrayList<JsonNode>();
		try {
			dashboards = jdbcTemplate.query(Sql.ConfigQueries.GET_DASHBOARDS_FOR_PROFILE,
					new Object[] { profile, roleName }, sqlDataMapper.new RoleDashboardMapper());
		} catch (Exception e) {
			logger.error(String.format(
					"Encountered an Exception while fetching all the Dashboards for the Profile on Role ID: %s ",
					e.getMessage()));
		}
		return dashboards;

	}

	public JsonNode getVisualizationByCode(String visualizationCode) {
		logger.info(String.format("Chart ID : %s", visualizationCode));
		List<JsonNode> visualizations = new ArrayList<>();
		try {
			visualizations = jdbcTemplate.query(Sql.ConfigQueries.GET_VISUALIZATION_BY_CODE,
					new Object[] { visualizationCode }, sqlDataMapper.new VisualizationConfigurationMapper());
		} catch (Exception e) {
			logger.error(String.format(
					"Encountered an Exception while fetching all the Dashboards for the Profile on Role ID: %s ",
					e.getMessage()));
		}
		if (visualizations.size() == 1) {
			return visualizations.get(0);
		} else {
			return null;
		}

	}

	public JsonNode getDashboard(String profileName, String dashboardId) {
		List<JsonNode> dashboardConfigs = new ArrayList<JsonNode>();
		try {
			dashboardConfigs = jdbcTemplate.query(Sql.ConfigQueries.GET_DASHBOARD_CONFIG,
					new Object[] { dashboardId, profileName }, sqlDataMapper.new DashboardMapper());
		} catch (Exception e) {
			logger.error(String.format(
					"Encountered an Exception while fetching all the Dashboards for the Profile on Role ID: %s ",
					e.getMessage()));
		}
		if (!dashboardConfigs.isEmpty()) {
			return dashboardConfigs.get(0);
		}
		return null;
	}

	public List<JsonNode> getDashboardConfiguration(String profileName, String dashboardId) {
		List<JsonNode> dashboardConfigs = new ArrayList<JsonNode>();
		try {
			dashboardConfigs = jdbcTemplate.query(Sql.ConfigQueries.GET_DASHBOARD_CONFIG,
					new Object[] { dashboardId, profileName }, sqlDataMapper.new DashboardConfigurationMapper());
		} catch (Exception e) {
			logger.error(String.format(
					"Encountered an Exception while fetching all the Dashboards for the Profile on Role ID: %s ",
					e.getMessage()));
		}
		return dashboardConfigs;
	}

	public JsonNode postVisualizations(Visualizations visualizations) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					String[] returnValColumn = new String[] { "id" };
					PreparedStatement statement = connection.prepareStatement(Sql.ConfigQueries.SAVE_VISUALIZATIONS,
							returnValColumn);
					statement.setString(1, visualizations.getVisualizationCode());
					if (visualizations.getChartName() != null) {
						statement.setString(2, visualizations.getChartName());
					} else {
						statement.setString(2, "");
					}
					statement.setString(3, visualizations.getDescription());
					if (visualizations.getChartType() != null) {
						statement.setString(4, visualizations.getChartType());
					} else {
						statement.setString(4, "");
					}
					statement.setBoolean(5, visualizations.getIsDecimal());
					statement.setLong(6, visualizations.getCreatedBy());
					statement.setBoolean(7, visualizations.getIsPinned());
					statement.setObject(8, visualizations.getDashboards());
					statement.setString(9, visualizations.getType());
					statement.setString(10, visualizations.getQueries());
					statement.setLong(11, visualizations.getTimestamp());
					statement.setString(12, visualizations.getConfig());
					return statement;
				}
			}, keyHolder);
			Long id = keyHolder.getKey().longValue();
			visualizations.setId(id);
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while saving a new visualization : %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(visualizations, JsonNode.class);
	}

	public JsonNode postDashboards(Dashboards dashboard) {
		ObjectNode dashboardMetaConfig = arrangeDashboardMetaInfo(dashboard);
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					String[] returnValColumn = new String[] { "id" };
					PreparedStatement statement = connection.prepareStatement(Sql.ConfigQueries.SAVE_DASHBOARDS,
							returnValColumn);
					if (dashboardMetaConfig.get("id") != null) {
						statement.setString(1, dashboardMetaConfig.get("id").asText());
					} else {
						statement.setString(1, "");
					}
					statement.setString(2, dashboardMetaConfig.get("name").asText());
					statement.setString(3, dashboardMetaConfig.get("description").asText());
					statement.setBoolean(4, dashboardMetaConfig.get("isActive").asBoolean());
					statement.setString(5, dashboardMetaConfig.get("style").asText());
					statement.setString(6, dashboard.getWidgetTitle());
					statement.setBoolean(7, dashboard.getShowFilters());
					statement.setBoolean(8, dashboard.getShowWidgets());
					statement.setBoolean(9, dashboard.getShowWidgetTitle());
					statement.setString(10, dashboardMetaConfig.get("filters").asText());
					statement.setString(11, dashboardMetaConfig.get("widgetCharts").asText());
					try {
						statement.setString(12, new ObjectMapper().writeValueAsString(dashboardMetaConfig.get("visualizations")));
					} catch (JsonProcessingException e) {
						logger.error(String.format("Encountered an exception while posting a new dashboard : %s", e.getMessage()));
					} catch (SQLException e) {
						logger.error(String.format("Encountered an exception while posting a new dashboard : %s", e.getMessage()));
					}
					statement.setString(13, dashboardMetaConfig.get("profile").asText());
					statement.setLong(14, dashboard.getCreatedBy());
					statement.setBoolean(15, dashboard.getIsPinned());
					statement.setString(16, dashboard.getLayoutConfig());
					statement.setString(17, dashboard.getType());
					statement.setBoolean(18, dashboard.getShowDateFilter());
					statement.setString(19, dashboard.getDashboardConfig());
					statement.setString(20, dashboard.getAccess());
					statement.setLong(21, dashboard.getTimestamp());
					return statement;
				}
			}, keyHolder);
			Long id = keyHolder.getKey().longValue();
			dashboard.setId(id);
//			dashboard.setVisualizations(dashboardMetaConfig.get("visualizations").toString());
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while saving a new dashboard : %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(dashboard, JsonNode.class);
	}
	
	private ObjectNode arrangeDashboardMetaInfo(Dashboards dashboard) { 
		ObjectNode dashboardMetaConfig = objectMapper.createObjectNode();
		dashboardMetaConfig.put("name", dashboard.getTitle());
		dashboardMetaConfig.put("description", dashboard.getDescription());
		dashboardMetaConfig.put("id", dashboard.getDashboardId());
		dashboardMetaConfig.put("isActive", dashboard.getIsActive());
		dashboardMetaConfig.put("style", dashboard.getStyle());
		dashboardMetaConfig.put("filters", dashboard.getFilters());
		dashboardMetaConfig.put("widgetCharts", dashboard.getWidgetCharts());
		dashboardMetaConfig.put("profile", dashboard.getProfile());

		ArrayNode visualizations = objectMapper.createArrayNode();
		//ArrayNode layoutsList = new ObjectMapper().convertValue(lc, ArrayNode.class);
		JsonNode layoutsList = null;
		try {
			layoutsList = new ObjectMapper().readTree(dashboard.getLayoutConfig()); 
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error(String.format("Encountered an exception while arranging dashboard meta info : %s", e.getMessage()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error(String.format("Encountered an exception while arranging dashboard meta info : %s", e.getMessage()));
		}
		//List<JsonNode> layoutsList = new ObjectMapper().convertValue(lc, List.class);
		if(layoutsList != null && !layoutsList.isEmpty()) { 
			layoutsList.forEach(eachLayout -> {
				ObjectNode eachVisualizationRow = objectMapper.createObjectNode();
				LayoutDetail lDetail = objectMapper.convertValue(eachLayout, LayoutDetail.class);
				eachVisualizationRow.put("row", lDetail.getRowId()+1);
				eachVisualizationRow.put("name", "");
				ArrayNode vizArray = objectMapper.createArrayNode();
				for (Columns eachColumn : lDetail.getCols()) {
					ObjectNode eachVisualization = objectMapper.createObjectNode();
					ObjectNode eachChart = objectMapper.createObjectNode();
					getUpdateVisualizationDetailForCode(eachColumn, eachChart);
					eachVisualization.put("id", eachColumn.getId()+1);
					eachVisualization.put("name", eachChart.get("name")!=null? eachChart.get("name").asText() : eachColumn.getVisualizationCode());
					eachVisualization.put("description", eachChart.get("description")!=null? eachChart.get("description").asText() : eachColumn.getVisualizationCode());

					ObjectNode dimensions = objectMapper.createObjectNode();
					dimensions.put("height", 250);
					dimensions.put("width", 12 / lDetail.getCols().size());
					eachVisualization.put("dimensions", dimensions);
					eachVisualization.put("vizType", "chart");
					eachVisualization.put("noUnit", Boolean.TRUE);
					eachVisualization.put("isCollapsible", Boolean.FALSE); 

					ArrayNode chartsArray = objectMapper.createArrayNode();
					eachChart.put("id", eachColumn.getVisualizationCode());
					eachChart.put("chartType", eachColumn.getData().getChartType());
					eachChart.put("filter", "");
					chartsArray.add(eachChart); 
					eachVisualization.put("charts", chartsArray);
					vizArray.add(eachVisualization);
				}
				eachVisualizationRow.put("vizArray", vizArray);
				visualizations.add(eachVisualizationRow);
			});
		}
		dashboardMetaConfig.put("visualizations", visualizations);
		return dashboardMetaConfig; 
	}

	private void getUpdateVisualizationDetailForCode(Columns eachColumn, ObjectNode eachChart) {
		List<JsonNode> resultNodes = null; 
		try { 
			resultNodes = jdbcTemplate.query(Sql.ConfigQueries.GET_VISUALIZATION_NAME,
					new Object[] { eachColumn.getVisualizationCode() }, sqlDataMapper.new VisualizationNameMapper()); 
		} catch (Exception ex) { 
			logger.error(String.format("Encountered an exception while fetching name for visualization code : %s ", ex.getMessage()));
		}
		if(resultNodes != null && !resultNodes.isEmpty()) { 
			for(JsonNode eachNode : resultNodes) { 
				eachChart.put("name", eachNode.get("name").asText());
				eachChart.put("code", eachColumn.getVisualizationCode());
				eachChart.put("description", eachNode.get("description").asText());
			}
		}
	}
	
	public JsonNode postUserRoleMappings(UserRole userRole) {
		try {
			if (userRole != null && userRole.getUserId() != null && userRole.getRoleIds() != null
					&& !userRole.getRoleIds().isEmpty()) {
				jdbcTemplate.update(Sql.ConfigQueries.DELETE_ROLES_FOR_USER, userRole.getUserId());
				for (int i = 0; i < userRole.getRoleIds().size(); i++) {
					jdbcTemplate.update(Sql.ConfigQueries.SAVE_USER_ROLES, userRole.getUserId(),
							userRole.getRoleIds().get(i), userRole.getOrgId());
				}
			}
			if (userRole != null && userRole.getRoleId() != null && userRole.getUserIds() != null
					&& !userRole.getUserIds().isEmpty()) {
				jdbcTemplate.update(Sql.ConfigQueries.DELETE_USERS_FOR_ROLE, userRole.getRoleId());
				for (int i = 0; i < userRole.getUserIds().size(); i++) {
					jdbcTemplate.update(Sql.ConfigQueries.SAVE_USER_ROLES, userRole.getUserIds().get(i),
							userRole.getRoleId(), userRole.getOrgId());
				}
			}
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while saving a new role dashboard :  %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(userRole, JsonNode.class);

	}

	public JsonNode updateVisualizations(Visualizations visualizations) {
		try {
			if (visualizations.getId() != null) {
				int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_VISUALIZATION,
						visualizations.getVisualizationCode(),
						visualizations.getChartName() != null ? visualizations.getChartName() : " ",
						visualizations.getDescription(),
						visualizations.getChartType() != null ? visualizations.getChartType() : " ",
						visualizations.getIsDecimal(), visualizations.getCreatedBy(), visualizations.getIsPinned(),
						visualizations.getDashboards(), visualizations.getType(), visualizations.getQueries(),
						visualizations.getTimestamp(), visualizations.getConfig(), visualizations.getId());
				if (result > 0)
					return new ObjectMapper().convertValue(visualizations, JsonNode.class);
				else
					return new ObjectMapper().convertValue(result, JsonNode.class);
			}
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating visualization : %s ", e.getMessage()));
		}
		return new ObjectMapper().convertValue(visualizations, JsonNode.class);

	}

	public JsonNode updateDashboards(Dashboards dashboards) {
		ObjectNode dashboardMetaConfig = arrangeDashboardMetaInfo(dashboards);
		String visualizationsJson = ""; 
		try {
			visualizationsJson = new ObjectMapper().writeValueAsString(dashboardMetaConfig.get("visualizations"));
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while updating a dashboard :  %s", e.getMessage()));
		}
		try {
			if (dashboards.getId() != null) {
				int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_DASHBOARDS, dashboardMetaConfig.get("id").asText(),
						dashboardMetaConfig.get("name").asText(), dashboardMetaConfig.get("description").asText() , dashboardMetaConfig.get("isActive").asBoolean(),
						dashboardMetaConfig.get("style").asText(), dashboards.getWidgetTitle(), dashboards.getShowFilters(),
						dashboards.getShowWidgets(), dashboards.getShowWidgetTitle(), dashboardMetaConfig.get("filters").asText(),
						dashboardMetaConfig.get("widgetCharts").asText(), visualizationsJson, dashboardMetaConfig.get("profile").asText(),
						dashboards.getCreatedBy(), dashboards.getIsPinned(), dashboards.getLayoutConfig(),
						dashboards.getType(), dashboards.getShowDateFilter(), dashboards.getDashboardConfig(),
						dashboards.getAccess(), dashboards.getTimestamp(), dashboards.getId());
				if (result > 0)
					return new ObjectMapper().convertValue(dashboards, JsonNode.class);
				else
					return new ObjectMapper().convertValue(result, JsonNode.class);
			}
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating dashboard : %s ", e.getMessage()));
		}
		return new ObjectMapper().convertValue(dashboards, JsonNode.class);

	}
	
	public JsonNode postRoleDashboards(RoleDashboard roleDashboard) {
		  try {
		//   if (roleDashboard != null && roleDashboard.getRoleId() != null && roleDashboard.getDashboardIdItem() != null
//		     && !roleDashboard.getRoleIds().isEmpty()) {
		    for (int i = 0; i < roleDashboard.getRoleIds().size(); i++) {
		     String result = ""; 
		     try { 
		      result = jdbcTemplate.queryForObject(Sql.ConfigQueries.FIND_ROLE_DASHBOARDS, 
		        new Object[] {roleDashboard.getRoleIds().get(i), roleDashboard.getDashboardIdItem()} , String.class);
		     } catch (Exception ex) { 
		      logger.error(
		        String.format("No matching Role Dashboard mapping found :  %s", ex.getMessage()));
		     }
		     if(StringUtils.isBlank(result)) { 
		      jdbcTemplate.update(Sql.ConfigQueries.SAVE_ROLE_DASHBOARDS, roleDashboard.getRoleIds().get(i),
		        roleDashboard.getDashboardIdItem(), roleDashboard.getOrgId());
		     }
//		    }
		   }
		  } catch (Exception e) {
		   logger.error(
		     String.format("Encountered an exception while saving a new role dashboard :  %s", e.getMessage()));
		  }
		  return new ObjectMapper().convertValue(roleDashboard, JsonNode.class);

		 }

	public Boolean updateQueriesForVisualization(String query, Long id) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_QUERIES, query, id);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating query for Visualization : %s",
					e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public Boolean updateFiltersForDashboard(String filters, String dashboardId) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_FILTERS, filters, dashboardId);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating filters for Dashboard : %s",
					e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public Boolean updateWidgetChartsForDashboard(String widgetCharts, String dashboardId) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_WIDGET_CHARTS, widgetCharts, dashboardId);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating widget charts for Dashboard : %s",
					e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public Boolean updateVisualizationsForDashboard(String visualizations, String dashboardId) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_VISUALIZATIONS, visualizations, dashboardId);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating visualizations for Dashboard : %s",
					e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public Boolean updateAggregationPathForVisualization(String aggregationPath, Long id) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_AGGREGATION_PATH, aggregationPath, id);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating query for Visualization : %s",
					e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public Boolean updateRoleDashboards(RoleDashboard roledashboard) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_ROLE_DASHBOARD, roledashboard.getRoleId(),
					roledashboard.getNewDashboardId().get(0), roledashboard.getOrgId(), roledashboard.getRoleId(),
					roledashboard.getDashboardId().get(0));
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating role dashboard : %s ", e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public JsonNode getChartConfiguration(String visualizationCode) {
		List<JsonNode> chartConfigs = new ArrayList<>();
		try {
			chartConfigs = jdbcTemplate.query(Sql.ConfigQueries.GET_CHART_CONFIG, new Object[] { visualizationCode },
					sqlDataMapper.new VisualizationMapper());
		} catch (Exception e) {
			logger.error(String.format("Encountered an Exception while fetching all the Charts for visualization: %s",
					e.getMessage()));
		}
		if (!chartConfigs.isEmpty()) {
			return chartConfigs.get(0);
		}
		return null;
	}

	public JsonNode postDataSource(DataSources dataSource) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					String[] returnValColumn = new String[] { "id" };
					PreparedStatement statement = connection.prepareStatement(Sql.ConfigQueries.SAVE_DATA_SOURCE,
							returnValColumn);
					statement.setString(1, dataSource.getTitle());
					statement.setString(2, dataSource.getSrcType());
					statement.setLong(3, dataSource.getCreatedBy());
					statement.setBoolean(4, dataSource.getIsPinned());
					statement.setString(5, dataSource.getDescription());
					statement.setString(6, dataSource.getUrl());
					statement.setInt(7, dataSource.getPort());
					statement.setString(8, dataSource.getUsername());
					statement.setString(9, dataSource.getPassword());
					statement.setString(10, dataSource.getType());
					statement.setLong(11, dataSource.getTimestamp());
					return statement;
				}
			}, keyHolder);
			Long id = keyHolder.getKey().longValue();
			dataSource.setId(id);
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while saving a new data source : %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(dataSource, JsonNode.class);
	}

	public JsonNode updateDataSource(DataSources dataSource) {
		try {
			if (dataSource.getId() != null) {
				int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_DATA_SOURCE, dataSource.getTitle(),
						dataSource.getSrcType(), dataSource.getCreatedBy(), dataSource.getIsPinned(),
						dataSource.getDescription(), dataSource.getUrl(), dataSource.getPort(),
						dataSource.getUsername(), dataSource.getPassword(), dataSource.getType(),
						dataSource.getTimestamp(), dataSource.getId());
				if (result > 0)
					return new ObjectMapper().convertValue(dataSource, JsonNode.class);
				else
					return new ObjectMapper().convertValue(result, JsonNode.class);
			}
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while updating a data source : %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(dataSource, JsonNode.class);
	}

	public JsonNode getDataSource(Long id) {
		List<JsonNode> dataSource = new ArrayList<>();
		try {
			dataSource = jdbcTemplate.query(Sql.ConfigQueries.GET_DATA_SOURCE, new Object[] { id },
					sqlDataMapper.new DataSourceMapper());
		} catch (Exception e) {
			logger.error(String.format("Encountered an Exception while fetching data source data: %s", e.getMessage()));
		}
		if (dataSource.size() > 0) {
			return dataSource.get(0);
		} else {
			return null;
		}
	}

	public List<JsonNode> getAllDataSources() {
		List<JsonNode> dataSource = new ArrayList<>();
		try {
			dataSource = jdbcTemplate.query(Sql.ConfigQueries.GET_ALL_DATA_SOURCES,
					sqlDataMapper.new DataSourceMapper());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an Exception while fetching all data sources : %s", e.getMessage()));
		}
		return dataSource;
	}

	public JsonNode postRoleActionMappings(RoleActions roleActions) {
		try {
			if (roleActions != null && roleActions.getRoleId() != null && roleActions.getActionIds() != null
					&& !roleActions.getActionIds().isEmpty()) {
				jdbcTemplate.update(Sql.ConfigQueries.DELETE_ACTIONS_FOR_ROLE, roleActions.getRoleId());
				for (int i = 0; i < roleActions.getActionIds().size(); i++) {
					jdbcTemplate.update(Sql.ConfigQueries.SAVE_ROLE_ACTIONS, roleActions.getRoleId(),
							roleActions.getActionIds().get(i));
				}
			}

			if (roleActions != null && roleActions.getActionId() != null && roleActions.getRoleIds() != null
					&& !roleActions.getRoleIds().isEmpty()) {
				jdbcTemplate.update(Sql.ConfigQueries.DELETE_ROLES_FOR_ACTION, roleActions.getActionId());
				for (int i = 0; i < roleActions.getRoleIds().size(); i++) {
					jdbcTemplate.update(Sql.ConfigQueries.SAVE_ROLE_ACTIONS, roleActions.getRoleIds().get(i),
							roleActions.getActionId());
				}
			}
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while saving a role action mapping: %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(roleActions, JsonNode.class);
	}

	public JsonNode postActions(Actions actions) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					String[] returnValColumn = new String[] { "id" };
					PreparedStatement statement = connection.prepareStatement(Sql.ConfigQueries.SAVE_ACTION,
							returnValColumn);
//					statement.setLong(1, actions.getId());
					statement.setString(1, actions.getName());
					statement.setString(2, actions.getDisplayName());
					statement.setString(3, actions.getUrl());
					statement.setString(4, actions.getServiceCode());
					statement.setBoolean(5, actions.getEnabled());
					statement.setLong(6, actions.getCreatedUser());
					statement.setLong(7, actions.getCreatedDate());
					statement.setLong(8, actions.getUpdatedUser());
					statement.setLong(9, actions.getUpdatedDate());
					return statement;
				}
			}, keyHolder);
			Long id = keyHolder.getKey().longValue();
			actions.setId(id);
//			jdbcTemplate.update(Sql.ConfigQueries.SAVE_ACTION, actions.getId(), actions.getName(),
//					actions.getDisplayName(), actions.getUrl(), actions.getServiceCode(), actions.getEnabled(),
//					actions.getCreatedUser(), actions.getCreatedDate(), actions.getUpdatedUser(),
//					actions.getUpdatedDate());
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while saving a new action : %s", e.getMessage()));
		}
		return new ObjectMapper().convertValue(actions, JsonNode.class);
	}

	public JsonNode updateActions(Actions actions) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.UPDATE_ACTION, actions.getName(),
					actions.getDisplayName(), actions.getUrl(), actions.getServiceCode(), actions.getEnabled(),
					actions.getCreatedUser(), actions.getCreatedDate(), actions.getUpdatedUser(),
					actions.getUpdatedDate(), actions.getId());
			if (result > 0)
				return new ObjectMapper().convertValue(actions, JsonNode.class);
			else
				return new ObjectMapper().convertValue(result, JsonNode.class);
	} catch (Exception e) {
		logger.error(String.format("Encountered an exception while updating actions : %s ", e.getMessage()));
	}
	return new ObjectMapper().convertValue(actions, JsonNode.class);
	}

	public Boolean deleteActions(Long id) {
		try {
			int result = jdbcTemplate.update(Sql.ConfigQueries.DELETE_ACTION, id);
			if (result > 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while deleting action : %s ", e.getMessage()));
		}
		return Boolean.FALSE;
	}

	public List<JsonNode> getAction(Long id) {
		List<JsonNode> action = new ArrayList<>();
		try {
			action = jdbcTemplate.query(Sql.ConfigQueries.GET_ACTION, new Object[] { id },
					sqlDataMapper.new ActionMapper());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an Exception while fetching data from actions: %s", e.getMessage()));
		}
		return action;
	}

	public List<JsonNode> getAllVisualizations() {
		List<JsonNode> visualizations = new ArrayList<>();
		try {
			visualizations = jdbcTemplate.query(Sql.ConfigQueries.GET_ALL_VISUALIZATIONS,
					sqlDataMapper.new VisualizationMapper());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an Exception while fetching all visualizations : %s", e.getMessage()));
		}
		return visualizations;
	}

	public List<JsonNode> getAllDashboards() {
		List<JsonNode> dashboards = new ArrayList<>();
		try {
			dashboards = jdbcTemplate.query(Sql.ConfigQueries.GET_ALL_DASHBOARDS,
					sqlDataMapper.new DashboardMapper());
		} catch (Exception e) {
			logger.error(String.format("Encountered an Exception while fetching all dashboards : %s", e.getMessage()));
		}
		return dashboards;
	}

}