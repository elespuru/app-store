app-store
===========

A simple test implementation of a RESTful API for an app-store, built on Spray/Scala/Akka, cloud hosted on Heroku


# API Documentation #

        http://docs.elespuruappstore.apiary.io/

# Notes #

Built with the Spray framework (which in turn rides on Akka)

Why Spray ? I've been wanting to try it and/or Play for a while, and for this Spray made more sense since there
was no need for MVC etc that come along with Play. Here's a couple more opinions on why though

        http://spray.io/scala.io/
        http://blog.michaelhamrah.com/2013/06/scala-web-apis-up-and-running-with-spray-and-akka/
        http://www.cakesolutions.net/teamblogs/2013/08/02/akka-and-spray/

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

6. Or, GET a test application that's bundled already by browsing...

        http://localhost:8080/app/1

7. Or, POST to create a new application

        % curl --include --header "Content-Type: application/json" \
               --request POST \
               --data-binary "{ \"name\": \"AppName\", \"description\" : \"Some Really Really Cool App\", \"author\" : \"CoolestApps\", \"price\" : 1.00 }" \
                    "http://localhost:8080/apps"


## Accessing the cloud hosted API ##
* Heroku: