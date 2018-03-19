# p6spy-checker
A small class to parse through some p6spy output and isolate the longest running queries

## What does it do?
Basically takes a p6spy output file (these can be extremely large and difficult to read) and parses it. It then does the following:
- isolates the n longest running queries (n is configurable)
- it then gets the actual queries being run for each long-running query and spits that out too.

## How to make it work
There are just a couple of things in the code you need to change.

java \-jar target/p6spy_stuff-0.0.1-SNAPSHOT-jar-with-dependencies.jar \-l "./spy36.log" \-b 11
* Options are l & b
        * \-l is the spy.log you want to analyze
        * \-b is the top number of long running operations you which to see.  (\–b 10 will show 10 longest running operations)

## Prerequisites
* Eclipse
* Java 7 or 8

## Output example:
```
3 of the longest query times:
2 - Processing
QueryTime (ms): 243
2 - Processing
QueryTime (ms): 208
--------------------------------
243  : 1521255600710|243|statement|connection 517|delete apr from alf_prop_root apr inner join temp_prop_root_obs tpra on apr.id = tpra.id and tpra.id >= 0 and tpra.id <= 9999|delete apr from alf_prop_root apr inner join temp_prop_root_obs tpra on apr.id = tpra.id and tpra.id >= 0 and tpra.id <= 9999
209  : 1521340230216|209|statement|connection 1637|select             txn.id              as id,             txn.commit_time_ms  as commit_time_ms,             count(case when node.type_qname_id != ? then 1 end) as updates,             count(case when node.type_qname_id = ? then 1 end) as deletes         from             alf_transaction txn         join alf_node node on (txn.id = node.transaction_id)          WHERE  txn.id >= ?                                                    and txn.id < ?          group by txn.commit_time_ms, txn.id         order by txn.commit_time_ms ASC, txn.id ASC|select             txn.id              as id,             txn.commit_time_ms  as commit_time_ms,             count(case when node.type_qname_id != 149 then 1 end) as updates,             count(case when node.type_qname_id = 149 then 1 end) as deletes         from             alf_transaction txn         join alf_node node on (txn.id = node.transaction_id)          WHERE  txn.id >= 0                                                    and txn.id < 2000          group by txn.commit_time_ms, txn.id         order by txn.commit_time_ms ASC, txn.id ASC
208  : 1521221280258|208|statement|connection 57|select           sequence_id as id,          activity_data as activityData,          activity_type as activityType,          post_user_id as userId,          post_date as postDate,          job_task_node as jobTaskNode,          site_network as siteNetwork,          app_tool as appTool,          status as status       from           alf_activity_post       where           status = ?|select           sequence_id as id,          activity_data as activityData,          activity_type as activityType,          post_user_id as userId,          post_date as postDate,          job_task_node as jobTaskNode,          site_network as siteNetwork,          app_tool as appTool,          status as status       from           alf_activity_post       where           status = 'PENDING'
```

## Note
The code for this checker looks for delimited patterns based on the 'logMessageFormat' found [here](https://p6spy.github.io/p6spy/2.0/configandusage.html)
