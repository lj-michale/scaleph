import {request} from "umi";
import {CatalogFunctionInfo} from "@/services/project/typings";

const url = '/api/flink/sql-gateway';

export const WsFlinkSqlGatewayService = {

  openSession: async (sessionClusterId: string | null) => {
    return request<string>(`${url}/${sessionClusterId}/openSession`, {
      method: 'POST'
    });
  },

  listCatalogs: async (sessionClusterId: string | null, sqlGatewaySessionHandleId: string) => {
    return request<Array<string>>(`${url}/${sessionClusterId}/${sqlGatewaySessionHandleId}/listCatalogs`)
  },

  listDatabases: async (sessionClusterId?: string | null, sqlGatewaySessionHandleId?: string, catalog?: string) => {
    return request<Array<string>>(`${url}/${sessionClusterId}/${sqlGatewaySessionHandleId}/listDatabases`, {
      method: 'GET',
      params: {catalog: catalog}
    })
  },

  listTables: async (sessionClusterId?: string | null, sqlGatewaySessionHandleId?: string, catalog?: string, database?: string) => {
    return request<Array<string>>(`${url}/${sessionClusterId}/${sqlGatewaySessionHandleId}/listTables`, {
      method: 'GET',
      params: {catalog: catalog, database: database}
    })
  },

  listViews: async (sessionClusterId?: string | null, sqlGatewaySessionHandleId?: string, catalog?: string, database?: string) => {
    return request<Array<string>>(`${url}/${sessionClusterId}/${sqlGatewaySessionHandleId}/listViews`, {
      method: 'GET',
      params: {catalog: catalog, database: database}
    })
  },

  listUserDefinedFunctions: async (sessionClusterId?: string | null, sqlGatewaySessionHandleId?: string, catalog?: string, database?: string) => {
    return request<Array<CatalogFunctionInfo>>(`${url}/${sessionClusterId}/${sqlGatewaySessionHandleId}/listUserDefinedFunctions`, {
      method: 'GET',
      params: {catalog: catalog, database: database}
    })
  }

}