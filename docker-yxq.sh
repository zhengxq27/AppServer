# 定义变量
WORKHOME='./yxq'
BUILD_NUMBER=$1
API_NAME="yxq"
API_VERSION="1.0"
API_PORT=9999
IMAGE_NAME="yxq"
CONTAINER_NAME=$API_NAME-$API_VERSION
docker --version
# 进入target 目录复制Dockerfile 文件
#cd $WORKSPACE/target
#cp classes/Dockerfile .
#cd $WORKHOME
#构建docker 镜像
docker build -t yxq .
#删除同名docker容器
cid=$(docker ps -a| grep "$CONTAINER_NAME" | awk '{print $1}')
if [ "$cid" != "" ]; then
   docker rm -f $cid
fi
#启动docker 容器
docker run -d -p $API_PORT:8080 --name yxq-1.0 yxq