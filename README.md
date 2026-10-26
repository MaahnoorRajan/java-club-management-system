# Java Club Management System

A console-based Java coursework project by Maahnoor Rajan. It models university clubs, student members, leaders, and activities while practising object-oriented programming.

Originally developed for coursework and imported into GitHub later. The maintenance commits document fixes made after reviewing the existing project; they do not represent backdated development activity.

## Features

- Create, list, update, search, filter, and delete academic or sports clubs.
- Register members, enforce club capacity, and reject duplicate enrollment.
- Assign leaders and remove members while preserving other memberships.
- Add club activities and generate summary, membership, activity, member, and active-club reports.
- Validate basic console input and handle errors with custom exceptions.
- Start with fictional sample data for a quick demonstration.

This repository contains a **console application**: 16 application Java source files and two standalone regression test programs. It does not contain a Swing GUI.

## Requirements

JDK 17 or newer on your PATH. No third-party libraries, database, Maven, or Gradle are required. Verified using OpenJDK 25.0.2 with the compiler's Java 17 release target; execution on an actual Java 17 runtime has not been tested.

## Build and run

From the repository folder in PowerShell:

~~~powershell
New-Item -ItemType Directory -Force build | Out-Null
$sources = Get-ChildItem -LiteralPath src -Recurse -Filter '*.java' | ForEach-Object FullName
javac --release 17 -encoding UTF-8 -d build $sources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
java -cp build Main
~~~

Choose a numbered menu option. Choose 0 to exit. Each launch reloads sample data; changes are held in memory and disappear after exit. Console decoration may display incorrectly if your terminal does not support Unicode.

## Run regression checks

~~~powershell
$sources = Get-ChildItem -Path 'src/*.java','tests/*.java' -Recurse | ForEach-Object FullName
javac --release 17 -encoding UTF-8 -d build $sources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
java -cp build LeaderRemovalTest
if ($LASTEXITCODE -ne 0) { throw 'Leader removal checks failed' }
java -cp build ClubDeletionTest
if ($LASTEXITCODE -ne 0) { throw 'Club deletion checks failed' }
~~~

Tests throw AssertionError when a check fails; Java's -ea flag is not required. They cover lowercase removal/deletion IDs, leader demotion, preservation of unrelated memberships/leadership, activity cleanup, and rejected operations. See [validation notes](docs/VALIDATION.md).

## Structure and design

| Location | Responsibility |
| --- | --- |
| src/Main.java | Console menus and sample data |
| src/models/ | Clubs, members, leaders, and activities |
| src/managers/ClubManager.java | Operations, lists, and ID lookup maps |
| src/managers/ReportGenerator.java | Console reports |
| src/interfaces/ | Display and report contracts |
| src/exceptions/ | Domain error types |
| src/utils/InputHelper.java | Console input helpers |
| tests/ | Behavioural regression checks |

See [architecture and tradeoffs](docs/ARCHITECTURE.md) and [maintenance history](docs/CHANGELOG.md).

## Current limitations

- In-memory storage only; no persistence or account authentication.
- Leadership is modelled as a member subtype carrying one leading-club ID. Reassignment and multi-club leadership need a separate design pass; they are not fully reconciled by these fixes.
- Public getters expose mutable lists. Callers can bypass manager checks.
- Dates, email format, and model setters need stronger domain validation; capacity can be lowered below current membership.
- Case handling was repaired for removal and deletion. Other operations are not guaranteed to handle lowercase IDs consistently.
- No complete end-to-end menu test suite or graphical interface is claimed.

## Coursework and assistance

The application began as my university coursework. The original local folder was preserved. Publication excludes compiled classes, IDE metadata, archives, and original reports that describe GUI files absent from the supplied source. Demo names and emails were replaced with clearly fictional values.

AI assistance was used to review and package the project, repair compilation and lifecycle bugs, add regression checks, and prepare documentation. The history separates the original import from these assisted maintenance changes. Any instructor starter material or other external references should be credited where applicable; their provenance was not established by this review.
