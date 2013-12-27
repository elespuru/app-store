app-store
===========

A simple test implementation of a RESTful API for an app-store, built on Spray/Akka, cloud hosted on Heroku


# Notes #

Built on the Spray framework (which in turn is based on Akka)
Spray 1.2.0:

        http://spray.io/
        http://spray.io/documentation/1.2.0/

Akka 2.1.4:

        http://akka.io/

Evolved from the Spray provided spray-template and examples

        https://github.com/spray/spray-template
        https://github.com/spray/spray/tree/master/examples/spray-routing/on-spray-can


# Getting Started #
## Dependencies ##
1. Any Git client for the platform you're using
2. Download, install Scala: http://www.scala-lang.org/download/
3. Download, install SBT: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
4. Assumes 1-3 are all functioning correctly, confirming so is left as an exercise to the reader


## Running Locally ##
1. Get the code...

        $ git clone https://github.com/elespuru/app-store.git elespuru-app-store

2. Hop in the directory you just cloned from GitHub...

        $ cd elespuru-app-store

3. Build the app...

        $ sbt

4. Run the test suite...

        $ test

5. If all went well, fire it up...

        $ re-start

6. Open a browser to...

        http://localhost:8080/


## Accessing the cloud hosted API ##
* Heroku: