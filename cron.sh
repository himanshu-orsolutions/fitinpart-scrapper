word_count=$( jps | grep FitinpartScrapperApplication | wc -w )
if [ $word_count -eq 0 ]
then
	echo "starting application again" >> cron_log.txt
	cd /home/ubuntu/fitinpart-scrapper
	mvn spring-boot:run &	
else
	echo "Application is running" >> cron_log.txt
fi