## LogHawk will help:
-	monitor and continuously tail a pre-specified/configured set of log files on the server that it is running on.
-	allow for a pre-specified match function ( contains() equivalent in JAVA) to send ONLY messages to Kafka that match/contain a specific STRING. 
-	send all messages that have been filtered to a Kafka topic that then allows other applications, including monitoring and log searching to consume them.
-	The application is NOT primarily designed to stream all contents of a log file as other alternatives such as Fluentbit () already exist, but it will be possible to also use it for that purpose.
-	lessen impact on the server resources caused by local log querying (ie. use of grep/cat/more/less commands directly on Linux servers that can have negative impact in performance)

## Wiki documents on how to install, configure and run LogHawk
https://github.com/roliveira1986/LogHawk/wiki
