package com.tarento.analytics.utils;

public interface PathRoutes {

	public interface DashboardApi { 
		final String DASHBOARD_ROOT_PATH = "/dashboard";
		final String METADATA_ROOT_PATH = "/metadata";

		final String TEST_PATH = "/test";
		
		final String GET_CHART = "/getChart";
		final String GET_CHART_V2 = "/getChartV2";
		final String GET_REPORT = "/getReport";
		final String GET_DASHBOARD_CONFIG = "/getDashboardConfig"; 
		final String GET_DASHBOARD = "/getDashboard"; 
		final String GET_REPORTS_CONFIG = "/getReportsConfig"; 
		final String GET_HOME_CONFIG = "/getHomeConfig";
		final String GET_ALL_VISUALIZATIONS = "/getAllVisualizations"; 
		final String ADD_NEW_DASHBOARD = "/addNewDashboard"; 
		final String MAP_DASHBOARD_VISUALIZATIOn = "/mapVisualizationToDashboard"; 
		final String MAP_VISUALIZATION_ROLE = "/mapVisualizationToRole";
		final String GET_HEADER_DATA = "/getDashboardHeader";
		final String GET_FEEDBACK_MESSAGE="/getPulseFeedbackMessage";
		final String TARGET_DISTRICT_ULB = "/targetDistrict";
		final String GET_DASHBOARDS_FOR_PROFILE= "/getDashboardsForProfile";
		final String GET_REPORTS_FOR_PROFILE= "/getReportsForProfile";
		final String SAVE_DATASOURCE_CONFIG = "/saveDataSourceConfig";
		final String SAVE_VISUALIZATION_CONFIG = "/saveVisualizationConfig";
		final String SAVE_DASHBOARD_CONFIG = "/saveDashboardConfig";
		final String GET_DATASOURCE_CONFIG = "/getDataSourceConfig";
		final String POST_CHART = "/postChart";
		final String POST_DASHBOARD = "/postDashboard";
		final String POST_ROLE_DASHBOARD = "/postRoleDashboardConfig";
		final String POST_USER_ROLE = "/postUserRoleMapping";
		final String UPDATE_VISUALIZATION = "/updateVisualizationConfig";
		final String UPDATE_ROLE_DASHBOARD = "/updateRoleDashboardConfig";
		final String UPDATE_DASHBOARDS = "/updateDashboards";
		final String GET_CHART_CONFIG = "/getChartConfig";
		final String POST_DATA_SOURCE = "/postDataSource";
		final String GET_DATA_SOURCE = "/getDataSource";
		final String GET_ALL_DATA_SOURCES = "/getAllDataSources";
		final String POST_ROLE_ACTIONS = "/postRoleActions";
		final String POST_ACTIONS = "/postActions";
		final String UPDATE_ACTIONS = "/updateActions";
		final String DELETE_ACTIONS = "/deleteActions";
		final String GET_ACTIONS = "/getActions";
		final String POST_TEST_CONNECTION = "/postTestConnection";
		final String GET_ALL_DASHBOARDS = "/getAllDashboards"; 
	}
	
	public interface MetadataApi { 
		final String METADATA_ROOT_PATH = "/meta";
		
		final String GET_DASHBOARD_CONFIG = "/getDashboardConfiguration";
		final String SAVE_DASHBOARD_CONFIG = "/saveDashboardConfiguration";
		final String GET_CATEGORIES = "/getCategories";
		final String GET_SUB_CATEGORIES = "/getSubCategories"; 
		final String GET_ITEMS = "/getItems"; 
		final String GET_MASTERS = "/getMasters"; 
		final String FLUSH_MASTERS = "/flushMasters"; 
		final String GET_PULSE_RATING_CONFIG="/getPulseRatingConfig";
		final String GET_RATING_CONFIG = "/getRatingConfiguration"; 
		final String GET_RATING_CONFIG_ENCODE = "/getRatingConfig"; 
		final String GET_CONFIG_VERSION = "/getConfigVersion"; 
		final String PULSE_VERIFY_ORG_PIN = "/verifyOrgs"; 
		final String PUT_ORG_INDEX ="/putIndex";
		final String PUT_QUERY ="/createQuery";

		
	}
}
