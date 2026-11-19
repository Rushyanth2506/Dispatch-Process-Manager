# Dispatch — Process Manager

A CPU scheduling project with a Java local backend and a hosted web version.

## Quick demonstration
The hosted website is:
https://dispatch-os-lab-vijay-2026.uniquemanta8.chatgpt.site
It is private to the owner's ChatGPT account. For a demonstration on someone else's computer, use the local Java version below.

## Run locally (recommended for presentation)
Install JDK 17 or newer. Open the java-local folder in VS Code.
On Windows, double-click start.bat, or in a terminal run:

    javac -d out src/*.java
    java -cp out Main

Visit http://localhost:8080 in your browser. Keep the terminal running.
The app saves the process list to data.json when you press Save queue.
Stop with Ctrl+C.

## Features
FCFS, non-preemptive SJF, non-preemptive Priority and Round Robin; editable queue,
Gantt chart, per-process metrics, averages, CPU utilization, throughput, CSV export.
Lower priority number means higher scheduling priority.

## Hosted version source
hosted-worker/worker/index.js is a standalone Cloudflare Worker module with the
frontend embedded. scripts/build.sh creates the deployable build. It uses the
same scheduling API; its Save queue stores data in the browser's local storage.

## Expected demonstration results
Load Sample Data, then run:
FCFS average waiting time 5.75
SJF average waiting time 5.25
Priority average waiting time 5.25
Round Robin (quantum 2) average waiting time 9.75

## API example
POST /api/schedule with text:
FCFS,2;1,0,5,2;2,1,3,1;3,2,8,3;4,3,6,2

Every process is PID, arrival time, burst time, priority.
