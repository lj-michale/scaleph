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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJob;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkJobMapper;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobService;
import cn.sliew.scaleph.engine.flink.service.convert.WsFlinkJobConvert;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobListParam;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WsFlinkJobServiceImpl implements WsFlinkJobService {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private WsFlinkJobMapper flinkJobMapper;

    @Override
    public Page<WsFlinkJobDTO> list(WsFlinkJobListParam param) {
        final Page<WsFlinkJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        final WsFlinkJob wsFlinkJob = BeanUtil.copy(param, new WsFlinkJob());
        WsFlinkJobInstance instance = new WsFlinkJobInstance();
        instance.setJobState(param.getFlinkJobState());
        wsFlinkJob.setWsFlinkJobInstance(instance);
        final Page<WsFlinkJob> flinkJobPage = flinkJobMapper.list(page, wsFlinkJob);
        Page<WsFlinkJobDTO> result =
                new Page<>(flinkJobPage.getCurrent(), flinkJobPage.getSize(), flinkJobPage.getTotal());
        List<WsFlinkJobDTO> dtoList = WsFlinkJobConvert.INSTANCE.toDto(flinkJobPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkJobDTO selectOne(Long id) {
        final WsFlinkJob record = flinkJobMapper.selectOne(id);
        return WsFlinkJobConvert.INSTANCE.toDto(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int insert(WsFlinkJobDTO dto) {
        try {
            final WsFlinkJob record = WsFlinkJobConvert.INSTANCE.toDo(dto);
            record.setCode(defaultUidGenerator.getUID());
            return flinkJobMapper.insert(record);
        } catch (UidGenerateException e) {
            Rethrower.throwAs(e);
            return -1;
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int update(WsFlinkJobDTO dto) {
        final WsFlinkJob record = WsFlinkJobConvert.INSTANCE.toDo(dto);
        return flinkJobMapper.updateById(record);
    }

    public int delete(Long id) {
        return flinkJobMapper.deleteById(id);
    }

}