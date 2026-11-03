#!/bin/bash

echo "开始部署${1}服务"
git pull --rebase
app=${1}
sys_suffix="system"
workflow_suffix="workflow"
gateway_suffix="gateway"
prepose_midfix="prepose"
service_midfix="service"
auth_suffix="auth-parent"
actuator_suffix="actuator"
dslapp=""
cd $app
echo "构建工作空间:`pwd`"
echo "mvn 编译构建开始"
mvn clean install -Dmaven.test.skip=true
echo "mvn 编译构建完成"
if [[ "$app" == *"$sys_suffix" || "$app" == *"$workflow_suffix" ]];then
  app="${app}-start"
  cd "${app}/target"
elif [[ "$app" == *"$auth_suffix" ]]; then
  app=bytter-auth
  echo "启动路径${app}"
  cd "${app}/target"
elif [[ "$app" == *"$gateway_suffix" || "$app" == *"$actuator_suffix" ]]; then
  echo "启动路径${app}"
  cd target
elif [[ "$app" == bytter-manager-account ]]; then
  app=bytter-finacial-start
  echo "启动路径${app}"
  cd "${app}/target"
elif [[ "$app" == bytter-manager-plan ]]; then
  app=bytter-plan-start
  echo "启动路径${app}"
  cd "${app}/target"
elif [[ "$app" == *"$prepose_midfix"* ]]; then
  app="${app%-${prepose_midfix}*}${app#*${prepose_midfix}}-start"
  cd "${app}/target"
elif [[ "$app" == bytter-job ]]; then
  app=bytter-job
  cd target
else
  app="${app%-${service_midfix}*}${app#*${service_midfix}}-start"
  cd "${app}/target"
fi
echo "启动工作空间:`pwd`"
echo "========================app name : $app"
if [[ "$app" == bytter-gateway ]]; then
  dslapp="bytter-gateway"
elif [[ "$app" == bytter-auth ]]; then
  dslapp="bytter-auth"
elif [[ "$app" == bytter-system-start ]]; then
  dslapp="bytter-system-start"
elif [[ "$app" == bytter-workflow-start ]]; then
  dslapp="bytter-workflow-start"
elif [[ "$app" == bytter-payment-start ]]; then
  dslapp="bytter-payment-start"
elif [[ "$app" == bytter-plan-start ]]; then
  dslapp="bytter-plan-start"
elif [[ "$app" == bytter-bdlink-start ]]; then
  dslapp="bytter-bdlink-start"
elif [[ "$app" == bytter-finacial-start ]]; then
  dslapp="bytter-finacial-start"
elif [[ "$app" == bytter-bill-start ]]; then
  dslapp="bytter-bill-start"
elif [[ "$app" == bytter-billbankimp-start ]]; then
  dslapp="bytter-billbankimp-start"
elif [[ "$app" == bytter-check-start ]]; then
  dslapp="bytter-check-start"
elif [[ "$app" == bytter-increment-start ]]; then
  dslapp="bytter-increment-start"
elif [[ "$app" == bytter-report-start ]]; then
  dslapp="bytter-report-start"
elif [[ "$app" == bytter-erp-start ]]; then
  dslapp="bytter-erp-start"
elif [[ "$app" == bytter-fundwarn-start ]]; then
  dslapp="bytter-fundwarn-start"
elif [[ "$app" == bytter-job ]]; then
  dslapp="bytter-job"
else
  echo "你构建的服务非本项目微服务!!"
  exit 0
fi
echo "dslapp name:${dslapp}"
ps -ef | grep java | grep -v grep | grep $app | awk '{print $2}' | xargs --no-run-if-empty kill -9
nohup java -jar  -Xms256M -Xmx512M -Xmn256M  -server  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/dump ${dslapp}-2.0.0.RELEASE.jar --dubbo.protocol.host=192.168.0.111 >${dslapp}.log 2>&1 &
tail -f -n200 ${dslapp}.log
