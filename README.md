# Uptime Monitor

A small Scala automation project that checks a list of websites every hour, keeps a history of the results, and sends a push notification to my phone only when a site goes down or comes back up.

## What it does

1. Reads the list of sites from `sites.json`
2. Requests each site and records the status code and response time (a status below 400 counts as up)
3. Appends every check to `output/history.csv`, so the history keeps growing
4. Remembers each site's last state in `output/state.json`
5. Compares the new state with the last one and sends a notification through [ntfy.sh](https://ntfy.sh) only when something changed

When all sites stay the same, it stays quiet.

## Project layout

    sites.json            the sites to watch (edit this, no rebuild needed)
    src/
      project.scala       Scala version and libraries
      Sites.scala         reads sites.json
      Checker.scala       checks one site
      History.scala       appends to history.csv
      State.scala         remembers the last state
      Notify.scala        sends the ntfy notification
      Main.scala          ties everything together
    Dockerfile            two-stage build: compile with Scala CLI, run on a small Java image
    docker-compose.yml
    .env.example          template for the private ntfy topic

## How to run it

1. Install the ntfy app on your phone and subscribe to a long, random topic name.
2. Copy `.env.example` to `.env` and put your topic name in it.
3. Edit `sites.json` to list the sites you want to watch.
4. Run:

        docker compose up --build

The results appear in `output/`, and your phone is notified whenever a site changes state.

To run it again without rebuilding:

    docker compose run --rm monitor

Rebuild only after you change the Scala code.

## Scheduling

I run it hourly with Windows Task Scheduler, using `docker compose run --rm -T monitor`. Any scheduler works, for example cron on Linux.

## Known limits

- It can only check while the machine it runs on is on.
- If ntfy.sh itself is down, the alert can't be delivered. The check is still saved in the history.

## Tech used

Scala 3, Scala CLI, requests-scala, upickle, Docker and Docker Compose, ntfy.sh