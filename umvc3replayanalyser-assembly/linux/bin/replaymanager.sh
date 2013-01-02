#!/bin/bash
#
# Startup script for the Ultimate Marvel vs Capcom 3 Replay Manager.
# This script assumes that a valid java, version 1.7 or later, is available.

LIB_DIR=../lib

CONFIG_DIR=../etc

JAVA_OPTS=

JAVA=java

${JAVA} ${JAVA_OPTS} -cp ${CONFIG_DIR}:${LIB_DIR}/* nl.tinus.umvc3replayanalyser.gui.Umvc3ReplayManager
