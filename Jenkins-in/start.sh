JARFILE=cnbm-client-sdk-1.0-SNAPSHOT.jar
ps -ef | grep $JARFILE | grep -v grep | awk '{print $2}' | xargs kill -9
mv target/$JARFILE /mysoft/javalog/$JARFILE
cd /mysoft/javalog
BUILD_ID=dontKillMe nohup java -jar $JARFILE > /mysoft/javalog/springboot.log &
if [ $? = 0 ];then
        echo "springboot started successfully"
fi
