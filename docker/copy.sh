#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20231130.sql ./mysql/db
cp ../sql/ry_config_20231204.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../robin-iot-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy robin-iot-gateway "
cp ../robin-iot-gateway/target/robin-iot-gateway.jar ./ruoyi/gateway/jar

echo "begin copy robin-iot-auth "
cp ../robin-iot-auth/target/robin-iot-auth.jar ./ruoyi/auth/jar

echo "begin copy robin-iot-visual "
cp ../robin-iot-visual/robin-iot-monitor/target/robin-iot-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy robin-iot-modules-system "
cp ../robin-iot-modules/robin-iot-system/target/robin-iot-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy robin-iot-modules-file "
cp ../robin-iot-modules/robin-iot-file/target/robin-iot-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy robin-iot-modules-job "
cp ../robin-iot-modules/robin-iot-job/target/robin-iot-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy robin-iot-modules-gen "
cp ../robin-iot-modules/robin-iot-gen/target/robin-iot-modules-gen.jar ./ruoyi/modules/gen/jar

