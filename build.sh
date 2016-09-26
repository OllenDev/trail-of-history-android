#!/bin/bash

# Exit on error
set -e

# Copy mock google-services file if necessary
if [ ! -f ./ranger/google-services.json ]; then
  echo "Using mock google-services.json"
  cp ./ranger/mock-google-services.json ./ranger/google-services.json
fi

if [ ! -f ./app/google-services.json ]; then
  echo "Using mock google-services.json"
  cp ./app/mock-google-services.json ./app/google-services.json
fi

./gradlew check -Dpre-dex=false

# Emulator Management: Create, Start and Wait
echo no | android create avd --force -n test -t android-22 --abi armeabi-v7a
emulator -avd test -no-audio -no-window &
android-wait-for-emulator
adb shell input keyevent 82 &

./gradlew connectedAndroidTest
