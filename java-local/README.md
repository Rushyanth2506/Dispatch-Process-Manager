# Dispatch — Process Manager

A responsive navy-and-teal CPU scheduling workspace with a Java backend.
Requires a full **JDK 17+**, including javac. No Maven, Node, or external libraries.

## Run in VS Code
Open this folder, then run:
    javac -d out src/*.java
    java -cp out Main
Open http://localhost:8080. Stop with Ctrl+C. Run from this folder.

On Windows you can also double-click start.bat. On macOS/Linux run bash start.sh.

## Features
- FCFS, non-preemptive SJF, non-preemptive Priority, Round Robin.
- Editable queue, add/remove, load sample, clear, explicitly save to data.json.
- Gantt timeline including idle periods, completion/turnaround/waiting/response metrics.
- CPU utilization and throughput measured from time zero through final completion.
- CSV export, input validation, request timeouts and visible server errors.
- Responsive layout and keyboard focus indicators.
- Input changes invalidate old results; controls lock during backend requests.
- Lower priority number wins. Ties follow arrival order, then input order.
- RR enqueues new arrivals before re-enqueuing the current process.

## API
POST /api/schedule
Text body: FCFS,2;1,0,5,2;2,1,3,1;3,2,8,3;4,3,6,2
Algorithms: FCFS, SJF, PRIORITY, RR.
Each row is pid,arrival,burst,priority.

GET /api/processes returns saved JSON.
POST /api/processes saves an array:
[{"pid":1,"at":0,"bt":5,"pr":2}]
JSON object keys must follow that order.

Limits: 100 processes; unique positive 32-bit integer IDs; arrival and priority
0–100000; burst and quantum 1–10000. Requests capped at 64 KiB.
Round Robin rejects more than 100000 timeline allocations.

## Verification
Compiled with Java 21. Tested all four sample averages:
FCFS 5.75; SJF 5.25; Priority 5.25; RR quantum 2: 9.75.
Also tested idle CPU periods, duplicate IDs, negative arrivals, invalid quantum,
save/load round trips, and frontend HTTP routes. JavaScript syntax checked.
Full interactive browser testing has not been performed.

This is an educational local application. The server listens on localhost.
