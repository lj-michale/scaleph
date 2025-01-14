import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ClickHouseParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkClickHouseStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            StepSchemaService.formatClickHouseConf(values);
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <DataSourceItem dataSource={'ClickHouse'}/>
        <ProFormText
          name={STEP_ATTR_TYPE.table}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.table'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={STEP_ATTR_TYPE.bulkSize}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.bulkSize'})}
          initialValue={20000}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormSwitch
          name={'split_mode'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormDependency name={['split_mode']}>
          {({split_mode}) => {
            if (split_mode) {
              return (
                <ProFormText
                  name={ClickHouseParams.shardingKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.shardingKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={'support_upsert'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.supportUpsert'})}
        />
        <ProFormDependency name={['support_upsert']}>
          {({support_upsert}) => {
            if (support_upsert) {
              return (
                <ProFormText
                  name={ClickHouseParams.primaryKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.primaryKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={ClickHouseParams.allowExperimentalLightweightDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.allowExperimentalLightweightDelete'})}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={ClickHouseParams.clickhouseConfArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={ClickHouseParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={ClickHouseParams.value}
                label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkClickHouseStepForm;
