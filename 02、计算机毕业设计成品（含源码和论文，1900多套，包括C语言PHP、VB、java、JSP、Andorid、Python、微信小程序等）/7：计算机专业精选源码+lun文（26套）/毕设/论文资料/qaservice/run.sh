#!/bin/sh
#测试脚本

cp=`ls -l lib | awk '{if ($9!="") {print "./lib/"$9}}' |xargs | sed  "s/ /:/g"`
cp='./resources/:'$cp


CLASSPATH=$cp

function start()
{
	java Main &
}

function stop()
{
	ps aux|grep Main | grep -v "grep" | awk '{print $2}' |xargs kill -9
}

case $1 in
	start)
		start;;
	stop)
		stop;;
	restart)
		stop
		start;;
	*)
		echo "unknown param";;
esac
