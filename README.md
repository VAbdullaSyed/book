# Card
Spring boot REST api with jpa(h2), security(token) and tests

## Build and run tests
Run: ./gradlew bootRun
Test: ./gradlew test
We need to provide a X-API-KEY: <<secret is in codebase, security folder>> as a header

## AWS Account
Create a AWS account
Install CLI
Create access key and login into CLI

## AWS infrastructure Info
1. VPC with Single public subnet
2. Internet gateway for public Subnet. >Allows Outbound traffic and does NAT to route inbound traffic
3. ECS cluster
   a. Service
   b. Task > Points to docker Image

## Running docker Image in AWS
1. cd cloudformation ./create.sh


## Docker
./gradlew build
./gradlew build -x test
docker build -t abdulla401/card:latest .
docker build -t abdulla401/card:latest --secret AWS_REGION=eu-west-2,AWS_ACCESS_KEY_ID=X,AWS_SECRET_ACCESS_KEY=Y .
docker push abdulla401/card:latest
docker login
docker image ls

docker run -p 9000:8080 abdulla401/card 
docker run -p 9000:8080  -e AWS_REGION='eu-west-2' -p 9000:8080 abdulla401/card 
docker run -e AWS_REGION='eu-west-2' -e AWS_ACCESS_KEY_ID='X' -e AWS_SECRET_ACCESS_KEY='Y' -p 9000:8080 abdulla401/card 


