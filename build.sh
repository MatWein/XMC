#!/bin/bash

mvn clean && mvn package -Djavafx.platform=win && mvn package -Djavafx.platform=linux