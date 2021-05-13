#!/bin/bash

mvn clean \
	&& mvn package -Djavafx.platform=win -DskipTests=true \
	&& mvn package -Djavafx.platform=linux -DskipTests=true \
	&& mvn package -Djavafx.platform=mac -DskipTests=true
