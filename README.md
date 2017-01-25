# i2p-observer
This is the source code of i2p-observer, a Java programm which collects information about the I2P network.
It was created as part of a bachelor thesis, which can be found here: https://jenix.net/i2p-observer/Analysis_of_the_I2P_Network-Bachelor_Thesis.pdf

The code works and runs fine since the last 6 or so months, but has definately room for improvements. You are free to use the code for academic research or development.
Usage by state agencies or their contractors is strictly forbidden.


# Installation

- Install and run an instance of I2P on the same machine on which i2p-observer should run.
- Clone the repository
- Add credentials for the FTP upload into data/ObserverProperties
- Probably adjust and fix some stuff

# Requirements
i2p-observer uses various third party plugins:

GeoLite2 to resolve the corresponding countries from IP adresses in the Java program:
This product includes GeoLite2 data created by MaxMind, available from http://www.maxmind.com.
			
Morris.js to create the charts on the statistic page:
Distributed under Simplified BSD License. More information at https://morrisjs.github.io/morris.js/.
			
Raphaël Javascript Library to display the charts:
Distributed under MIT License. More information at https://dmitrybaranovskiy.github.io/raphael/.
		
