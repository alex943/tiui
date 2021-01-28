#!/bin/bash
#
# Copyright 2012, The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Set up prog to be the path of this script, including following symlinks,
# and set up progdir to be the fully-qualified pathname of its directory.


DIST=$ANDROID_HOME/..
NAME=uiti.jar
curl -o ${DIST}/${NAME} \
        http://gitlab.alibaba-inc.com/lvwu.ws/ti_instrumentation/raw/master/dist/ui.jar

javaCmd="java"
exec "${javaCmd}" -Xmx1600M -XstartOnFirstThread \
                -Djava.ext.dirs="$ANDROID_HOME/tools/lib/x86_64:$ANDROID_HOME/tools/lib" \
                -Dcom.android.uiautomator2.bindir="$ANDROID_HOME/tools" \
                -jar "${DIST}/${NAME}"