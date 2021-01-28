
function packageName() {
  APK=$1
  apkanalyzer apk manifest application-id $APK
}

function ids() {
  APK=$1
  apkanalyzer resources names --config default --type id $APK
}

function androidId() {
  APK=$1
  ID2=$2 # for example: my_emptyview
  #apkanalyzer resources names --config default --type id $APK
  apkanalyzer resources value --name $ID2 --config ID --type id $APK
}

$@