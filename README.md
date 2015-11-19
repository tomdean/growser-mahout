# Mahout for Growser

Basic implementation of an Item-based recommender using Mahout 0.9 for the [Growser](https://github.com/tomdean/growser/) project.

Requires Java 1.8+ and Maven.

## Building

```bash
mvn package
```

## Running

```bash
mvn exec:java -Dsrc=... -Dout=... -Drepos=... -DnumRepos=... -DbatchSize=...
```

|Property|Optional|Description|
|--------|--------|-----------|
|**src**|No|A CSV file with GitHub events (repo_id, login_id, event, timestamp)|
|**out**|No|Save recommendations to this file (compressed).|
|**repos**|No|A CSV file (pre-sorted) to use for repository IDs (first field).|
|**numRepos**|Yes|(Default 10,000) The number of repositories to generate recommendations for.|
|**batchSize**|Yes|(Default 100) Break repositories in chunks of this size when generating recommendations.|