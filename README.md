# tucume_app
Tucume Application

## running

    mvn clean jooby:run

## building

    mvn clean package

## docker

     docker build . -t myapp
     docker run -p 8080:8080 -it myapp