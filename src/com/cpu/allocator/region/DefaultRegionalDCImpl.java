package com.cpu.allocator.region;

import com.cpu.allocator.constants.ResponseConstants;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultRegionalDCImpl implements DataCenter {
    protected String regionName;
    protected HashMap<ServerType, Float> serverTypeVsPerHourCost = new HashMap<>();
    protected List<ServerType> costEffectiveServers = new ArrayList<>();

    public DefaultRegionalDCImpl(String regionName, JSONObject perHourCostDetails) {
        this.regionName = regionName;
        constructCostDetails(perHourCostDetails);
        constructCostEffectiveServers();
    }

    protected void constructCostDetails(JSONObject perHourCostDetails) {
        for (ServerType server : ServerType.values()) {
            if (perHourCostDetails.has(server.getTypeName())) {
                serverTypeVsPerHourCost.put(server, perHourCostDetails.getFloat(server.getTypeName()));
            }
        }
    }

    protected void constructCostEffectiveServers() {
        ServerType[] availableTypes = serverTypeVsPerHourCost.keySet().toArray(new ServerType[serverTypeVsPerHourCost.size()]);

        this.costEffectiveServers = Arrays.stream(availableTypes).sorted((server1, server2) -> {
            float perCPUCostOfServer1 = serverTypeVsPerHourCost.get(server1) / server1.getCpuCount();
            float perCPUCostOfServer2 = serverTypeVsPerHourCost.get(server2) / server2.getCpuCount();

            if (perCPUCostOfServer1 < perCPUCostOfServer2) {
                return -1;
            } else if (perCPUCostOfServer1 > perCPUCostOfServer2) {
                return 1;
            } else {
                return 0;
            }

        }).collect(Collectors.toList());
    }

    @Override
    public String getRegionName() {
        return regionName;
    }

    @Override
    public JSONObject getCost(int hours, int cpus) {
        JSONObject result = new JSONObject();
        float totalCost = 0;

        JSONObject servers = new JSONObject();
        
        for (ServerType server : costEffectiveServers) {
            if (cpus > 0) {
                int serversCount = cpus / server.getCpuCount();
                cpus = cpus % server.getCpuCount();
                totalCost += serverTypeVsPerHourCost.get(server) * hours * serversCount;
                servers.put(server.getTypeName(), serversCount);
            }
        }
	
		result.put(ResponseConstants.servers.toString(), servers);
		result.put(ResponseConstants.total_cost_unformatted.toString(), totalCost);
		result.put(ResponseConstants.total_cost.toString(), "$" + totalCost);
		result.put(ResponseConstants.region.toString(), getRegionName());

        return result;
    }

    @Override
    public JSONObject getCost(int hours, int cpus, float price) {
        JSONObject result = new JSONObject();
        float totalCost = 0;
	
		JSONObject servers = new JSONObject();
	
		for (ServerType server : costEffectiveServers) {
            float costRequiredPerServer = serverTypeVsPerHourCost.get(server) * hours;

            if (price >= costRequiredPerServer) {
                int serversCount = (int) (price / costRequiredPerServer);
                float serversCost = costRequiredPerServer * serversCount;

                price = price - serversCost;
                totalCost += serversCost;
				servers.put(server.getTypeName(), serversCount);
                cpus = cpus % server.getCpuCount();
            }
        }

        if (cpus > 0) {
            throw new RuntimeException("Cannot allocate servers with required cpus under required price.");
        }
	
		result.put(ResponseConstants.servers.toString(), servers);
		result.put(ResponseConstants.total_cost_unformatted.toString(), totalCost);
		result.put(ResponseConstants.total_cost.toString(), "$" + totalCost);
		result.put(ResponseConstants.region.toString(), getRegionName());
		
        return result;
    }

    @Override
    public JSONObject getCost(int hours, float price) {
        JSONObject result = new JSONObject();
        float totalCost = 0;
	
		JSONObject servers = new JSONObject();
	
		for (ServerType server : costEffectiveServers) {
            float costRequiredPerServer = serverTypeVsPerHourCost.get(server) * hours;
            if (price >= costRequiredPerServer) {
                int serversCount = (int) (price / costRequiredPerServer);
                float serversCost = costRequiredPerServer * serversCount;

                price = price - serversCost;
                totalCost += serversCost;
				servers.put(server.getTypeName(), serversCount);
            }
        }
	
		result.put(ResponseConstants.servers.toString(), servers);
		result.put(ResponseConstants.total_cost_unformatted.toString(), totalCost);
		result.put(ResponseConstants.total_cost.toString(), "$" + totalCost);
		result.put(ResponseConstants.region.toString(), getRegionName());

        return result;
    }
}