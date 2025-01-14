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
 */

package cn.sliew.scaleph.dataservice.service.impl;

import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceConfig;
import cn.sliew.scaleph.dao.mapper.master.dataservice.DataserviceConfigMapper;
import cn.sliew.scaleph.dataservice.service.DataserviceConfigService;
import cn.sliew.scaleph.dataservice.service.convert.DataserviceConfigConvert;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceConfigDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceConfigAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceConfigListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceConfigUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DataserviceConfigServiceImpl implements DataserviceConfigService {

    @Autowired
    private DataserviceConfigMapper dataserviceConfigMapper;

    @Override
    public Page<DataserviceConfigDTO> list(DataserviceConfigListParam param) {
        Page<DataserviceConfig> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<DataserviceConfig> queryWrapper = Wrappers.lambdaQuery(DataserviceConfig.class)
                .eq(DataserviceConfig::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), DataserviceConfig::getName, param.getName());

        Page<DataserviceConfig> dataserviceConfigPage = dataserviceConfigMapper.selectPage(page, queryWrapper);
        Page<DataserviceConfigDTO> result = new Page<>(dataserviceConfigPage.getCurrent(), dataserviceConfigPage.getSize(), dataserviceConfigPage.getTotal());
        List<DataserviceConfigDTO> dataserviceConfigDTOS = DataserviceConfigConvert.INSTANCE.toDto(dataserviceConfigPage.getRecords());
        result.setRecords(dataserviceConfigDTOS);
        return result;
    }

    @Override
    public DataserviceConfigDTO selectOne(Long id) {
        DataserviceConfig record = dataserviceConfigMapper.getById(id);
        checkState(record != null, () -> "data service config not exists for id: " + id);
        return DataserviceConfigConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DataserviceConfigAddParam param) {
        DataserviceConfig record = BeanUtil.copy(param, new DataserviceConfig());
        return dataserviceConfigMapper.insert(record);
    }

    @Override
    public int update(DataserviceConfigUpdateParam param) {
        DataserviceConfig record = BeanUtil.copy(param, new DataserviceConfig());
        return dataserviceConfigMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dataserviceConfigMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dataserviceConfigMapper.deleteBatchIds(ids);
    }
}
