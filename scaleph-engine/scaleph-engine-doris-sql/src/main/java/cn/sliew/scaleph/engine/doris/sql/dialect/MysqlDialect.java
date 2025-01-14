/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.sliew.scaleph.engine.doris.sql.dialect;

public class MysqlDialect extends SqlDialect {


    @Override
    public boolean accept(String jdbcUrl) {
        return jdbcUrl.startsWith("jdbc:mysql");
    }

    @Override
    public String getDataTypeSummaryString(String typeName, int dataType, int length, int scale) {
        return super.getDataTypeSummaryString(typeName, dataType, length, scale);
    }

    @Override
    public String getLimitationQuery(String sql, int limitation) {
        String trimmedSql = sql.trim();
        while (trimmedSql.endsWith(";")) {
            trimmedSql = trimmedSql.substring(0, trimmedSql.length() - 1).trim();
        }
        if (limitation <= 0) {
            return trimmedSql;
        } else {
            return String.format("SELECT * FROM (%s) T limit %d", trimmedSql, limitation);
        }
    }
}
