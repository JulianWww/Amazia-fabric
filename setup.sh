#!/bin/bash

wget --directory-prefix=libs https://github.com/cabaletta/baritone/releases/download/v1.8.3/baritone-api-fabric-1.8.3.jar
./gradlew genEclipseRuns
