FaceVerConvert
==================================

Configuration
---------------
```sh
#File
file.input=D:\\project\\doc\\ndid\\export_face_UAT_crose_FAR_FRR\\Back_up\\export_face_UAT_crose_FAR_FRR.dsv
file.output.folder=D:\\Temp\\
file.header.regex=|

#Field
field.data.id=biometricRefId
field.file.name=documentId
field.folder.type=documentIdType
field.data.detail=biometricData
field.file.type=biometricDataFormat
field.folder.primary=P
field.folder.secondary=S

#Log4j
log.config.file=log4j.properties
```

Build jar command
---------------
*Plese install gradle download from https://gradle.org/

Shell
```sh
./gradlew clean build
```
CMD
```sh
gradlew.bat clean build
```


Usage command
---------------
```sh
java -Dconfig.file=${config.properties} -jar ${FaceVerConvert.jar} ${mode}
```
  Use -Dconfig.file=${config.properties} to get your config
	
  Use -jar ${FaceVerConvert.jar} to get your jarfile to run


