all: GH.jar.js graphhopper-tools-WITH-OSM-0.10-SNAPSHOT-jar-with-dependencies.jar.js graphhopper-tools-TRIMMED-0.10-SNAPSHOT-jar-with-dependencies.jar.js

Main.class: Main.java
	javac -cp graphhopper-tools-WITH-OSM-0.10-SNAPSHOT-jar-with-dependencies.jar $^

GH.jar: Main.class
	jar cvf $@ $^

%.jar.js: %.jar
	/opt/cheerpj/cheerpjfy.py --natives=natives/ --deps graphhopper-tools-WITH-OSM-0.10-SNAPSHOT-jar-with-dependencies.jar $^
