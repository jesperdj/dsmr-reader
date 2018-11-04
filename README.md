# DSMR Reader for Raspberry Pi

## Prerequisites

On the Raspberry Pi:

* Java Runtime Environment for Java 8, for example: `sudo apt-get install openjdk-8-jre-headless`
* [WiringPi](http://wiringpi.com/): `sudo apt-get install wiringpi`
* [Pi4J](http://pi4j.com/) version 1.2-SNAPSHOT (see below)

This uses Pi4J 1.2-SNAPSHOT instead of 1.1 because 1.2-SNAPSHOT has support for the Raspberry Pi Zero W, which is what I am using for this project.

To install Pi4J 1.2-SNAPSHOT:

    wget http://get.pi4j.com/download/pi4j-1.2-SNAPSHOT.deb
    sudo dpkg -i pi4j-1.2-SNAPSHOT.deb

(See [installation instructions](http://pi4j.com/install.html) - note that the easy option installs Pi4J 1.1 which is not what you need).

Ofcourse, you need a cable to connect the P1 port of your smart meter to an USB port of the Raspberry Pi.

On the PC:

* JDK 8, Maven, your favorite IDE

## Building

Build with `mvn clean package`

## Running

After building, copy the JAR file `dsmr-reader-1.0-SNAPSHOT.jar` in the `target` directory to the Raspberry Pi.

Run it on the Raspberry Pi:

    java -jar dsmr-reader-1.0-SNAPSHOT.jar

If the USB port that the cable to your smart meter is connected to is different from the default `/dev/ttyUSB0`, then you can specify a different name on the command line, for example:

    java -jar dsmr-reader-1.0-SNAPSHOT.jar /dev/ttyS0

Note: The JAR file is an executable JAR with a custom manifest that points to the Pi4J JAR files, in the location where they will be installed when you installed the Pi4J package (directory: `/opt/pi4j/lib`).

## More information

See: Serial programming with Pi4J [example](http://pi4j.com/example/serial.html).

## TODO

Idea: Automatically upload file with received messages to Google Drive once a day. See [Google Drive REST API](https://developers.google.com/drive/api/v3/about-sdk).

Do this in a separate thread (not in the thread that called `dataReceived`).
