package com.tarento.analytics.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarento.analytics.dto.AdditionalConfig;
import com.tarento.analytics.dto.Config;
import com.tarento.analytics.dto.Connection;
import com.tarento.analytics.dto.DashboardData;
import com.tarento.analytics.dto.DashboardsDto;
import com.tarento.analytics.dto.DataSourceDto;
import com.tarento.analytics.dto.DatasourceData;
import com.tarento.analytics.dto.Filters;
import com.tarento.analytics.dto.Info;
import com.tarento.analytics.dto.Layout;
import com.tarento.analytics.dto.QueryData;
import com.tarento.analytics.dto.RoleDto;
import com.tarento.analytics.dto.TestConnectionDto;
import com.tarento.analytics.dto.UserDto;
import com.tarento.analytics.dto.VisualizationData;
import com.tarento.analytics.dto.VisualizationsDto;
import com.tarento.analytics.dto.Widgets;
import com.tarento.analytics.model.dashboardconfig.Dashboards;
import com.tarento.analytics.model.dashboardconfig.DataSources;
import com.tarento.analytics.model.dashboardconfig.TestConnection;
import com.tarento.analytics.model.dashboardconfig.Visualizations;

public class ModelDtoMapperUtil {

	public static final Logger logger = LoggerFactory.getLogger(ModelDtoMapperUtil.class);

	public static DataSources insertDataSource(DataSourceDto dataSourceDto, UserDto userInfo) {
		DataSources dataSource = new DataSources();
		try {
			dataSource.setId(dataSourceDto.getId());
			dataSource.setTitle(dataSourceDto.getTitle());
			dataSource.setSrcType(dataSourceDto.getSrcType());
			dataSource.setCreatedBy(userInfo.getId());
			dataSource.setIsPinned(dataSourceDto.getIsPinned());
			if (dataSourceDto.getData() != null && !dataSourceDto.getData().isEmpty()) {
				if (dataSourceDto.getData().get(0).getInfo() != null) {
					dataSource.setDescription(dataSourceDto.getData().get(0).getInfo().getDescription());
				}
				if (dataSourceDto.getData().get(0).getConnection() != null) {
					dataSource.setUrl(dataSourceDto.getData().get(0).getConnection().getUrl());
					dataSource.setPort(dataSourceDto.getData().get(0).getConnection().getPort());
					dataSource.setUsername(dataSourceDto.getData().get(0).getConnection().getUsername());
					dataSource.setPassword(dataSourceDto.getData().get(0).getConnection().getPassword());
				}

			}
			dataSource.setType(dataSourceDto.getType());
			dataSource.setTimestamp(new Date().getTime());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while setting data source data : %s ", e.getMessage()));
		}
		return dataSource;
	}

	public static List<DataSourceDto> returnDataSourceList(List<DataSources> dataSourcesList, UserDto userInfo) {
		List<DataSourceDto> dataSourceDtoList = new ArrayList<>();
		for (DataSources dataSources : dataSourcesList) {
			dataSourceDtoList.add(returnDataSource(dataSources, null));
		}
		return dataSourceDtoList;
	}

	public static DataSourceDto returnDataSource(DataSources dataSource, UserDto userInfo) {
		DataSourceDto dataSourceDto = new DataSourceDto();
		try {
			dataSourceDto.setId(dataSource.getId());
			dataSourceDto.setTitle(dataSource.getTitle());
			dataSourceDto.setSrcType(dataSource.getSrcType());
			dataSourceDto.setCreatedBy(dataSource.getCreatedBy());
			dataSourceDto.setIsPinned(dataSource.getIsPinned());
			List<DatasourceData> dataSourceDataList = new ArrayList<>();
			if (StringUtils.isNotBlank(dataSource.getUrl())) {
				Info info = new Info();
				info.setTitle(dataSource.getTitle());
				info.setDescription(dataSource.getDescription());
				Connection connection = new Connection();
				connection.setUrl(dataSource.getUrl());
				connection.setPort(dataSource.getPort());
				connection.setUsername(dataSource.getUsername());
				connection.setPassword(dataSource.getPassword());
				DatasourceData dataSourceData = new DatasourceData();
				dataSourceData.setInfo(info);
				dataSourceData.setConnection(connection);
				dataSourceDataList.add(dataSourceData);
			}
			dataSourceDto.setData(dataSourceDataList);
			dataSourceDto.setType(dataSource.getType());
			dataSourceDto.setTimestamp(dataSource.getTimestamp());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while setting data source data : %s ", e.getMessage()));
		}
		return dataSourceDto;
	}

	public static Dashboards insertDashboardData(DashboardsDto dashboardsDto, UserDto userInfo) {
		Dashboards dashboard = new Dashboards();
		try {
			dashboard.setId(dashboardsDto.getId());
			dashboard.setDashboardId(dashboardsDto.getDashboardId());
			dashboard.setTitle(dashboardsDto.getTitle());
			if (dashboardsDto.getData() != null && !dashboardsDto.getData().isEmpty()) {
				if (dashboardsDto.getData().get(0).getInfo() != null) {
					dashboard.setDescription(dashboardsDto.getData().get(0).getInfo().getDescription());
				}
				if (dashboardsDto.getData().get(0).getLayout() != null) {
					dashboard.setLayoutConfig(dashboardsDto.getData().get(0).getLayout().getLayoutConfig());
				}
				if (dashboardsDto.getData().get(0).getWidgets() != null) {
					dashboard.setWidgetCharts(dashboardsDto.getData().get(0).getWidgets().getWidgetsConfig());
				}
				if (dashboardsDto.getData().get(0).getFilters() != null) {
					dashboard.setFilters(dashboardsDto.getData().get(0).getFilters().getVisualConfig());
					dashboard.setShowDateFilter(dashboardsDto.getData().get(0).getFilters().getShowDateFilter());
				}
				if (dashboardsDto.getData().get(0).getConfig() != null) {
					dashboard.setDashboardConfig(dashboardsDto.getData().get(0).getConfig().getDashboardConfig());
				}
				if (dashboardsDto.getData().get(0).getAccess() != null
						&& !dashboardsDto.getData().get(0).getAccess().isEmpty()) {
					String accessObjects = (String) new ObjectMapper()
							.writeValueAsString(dashboardsDto.getData().get(0).getAccess());
					dashboard.setAccess(accessObjects);
				}
			}
//			dashboard.setCreatedBy((long)12335657);
			dashboard.setCreatedBy(userInfo.getId());
			dashboard.setIsPinned(dashboardsDto.getIsPinned());
			dashboard.setType(dashboardsDto.getType());
			dashboard.setIsActive(dashboardsDto.getIsActive());
			dashboard.setStyle(dashboardsDto.getStyle());
			dashboard.setWidgetTitle(dashboardsDto.getWidgetTitle());
			dashboard.setShowFilters(dashboardsDto.getShowFilters());
			dashboard.setShowWidgets(dashboardsDto.getShowWidgets());
			dashboard.setWidgetTitle(dashboardsDto.getWidgetTitle());
			dashboard.setProfile(dashboardsDto.getProfile());
			dashboard.setTimestamp(new Date().getTime());
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while setting dashboard data :  %s", e.getMessage()));
		}
		return dashboard;
	}

	public static List<DashboardsDto> returnDashboardsList(List<Dashboards> dashboardsList, UserDto userInfo) {
		List<DashboardsDto> dashboardsDtoList = new ArrayList<>();
		if (dashboardsList != null && !dashboardsList.isEmpty()) {
			for (Dashboards dashboards : dashboardsList) {
				dashboardsDtoList.add(returnDashboardData(dashboards, null));
			}
		}
		return dashboardsDtoList;
	}

	public static DashboardsDto returnDashboardData(Dashboards dashboards, UserDto userInfo) {
		DashboardsDto dto = new DashboardsDto();
		try {
			dto.setId(dashboards.getId());
			dto.setDashboardId(dashboards.getDashboardId());
			dto.setTitle(dashboards.getTitle());

			List<DashboardData> dashboardDataList = new ArrayList<>();
			DashboardData dashboardData = new DashboardData();
			Info info = new Info();
			info.setDescription(dashboards.getDescription());
			dashboardData.setInfo(info);

			Layout layout = new Layout();
			layout.setLayoutConfig(dashboards.getLayoutConfig());
			dashboardData.setLayout(layout);

			Widgets widgets = new Widgets();
			widgets.setWidgetsConfig(dashboards.getWidgetCharts());
			dashboardData.setWidgets(widgets);

			Filters filters = new Filters();
			filters.setVisualConfig(dashboards.getVisualizations());
			filters.setShowDateFilter(dashboards.getShowDateFilter());
			dashboardData.setFilters(filters);

			Config config = new Config();
			config.setDashboardConfig(dashboards.getDashboardConfig());
			dashboardData.setConfig(config);

			List<RoleDto> accessObjects = new ObjectMapper().readValue(dashboards.getAccess(),
					new ObjectMapper().getTypeFactory().constructCollectionType(List.class, RoleDto.class));
			dashboardData.setAccess(accessObjects);
			dashboardDataList.add(dashboardData);

			dto.setData(dashboardDataList);
			dto.setCreatedBy(dashboards.getCreatedBy());
			dto.setIsPinned(dashboards.getIsPinned());
			dto.setType(dashboards.getType());
			dto.setIsActive(dashboards.getIsActive());
			dto.setStyle(dashboards.getStyle());
			dto.setWidgetTitle(dashboards.getWidgetTitle());
			dto.setShowFilters(dashboards.getShowFilters());
			dto.setShowWidgets(dashboards.getShowWidgets());
			dto.setWidgetTitle(dashboards.getWidgetTitle());
			dto.setFilters(dashboards.getFilters());
			dto.setProfile(dashboards.getProfile());
			dto.setTimestamp(dashboards.getTimestamp());
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception while setting dashboard data :  %s", e.getMessage()));
		}

		return dto;
	}

	public static Visualizations insertVisualizationData(VisualizationsDto visualizationsDto, UserDto userInfo) {
		Visualizations visualizations = new Visualizations();
		try {
			visualizations.setCreatedBy(userInfo.getId());
			visualizations.setIsPinned(visualizationsDto.getIsPinned());
			visualizations.setChartName(visualizationsDto.getTitle());
			visualizations.setIsDecimal(visualizationsDto.getIsDecimal());
			visualizations.setId(visualizationsDto.getId());
			visualizations.setType(visualizationsDto.getType());
			visualizations.setVisualizationCode(visualizationsDto.getVisualizationCode());
			if (visualizationsDto.getData() != null && !visualizationsDto.getData().isEmpty()) {
				if (visualizationsDto.getData().get(0).getInfo() != null) {
					visualizations.setChartType(visualizationsDto.getData().get(0).getInfo().getChartType());
					visualizations.setDescription(visualizationsDto.getData().get(0).getInfo().getSubTitle());
				}
				if (visualizationsDto.getData().get(0).getData() != null) {
					visualizations.setQueries(visualizationsDto.getData().get(0).getData().getQuery());
				}
				if (visualizationsDto.getData().get(0).getDashboards() != null) {
					visualizations.setDashboards(visualizationsDto.getData().get(0).getDashboards());
				}
				if (visualizationsDto.getData().get(0).getAdditionalConfig() != null) {
					visualizations.setConfig(visualizationsDto.getData().get(0).getAdditionalConfig().getConfig());
				}
			}
			visualizations.setTimestamp(new Date().getTime());
		} catch (Exception e) {
			logger.error(
					String.format("Encountered an exception while setting visualization data : %s ", e.getMessage()));
		}
		return visualizations;
	}

	public static List<VisualizationsDto> returnVisualizationDataList(List<Visualizations> visualizationsList,
			UserDto userInfo) {
		List<VisualizationsDto> visualizationDtoList = new ArrayList<>();
		if (visualizationsList != null && !visualizationsList.isEmpty()) {
			for (Visualizations visualizations : visualizationsList) {
				visualizationDtoList.add(returnVisualizationData(visualizations, null));
			}
		}
		return visualizationDtoList;
	}

	public static VisualizationsDto returnVisualizationData(Visualizations visualizations, UserDto userInfo) {
		VisualizationsDto dto = new VisualizationsDto();
		try {
			dto.setCreatedBy(visualizations.getCreatedBy());
			dto.setIsPinned(visualizations.getIsPinned());
			dto.setTitle(visualizations.getChartName());
			dto.setIsDecimal(visualizations.getIsDecimal());
			dto.setId(visualizations.getId());
			dto.setType(visualizations.getType());
			List<VisualizationData> visualizationDataList = new ArrayList<>();
			if (StringUtils.isNotBlank(visualizations.getVisualizationCode())) {
				Info info = new Info();
				info.setChartType(visualizations.getChartType());
				info.setSubTitle(visualizations.getDescription());
				QueryData queryData = new QueryData();
				queryData.setQuery(visualizations.getQueries());
				AdditionalConfig additionalConfig = new AdditionalConfig();
				additionalConfig.setConfig(visualizations.getConfig());
				Object dashboards = visualizations.getDashboards();
				VisualizationData visualizationData = new VisualizationData();
				visualizationData.setInfo(info);
				visualizationData.setData(queryData);
				visualizationData.setAdditionalConfig(additionalConfig);
				visualizationData.setDashboards(dashboards);
				visualizationDataList.add(visualizationData);
			}
			dto.setData(visualizationDataList);
			;
			dto.setVisualizationCode(visualizations.getVisualizationCode());
			dto.setTimestamp(visualizations.getTimestamp());
		} catch (Exception e) {
			logger.error(
					String.format("Encounter)ed an exception while setting visualization data : %s ", e.getMessage()));
		}
		return dto;
	}

	public static TestConnection testConnection(TestConnectionDto testConnectionDto) {
		TestConnection testConnection = new TestConnection();
		try {
			if (testConnectionDto.getPort() != null) {
				testConnection.setUrl(testConnectionDto.getUrl() + ":" + testConnectionDto.getPort());
				testConnection.setPort(testConnectionDto.getPort());
			} else {
				testConnection.setUrl(testConnectionDto.getUrl());
			}
			testConnection.setUsername(testConnectionDto.getUsername());
			testConnection.setPassword(testConnectionDto.getPassword());
		} catch (Exception e) {
			logger.error(String.format("Encountered an exception in test connection : %s ", e.getMessage()));
		}
		return testConnection;
	}

}
