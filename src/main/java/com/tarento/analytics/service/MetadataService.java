package com.tarento.analytics.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tarento.analytics.dto.DataSourceRequest;
import com.tarento.analytics.dto.RoleDto;
import com.tarento.analytics.exception.AINException;
import com.tarento.analytics.model.dashboardconfig.Actions;
import com.tarento.analytics.model.dashboardconfig.Dashboard;
import com.tarento.analytics.model.dashboardconfig.Dashboards;
import com.tarento.analytics.model.dashboardconfig.DataSources;
import com.tarento.analytics.model.dashboardconfig.RoleActions;
import com.tarento.analytics.model.dashboardconfig.RoleDashboard;
import com.tarento.analytics.model.dashboardconfig.TestConnection;
import com.tarento.analytics.model.dashboardconfig.UserRole;
import com.tarento.analytics.model.dashboardconfig.Visualizations;

public interface MetadataService {

	public Object getDashboardConfiguration(String profileName, String dashboardId, String catagory, List<RoleDto> roleIds) throws AINException, IOException;
	public Dashboards getDashboard(String profileName, String dashboardId, String catagory) throws AINException, IOException;
	public List<Dashboard> getDashboardsForProfile(String profileName, List<RoleDto> roleIds) throws AINException, IOException;
	public Object getReportsConfiguration(String profileName, String dashboardId, String catagory, List<RoleDto> roleIds) throws AINException, IOException;
	public Object getReportsForProfile(String profileName, List<RoleDto> roleIds) throws AINException, IOException;
	public List<String> getUserInfo(String authToken, String userId); 
	
	public Object getDataSourceConfig(DataSourceRequest dataSourceRequest); 
	public JsonNode postChart(Visualizations visualizations); 
	public JsonNode postDashboard(Dashboards dashboard); 
	public JsonNode postUserRole(UserRole userRole);
	public JsonNode postRoleDashboard(RoleDashboard roleDashboard); 
	public Boolean updateRoleDashboard(RoleDashboard roledashboard);
	public Visualizations getChartConfiguration(String visualizationCode, String catagory)
			throws AINException, IOException;
	public JsonNode postDataSource(DataSources dataSource);
	public DataSources getDataSource(Long id, String catagory) throws AINException, IOException;
	public List<DataSources> getAllDataSources() throws AINException, IOException;
	public JsonNode postRoleAction(RoleActions roleActions);
	public JsonNode postAction(Actions actions);
	public JsonNode updateAction(Actions actions);
	public Boolean deleteAction(Long id);
	public ArrayNode getAction(Long id) throws AINException, IOException;
	public String testConnection(TestConnection testConnection);
	public List<Visualizations> getAllVisualizations() throws AINException, IOException;
	public List<Dashboards> getAllDashboards() throws AINException, IOException;

}
